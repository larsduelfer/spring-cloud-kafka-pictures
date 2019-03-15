import { Action, State, StateContext } from "@ngxs/store";
import { AuthenticationService } from "../services/authentication.service";

export class TokenReceivedAction {
  static readonly type = 'TokenReceived';
  constructor(public token: string) { }
}

export class LogoutAction {
  static readonly type = 'Logout';
}

export interface AuthenticationModel {
  token: string;
  loggedIn: boolean
}

@State<AuthenticationModel>({
  name: 'authentication'
})
export class AuthenticationState {

  @Action(TokenReceivedAction)
  tokenReceived(ctx: StateContext<AuthenticationModel>, action: TokenReceivedAction) {
    ctx.setState({
      token: action.token,
      loggedIn: true
    });
  }

  @Action(LogoutAction)
  logout(ctx: StateContext<AuthenticationModel>, action: LogoutAction) {
    ctx.setState({
      token: undefined,
      loggedIn: false
    });
  }
}