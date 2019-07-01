import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UserResource } from '../../resources/user/UserResource';
import { Observable } from 'rxjs';
import { Store, Select } from '@ngxs/store';
import { UserSearchState } from '../../states/user-search.state';
import { UserReferenceResource } from 'src/app/resources/user/UserReferenceResource';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.sass']
})
export class UserProfileComponent implements OnInit {

  private userId: string;

  protected user: UserReferenceResource;

  constructor(private route: ActivatedRoute, private store: Store) { }

  ngOnInit() {
    this.userId = this.route.snapshot.paramMap.get("id");
    this.store.select<UserReferenceResource>(UserSearchState.user(this.userId)).subscribe(user => this.user = user);
  }

  profilePictureUrl(): string {
    if(this.user) {
      return `url(${this.user._links.profilePictureUrl.href})`;
    }
  }
}
