import { Action, State, StateContext, Selector } from "@ngxs/store";
import { UserService } from "../services/user.service";
import { tap, catchError } from "rxjs/operators";
import { resource } from "selenium-webdriver/http";
import { HttpClient } from "@angular/common/http";
import { environment } from "../../environments/environment";
import { UserResource } from "../resources/user/UserResource";
import { SaveUserResource } from "../resources/user/SaveUserResource";

export class LoadUserAction {
  static readonly type = 'LoadUser';
}

export class UpdateUserAction {
  static readonly type = 'UpdateUser';
  constructor(public firstName: string, public lastName: string, public displayName: string) { }
}

export class LogoutUserAction {
  static readonly type = 'LogoutUser';
}

@State<UserResource>({
  name: 'currentUser',
})
export class CurrentUserState {

  constructor(private userService: UserService, private httpClient: HttpClient) { }

  @Action(LoadUserAction)
  loadUser(ctx: StateContext<UserResource>, action: LoadUserAction) {
    //Load current user details via REST call and update the state
    this.userService.getCurrentUser().subscribe(usr => ctx.setState(usr));
  }

  @Action(UpdateUserAction)
  updateUser(ctx: StateContext<UserResource>, action: UpdateUserAction) {

    //Create REST resource with data from action 
    let resource = new SaveUserResource();
    resource.version = ctx.getState().version;
    resource.firstName = action.firstName;
    resource.lastName = action.lastName;
    resource.displayName = action.displayName;

    //Save changes and update the state with returned data
    this.userService.updateCurrentUser(resource).subscribe(usr => ctx.setState(usr));
  }

  @Action(LogoutUserAction)
  logoutUser(ctx: StateContext<UserResource>, action: LogoutUserAction) {
    ctx.setState(undefined)
  }

  @Selector()
  static userName(state: UserResource) {
    return state.displayName;
  }
}
