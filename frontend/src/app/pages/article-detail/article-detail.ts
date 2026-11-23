
import {
  Component,
  OnInit,
  inject,
  ChangeDetectorRef
} from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { forkJoin, of } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { Article, ArticleService } from '../../services/article';
import { AuthService } from '../../services/auth';
import { Comment as ArticleComment, CommentService } from '../../services/comment';
import { LikeService } from '../../services/like';
import { Tag, TagService } from '../../services/tag';

interface CommentNode extends ArticleComment {
  replies: CommentNode[];
  repliesLoaded: boolean;
  showReplies: boolean;
  replyText: string;
  submittingReply: boolean;
}

@Component({
  selector: 'app-article-detail',
  standalone: true,
  imports: [DatePipe, RouterLink],
  templateUrl: './article-detail.html',
  styleUrl: './article-detail.css'
})
export class ArticleDetail implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly articleService = inject(ArticleService);
  private readonly authService = inject(AuthService);
  private readonly commentService = inject(CommentService);
  private readonly likeService = inject(LikeService);
  private readonly tagService = inject(TagService);
  private readonly cdr = inject(ChangeDetectorRef);

  article: Article | null = null;
  comments: CommentNode[] = [];
  articleTags: Tag[] = [];

  commentText = '';
  likeCount = 0;
  liked = false;

  loading = true;
  submittingComment = false;
  errorMessage = '';
  commentError = '';

  get isLoggedIn(): boolean {
    return this.authService.isLoggedIn();
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const slug = params.get('slug');

      if (slug) {
        this.loadArticle(slug);
      }
    });
  }

  private loadArticle(slug: string): void {
    this.loading = true;
    this.errorMessage = '';
    this.article = null;
    this.comments = [];
    this.articleTags = [];
    this.cdr.markForCheck();

    this.articleService.getArticleBySlug(slug).subscribe({
      next: article => {
        this.article = article;
        this.loading = false;
        this.cdr.markForCheck();

        this.loadComments(article.id);
        this.loadArticleTags(article.id);
        this.loadLikeInformation(article.id);
      },
      error: error => {
        console.error('Failed to load article:', error);

        this.loading = false;
        this.errorMessage =
          error.error?.message || 'Unable to load this article.';

        this.cdr.markForCheck();
      }
    });
  }

  private loadComments(articleId: number): void {
    this.commentService.getComments(articleId).subscribe({
      next: response => {
        this.comments = response.map(comment =>
          this.createCommentNode(comment)
        );
        this.cdr.markForCheck();
      },
      error: error => {
        console.error('Failed to load comments:', error);
        this.cdr.markForCheck();
      }
    });
  }

  private createCommentNode(comment: ArticleComment): CommentNode {
    return {
      ...comment,
      replies: [],
      repliesLoaded: false,
      showReplies: false,
      replyText: '',
      submittingReply: false
    };
  }

  toggleReplies(comment: CommentNode): void {
    comment.showReplies = !comment.showReplies;
    this.cdr.markForCheck();

    if (!comment.showReplies || comment.repliesLoaded || !this.article) {
      return;
    }

    this.commentService.getReplies(
      this.article.id,
      comment.id
    ).subscribe({
      next: response => {
        comment.replies = response.map(reply =>
          this.createCommentNode(reply)
        );
        comment.repliesLoaded = true;
        this.cdr.markForCheck();
      },
      error: error => {
        console.error('Failed to load replies:', error);
        this.cdr.markForCheck();
      }
    });
  }

  addComment(): void {
    if (!this.requireLogin()) {
      return;
    }

    if (!this.article || !this.commentText.trim()) {
      this.commentError = 'Please write a comment.';
      return;
    }

    this.commentError = '';
    this.submittingComment = true;

    const content = this.commentText.trim();

    this.commentService.addComment(
      this.article.id,
      { content }
    ).subscribe({
      next: comment => {
        this.comments.unshift(this.createCommentNode(comment));
        this.commentText = '';
        this.submittingComment = false;
        this.cdr.markForCheck();
        this.tagMentionedUsers(comment.id, content, false);
      },
      error: error => {
        this.submittingComment = false;
        this.commentError =
          error.error?.message || 'Unable to post comment.';
        this.cdr.markForCheck();
      }
    });
  }

  addReply(parent: CommentNode): void {
    if (!this.requireLogin()) {
      return;
    }

    if (!this.article || !parent.replyText.trim()) {
      return;
    }

    const content = parent.replyText.trim();
    parent.submittingReply = true;
    this.cdr.markForCheck();

    this.commentService.addReply(
      this.article.id,
      parent.id,
      { content }
    ).subscribe({
      next: reply => {
        parent.replies.push(this.createCommentNode(reply));
        parent.repliesLoaded = true;
        parent.showReplies = true;
        parent.replyText = '';
        parent.submittingReply = false;
        this.cdr.markForCheck();

        this.tagMentionedUsers(reply.id, content, false);
      },
      error: error => {
        parent.submittingReply = false;
        this.commentError =
          error.error?.message || 'Unable to post reply.';
        this.cdr.markForCheck();
      }
    });
  }

  toggleLike(): void {
    if (!this.requireLogin()) {
      return;
    }

    if (!this.article) {
      return;
    }

    const request = this.liked
      ? this.likeService.unlikeArticle(this.article.id)
      : this.likeService.likeArticle(this.article.id);

    request.subscribe({
      next: () => {
        this.liked = !this.liked;
        this.likeCount += this.liked ? 1 : -1;
        this.cdr.markForCheck();
      },
      error: error => {
        console.error('Unable to update like:', error);
        this.cdr.markForCheck();
      }
    });
  }

  private loadLikeInformation(articleId: number): void {
    this.likeService.getLikeCount(articleId).subscribe({
      next: count => {
        this.likeCount = count;
        this.cdr.markForCheck();
      },
      error: error => {
        console.error('Unable to load like count:', error);
      }
    });

    if (this.isLoggedIn) {
      this.likeService.getLikeStatus(articleId).subscribe({
        next: response => {
          this.likeCount = response.likeCount;
          this.liked = response.likedByCurrentUser;
          this.cdr.markForCheck();
        },
        error: error => {
          console.error('Unable to load like status:', error);
        }
      });
    }
  }

  private loadArticleTags(articleId: number): void {
    this.tagService.getArticleTags(articleId).subscribe({
      next: tags => {
        this.articleTags = tags;
        this.cdr.markForCheck();
      },
      error: error => {
        console.error('Unable to load article tags:', error);
      }
    });
  }

  private tagMentionedUsers(
    commentId: number,
    content: string,
    articleTag: boolean
  ): void {
    const usernames = [
      ...new Set(
        Array.from(content.matchAll(/@([a-zA-Z0-9_]{3,30})/g))
          .map(match => match[1])
      )
    ];

    if (usernames.length === 0) {
      return;
    }

    const requests = usernames.map(username =>
      articleTag
        ? this.tagService.tagUserInArticle(
            this.article!.id,
            { username }
          )
        : this.tagService.tagUserInComment(
            commentId,
            { username }
          )
    );

    forkJoin(
      requests.map(request =>
        request.pipe(catchError(() => of(null)))
      )
    ).subscribe({
      next: () => {
        if (articleTag && this.article) {
          this.loadArticleTags(this.article.id);
        }
        this.cdr.markForCheck();
      }
    });
  }

  private requireLogin(): boolean {
    if (this.isLoggedIn) {
      return true;
    }

    this.router.navigate(['/login']);
    return false;
  }

  getAuthorInitial(username: string): string {
    return username?.charAt(0).toUpperCase() || 'U';
  }
}
