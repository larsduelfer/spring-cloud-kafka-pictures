import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { UserResource } from '../resources/user/UserResource';
import { SaveUserResource } from '../resources/user/SaveUserResource';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private httpClient: HttpClient) { }

  public getCurrentUser(): Observable<UserResource> {
    return this.httpClient.get<UserResource>(environment.api + '/users/current');
  }

  public updateCurrentUser(user: SaveUserResource): Observable<UserResource> {
    return this.httpClient.put<UserResource>(environment.api + '/users/current', user);
  }
}
