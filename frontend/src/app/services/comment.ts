
import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Comment {
  id: number;
  content: string;
  articleId: number;
  authorId: number;
  authorUsername: string;
  parentCommentId: number | null;
  createdAt: string;
}

export interface CommentRequest {
  content: string;
}

@Injectable({
  providedIn: 'root'
})
export class CommentService {
  private readonly http = inject(HttpClient);

  private readonly apiUrl = 'http://localhost:8080/api/articles';

  getComments(articleId: number): Observable<Comment[]> {
    return this.http.get<Comment[]>(
      `${this.apiUrl}/${articleId}/comments`
    );
  }

  addComment(
    articleId: number,
    request: CommentRequest
  ): Observable<Comment> {
    return this.http.post<Comment>(
      `${this.apiUrl}/${articleId}/comments`,
      request
    );
  }

  getReplies(
    articleId: number,
    parentCommentId: number
  ): Observable<Comment[]> {
    return this.http.get<Comment[]>(
      `${this.apiUrl}/${articleId}/comments/${parentCommentId}/replies`
    );
  }

  addReply(
    articleId: number,
    parentCommentId: number,
    request: CommentRequest
  ): Observable<Comment> {
    return this.http.post<Comment>(
      `${this.apiUrl}/${articleId}/comments/${parentCommentId}/replies`,
      request
    );
  }
}
