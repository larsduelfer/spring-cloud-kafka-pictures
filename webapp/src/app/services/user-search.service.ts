import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { UserPageResource } from '../resources/user/UserPageResource';
import { Observable, of } from 'rxjs';
import { environment } from '../../environments/environment';
import { UserReferenceResource } from '../resources/user/UserReferenceResource';

@Injectable({
  providedIn: 'root'
})
export class UserSearchService {

  constructor(private httpClient: HttpClient) { }

  public search(term: string): Observable<UserPageResource> {
    return this.httpClient.get<UserPageResource>(environment.api + `/users/search?term=${term}`);
  }

  public findOne(identifier: string): Observable<UserReferenceResource> {
    return this.httpClient.get<UserReferenceResource>(environment.api + `/users/${identifier}`)
  }
}
