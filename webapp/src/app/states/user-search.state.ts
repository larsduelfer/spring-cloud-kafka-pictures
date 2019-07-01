import { Action, StateContext, State, createSelector } from "@ngxs/store";
import { PicturePageResource } from "../resources/picture/PicturePageResource";
import { PictureSearchService } from "../services/picture-search.service";
import { UserPageResource } from "../resources/user/UserPageResource";
import { UserSearchService } from "../services/user-search.service";
import { UserReferenceResource } from "../resources/user/UserReferenceResource";

export class SearchUserAction {
  static readonly type = 'SearchUser';
  constructor(public name: string) { }
}

@State<UserPageResource>({
  name: 'users',
})
export class UserSearchState {

  constructor(private userSearchService: UserSearchService) { }


  @Action(SearchUserAction)
  searchImage(ctx: StateContext<UserPageResource>, action: SearchUserAction) {

    //Search and update the state with returned data
    this.userSearchService.search(action.name).subscribe(page => ctx.setState(page));
  }

  static user(identifier: string) {
    return createSelector([UserSearchState], (state: UserPageResource) => {
      let users = state.content.filter(s => s.identifier == identifier);
      if(users.length > 0) {
        return users[0];
      }
    });
  }
}
