import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { PicturePageResource } from '../resources/picture/PicturePageResource';
import { environment } from '../../environments/environment';
import { Observable, of } from 'rxjs';
import { PictureResource } from '../resources/picture/PictureResource';

@Injectable({
  providedIn: 'root'
})
export class PictureSearchService {

  constructor(private httpClient: HttpClient) { }

  public search(term: string): Observable<PicturePageResource> {
    return this.httpClient.get<PicturePageResource>(environment.api + `/images/search?term=${term}`);
  }

  public searchUserPictures(userIdentifier: string): Observable<PicturePageResource> {
    console.log(userIdentifier);
    return this.httpClient.get<PicturePageResource>(environment.api + `/users/${userIdentifier}/images`);
  }

  public findOne(identifier: string): Observable<PictureResource> {
    return this.httpClient.get<PictureResource>(environment.api + `/images/${identifier}`)
  }

  public updateTitle(identifier: string, title: string): Observable<PictureResource> {
    let params = new HttpParams().set('title', title);
    return this.httpClient.patch<PictureResource>(environment.api + `/images/${identifier}`, params);
  }
}
