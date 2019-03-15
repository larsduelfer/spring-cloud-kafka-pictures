import { Action, StateContext, State } from "@ngxs/store";
import { PicturePageResource } from "../resources/picture/PicturePageResource";
import { PictureSearchService } from "../services/picture-search.service";
import { UpdatePictureTitleAction } from "./update-picture-title-action";

export class SearchUserPictureAction {
  static readonly type = 'SearchUserPictures';
  constructor(public userIdentifier: string) { }
}

@State<PicturePageResource>({
  name: 'userPictures',
})
export class UserPictureSearchState {

  constructor(private pictureSearchService: PictureSearchService) { }


  @Action(SearchUserPictureAction)
  searchImage(ctx: StateContext<PicturePageResource>, action: SearchUserPictureAction) {

    //Search and update the state with returned data
    this.pictureSearchService.searchUserPictures(action.userIdentifier).subscribe(page => ctx.setState(page));
  }

  @Action(UpdatePictureTitleAction)
  updateTitle(ctx: StateContext<PicturePageResource>, action: UpdatePictureTitleAction) {
    let page = ctx.getState();
    let picture = page.content.find(p => p.identifier == action.identifier);
    if (picture) {
      picture.title = action.title;
    }
  }
}
