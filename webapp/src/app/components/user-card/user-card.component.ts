import { Component, OnInit, Input } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { UserResource } from '../../resources/user/UserResource';
import { UserReferenceResource } from '../../resources/user/UserReferenceResource';

@Component({
  selector: 'app-user-card',
  templateUrl: './user-card.component.html',
  styleUrls: ['./user-card.component.sass']
})
export class UserCardComponent implements OnInit {

  @Input()
  protected user: UserReferenceResource;

  ngOnInit() {
  }

  profilePictureUrl(): string {
    return `url(${this.user._links.profilePictureUrl.href})`;
  }

  userProfileUrl(): string {
    return `/users/${this.user.identifier}`;
  }
}
