
import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Tag {
  id: number;
  taggedUserId: number;
  taggedUsername: string;
  taggedByUserId: number;
  taggedByUsername: string;
  createdAt: string;
}

export interface TagRequest {
  username: string;
}

@Injectable({
  providedIn: 'root'
})
export class TagService {
  private readonly http = inject(HttpClient);

  private readonly apiUrl = 'http://localhost:8080/api';

  tagUserInArticle(
    articleId: number,
    request: TagRequest
  ): Observable<Tag> {
    return this.http.post<Tag>(
      `${this.apiUrl}/articles/${articleId}/tags`,
      request
    );
  }

  getArticleTags(articleId: number): Observable<Tag[]> {
    return this.http.get<Tag[]>(
      `${this.apiUrl}/articles/${articleId}/tags`
    );
  }

  tagUserInComment(
    commentId: number,
    request: TagRequest
  ): Observable<Tag> {
    return this.http.post<Tag>(
      `${this.apiUrl}/comments/${commentId}/tags`,
      request
    );
  }

  getCommentTags(commentId: number): Observable<Tag[]> {
    return this.http.get<Tag[]>(
      `${this.apiUrl}/comments/${commentId}/tags`
    );
  }
}
