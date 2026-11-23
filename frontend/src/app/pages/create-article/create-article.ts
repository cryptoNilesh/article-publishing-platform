
import {
  Component,
  inject,
  ChangeDetectorRef
} from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { Article, ArticleService } from '../../services/article';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-create-article',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './create-article.html',
  styleUrl: './create-article.css'
})
export class CreateArticle {
  private readonly articleService = inject(ArticleService);
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);
  private readonly cdr = inject(ChangeDetectorRef);

  title = '';
  slug = '';
  summary = '';
  content = '';

  loading = false;
  errorMessage = '';
  successMessage = '';

  constructor() {
    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/login']);
    }
  }

  generateSlug(): void {
    this.slug = this.title
      .toLowerCase()
      .trim()
      .replace(/[^a-z0-9\s-]/g, '')
      .replace(/\s+/g, '-')
      .replace(/-+/g, '-');

    this.cdr.markForCheck();
  }

  saveDraft(): void {
    this.submitArticle(false);
  }

  publishArticle(): void {
    this.submitArticle(true);
  }

  private submitArticle(publish: boolean): void {
    this.errorMessage = '';
    this.successMessage = '';

    if (!this.title.trim() || !this.slug.trim() || !this.content.trim()) {
      this.errorMessage = 'Title, slug and content are required.';
      this.cdr.markForCheck();
      return;
    }

    if (this.title.trim().length > 200) {
      this.errorMessage = 'Title cannot exceed 200 characters.';
      this.cdr.markForCheck();
      return;
    }

    if (this.slug.trim().length > 250) {
      this.errorMessage = 'Slug cannot exceed 250 characters.';
      this.cdr.markForCheck();
      return;
    }

    if (this.summary.length > 500) {
      this.errorMessage = 'Summary cannot exceed 500 characters.';
      this.cdr.markForCheck();
      return;
    }

    this.loading = true;
    this.cdr.markForCheck();

    this.articleService.createArticle({
      title: this.title.trim(),
      slug: this.slug.trim(),
      summary: this.summary.trim(),
      content: this.content.trim()
    }).subscribe({
      next: (article: Article) => {
        if (!publish) {
          this.loading = false;
          this.successMessage = 'Article saved as draft successfully.';
          this.cdr.markForCheck();
          this.router.navigate(['/']);
          return;
        }

        this.articleService.publishArticle(article.id).subscribe({
          next: () => {
            this.loading = false;
            this.cdr.markForCheck();
            this.router.navigate(['/articles', article.slug]);
          },
          error: (error) => {
            this.loading = false;
            this.errorMessage =
              error.error?.message ||
              'Article was saved as draft, but publishing failed.';

            this.cdr.markForCheck();
          }
        });
      },
      error: (error) => {
        this.loading = false;
        this.errorMessage =
          error.error?.message ||
          'Unable to create article. Please try again.';

        this.cdr.markForCheck();
      }
    });
  }
}
