import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CommentPageResource } from '../resources/comment/CommentPageResource';
import { environment } from '../../environments/environment';
import { CommentResource } from '../resources/comment/CommentResource';

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  constructor(private httpClient: HttpClient) { }

  public getComments(imageId: string): Observable<CommentPageResource> {
    return this.httpClient.get<CommentPageResource>(environment.api + `/images/${imageId}/comments`);
  }

  public addComment(imageId: string, comment: string): Observable<CommentResource> {
    return this.httpClient.post<CommentResource>(environment.api + `/images/${imageId}/comments?text=${comment}`, {});
  }
}
