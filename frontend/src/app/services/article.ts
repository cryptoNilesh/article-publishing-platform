
import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Article {
  id: number;
  title: string;
  slug: string;
  summary: string;
  content: string;
  status: 'DRAFT' | 'PUBLISHED';
  publishedAt: string | null;
  authorId: number;
  authorUsername: string;
  createdAt: string;
}

@Injectable({
  providedIn: 'root'
})
export class ArticleService {

  private readonly http = inject(HttpClient);

  private readonly apiUrl = 'http://localhost:8080/api/articles';

  getPublishedArticles(): Observable<Article[]> {
    return this.http.get<Article[]>(this.apiUrl);
  }

  getArticleBySlug(slug: string): Observable<Article> {
    return this.http.get<Article>(
      `${this.apiUrl}/${encodeURIComponent(slug)}`
    );
  }

  createArticle(request: {
    title: string;
    slug: string;
    summary: string;
    content: string;
  }): Observable<Article> {
    return this.http.post<Article>(this.apiUrl, request);
  }

  publishArticle(articleId: number): Observable<Article> {
    return this.http.patch<Article>(
      `${this.apiUrl}/${articleId}/publish`,
      {}
    );
  }
}
