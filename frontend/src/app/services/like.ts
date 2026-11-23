
import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface LikeResponse {
  articleId: number;
  likeCount: number;
  likedByCurrentUser: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class LikeService {
  private readonly http = inject(HttpClient);

  private readonly apiUrl = 'http://localhost:8080/api/articles';

  likeArticle(articleId: number): Observable<string> {
    return this.http.post(
      `${this.apiUrl}/${articleId}/likes`,
      {},
      { responseType: 'text' }
    );
  }

  unlikeArticle(articleId: number): Observable<string> {
    return this.http.delete(
      `${this.apiUrl}/${articleId}/likes`,
      { responseType: 'text' }
    );
  }

  getLikeCount(articleId: number): Observable<number> {
    return this.http.get<number>(
      `${this.apiUrl}/${articleId}/likes/count`
    );
  }

  getLikeStatus(articleId: number): Observable<LikeResponse> {
    return this.http.get<LikeResponse>(
      `${this.apiUrl}/${articleId}/likes/status`
    );
  }
}
