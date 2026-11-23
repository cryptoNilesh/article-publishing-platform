
import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { DatePipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Article, ArticleService } from '../../services/article';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [DatePipe, RouterLink],
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class Home implements OnInit {
  private readonly articleService = inject(ArticleService);
  private readonly cdr = inject(ChangeDetectorRef);

  articles: Article[] = [];
  loading = true;
  errorMessage = '';

  ngOnInit(): void {
    this.loadArticles();
  }

  loadArticles(): void {
    this.loading = true;
    this.errorMessage = '';

    this.articleService.getPublishedArticles().subscribe({
      next: (response) => {
        this.articles = response;
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: (error) => {
        console.error('Failed to load articles:', error);

        this.errorMessage =
          error.error?.message || 'Unable to load articles.';

        this.loading = false;
        this.cdr.markForCheck();
      }
    });
  }

  getAuthorInitial(username: string): string {
    return username?.charAt(0).toUpperCase() || 'A';
  }
}
