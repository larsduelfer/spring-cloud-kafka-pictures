import { Component, OnInit, ViewChild } from '@angular/core';

import { NgbCarouselConfig } from '@ng-bootstrap/ng-bootstrap';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';
import { Select, Store } from '@ngxs/store';
import { CurrentUserState } from '../../states/current-user.state';
import { AuthenticationModel } from '../../states/authentication.state';
import { trigger, state, style, transition, animate } from '@angular/animations';
import { UserResource } from '../../resources/user/UserResource';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.sass'],
  providers: [NgbCarouselConfig],
  animations: [
    // the fade-in/fade-out animation.
    trigger('fadeAnimation', [

      // the "in" style determines the "resting" state of the element when it is visible.
      state('in', style({ opacity: 1 })),

      // fade in when created. this could also be written as transition('void => *')
      transition(':enter', [
        style({ opacity: 0 }),
        animate(1500)
      ]),

      // fade out when destroyed. this could also be written as transition('void => *')
      transition(':leave',
        animate(600, style({ opacity: 0 })))
    ]),
    trigger('fadeTextAnimation', [

      state('in', style({ opacity: 1 })),

      transition(':enter', [
        style({ opacity: 0 }),
        animate(1500)
      ]),

      transition(':leave',
        animate(100, style({ opacity: 0 })))
    ]),
  ]
})
export class DashboardComponent implements OnInit {

  showNavigationArrows = false;
  showNavigationIndicators = true;
  images = [];

  protected idp: string;

  loggedIn: boolean;

  registered: boolean;

  @Select(CurrentUserState.userName) user$: Observable<string>;

  constructor(config: NgbCarouselConfig, private store: Store) {
    //Configure carousel
    config.interval = 10000;
    config.keyboard = false;
    config.wrap = true;
    config.pauseOnHover = false;

    //Map idp address for sign-up button
    this.idp = environment.idp;
    store.select(state => state.currentUser).subscribe(currentUser => this.registered = (currentUser as UserResource).registered);
    store.select(state => state.authentication).subscribe(auth => this.loggedIn = (auth as AuthenticationModel).loggedIn);
  }

  ngOnInit() {
    this.images = [
      '/assets/images/background1.jpg',
      '/assets/images/background2.jpg',
      '/assets/images/background3.jpg',
      '/assets/images/background4.jpg',
      '/assets/images/background5.jpg',
    ];
  }
}
