import { Component, OnInit, Input } from '@angular/core';
import { PicturePageResource } from '../../resources/picture/PicturePageResource';
import { PictureResource } from '../../resources/picture/PictureResource';
import { Store, Select } from '@ngxs/store';
import { SearchUserPictureAction, UserPictureSearchState } from '../../states/user-picture-search.state';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-user-pictures',
  templateUrl: './user-pictures.component.html',
  styleUrls: ['./user-pictures.component.sass']
})
export class UserPicturesComponent implements OnInit {

  @Input()
  protected userId: string;

  @Select(UserPictureSearchState)
  protected pictures$: Observable<PicturePageResource>;

  constructor(private store: Store) { }

  ngOnInit() {
    this.store.dispatch(new SearchUserPictureAction(this.userId));
  }

}
