import { Injectable } from '@angular/core';
import { OAuthService, AuthConfig, JwksValidationHandler } from 'angular-oauth2-oidc';
import { environment } from '../../environments/environment';
import { filter } from 'rxjs/operators';
import { Store } from '@ngxs/store';
import { LogoutAction, TokenReceivedAction } from '../states/authentication.state';
import { LoadUserAction, LogoutUserAction, CurrentUserState } from '../states/current-user.state';
import { Router } from '@angular/router';
import { UserResource } from '../resources/user/UserResource';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  private authConfig: AuthConfig = {

    // Url of the Identity Provider
    issuer: environment.idp,

    // URL of the SPA to redirect the user to after login
    redirectUri: window.location.origin + '/dashboard',

    // The SPA's id. The SPA is registerd with this id at the auth-server
    clientId: 'web-localhost',

    // set the scope for the permissions the client should request
    // The first three are defined by OIDC. The 4th is a usecase-specific one
    scope: 'openid profile email web',
  }

  constructor(private oauthService: OAuthService, private store: Store, private router: Router) {
    this.configureOpenIdApi();
  }

  private configureOpenIdApi() {
    this.oauthService.configure(this.authConfig);
    this.oauthService.setStorage(localStorage);
    this.oauthService.tokenValidationHandler = new JwksValidationHandler();
    this.oauthService.loadDiscoveryDocumentAndTryLogin();
    this.oauthService.setupAutomaticSilentRefresh();

    this.oauthService.events
      .pipe(filter(e => e.type === 'token_received'))
      .subscribe(e => {
        this.store.dispatch(new TokenReceivedAction(
          this.oauthService.getAccessToken())).subscribe(
            _ => this.store.dispatch(new LoadUserAction()).subscribe(_ => {
              this.store.select(state => state.currentUser).subscribe(currentUser => {
                let user = currentUser as UserResource;
                if (user.version !== undefined && !user.registered) {
                  this.router.navigate(['/register']);
                }
              });
            }));
      });
    this.oauthService.events
      .pipe(filter(e => e.type === 'logout'))
      .subscribe(e => {
        this.store.dispatch(new LogoutAction()).subscribe(
          _ => this.store.dispatch(new LogoutUserAction()));
      });
    this.oauthService.events
      .pipe(filter(e => e.type === 'session_terminated'))
      .subscribe(e => this.oauthService.logOut());
  }

  public getAccessToken(): string {
    if(this.oauthService.hasValidAccessToken) {
      return 'Bearer ' + this.oauthService.getAccessToken();
     } else {
       this.logout();
     }
  }
  public login(): void {
    this.oauthService.initImplicitFlow();
  }

  public logout(): void {
    this.oauthService.logOut();
  }
}
