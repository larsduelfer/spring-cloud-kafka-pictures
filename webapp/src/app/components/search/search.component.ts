import { Component, OnInit, ChangeDetectorRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbTabset, NgbTabChangeEvent } from '@ng-bootstrap/ng-bootstrap';
import { PicturePageResource } from '../../resources/picture/PicturePageResource';
import { UserPageResource } from '../../resources/user/UserPageResource';
import { PictureResource } from '../../resources/picture/PictureResource';
import { Store, Select } from '@ngxs/store';
import { SearchImageAction, PictureSearchState } from '../../states/picture-search.state';
import { Observable } from 'rxjs';
import { SearchUserAction, UserSearchState } from '../../states/user-search.state';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.sass']
})
export class SearchComponent implements OnInit {

  searchTerm = '';
  currentJustify = 'start';

  @ViewChild('ngbtabsetsearch')
  private tabs: NgbTabset;

  @Select(PictureSearchState)
  protected pictures$: Observable<PicturePageResource>;

  @Select(UserSearchState)
  protected users$: Observable<UserPageResource>;

  constructor(private route: ActivatedRoute, private router: Router, private store: Store, private changeDetector: ChangeDetectorRef) {
    //Register detector to get updates of parameter changes
    //this is required to 
    this.route.queryParams.subscribe((data) => {
      this.search()
    })
  }

  ngOnInit() { }

  ngAfterViewInit(): void {
    if (this.tabs) {
      let activeTab = '';
      if (this.isPhotoSearchActive()) {
        activeTab = "ngb-tabset-search-photos";
      } else {
        activeTab = "ngb-tabset-search-people";
      }
      this.tabs.select(activeTab);
      this.changeDetector.detectChanges();
    }
  }

  onTabChange($event: NgbTabChangeEvent) {
    let route: string = '/search';
    if ($event.nextId == "ngb-tabset-search-photos") {
      route += `/photos?term=${this.searchTerm}`;
    } else {
      route += `/people?term=${this.searchTerm}`;
    }
    this.router.navigateByUrl(route);
  }

  search() {
    this.searchTerm = this.route.snapshot.queryParamMap.get('term');
    if (this.isPeopleSearchActive()) {
      this.store.dispatch(new SearchUserAction(this.searchTerm));
    } else {
      this.store.dispatch(new SearchImageAction(this.searchTerm));
    }
  }

  private isPeopleSearchActive(): boolean {
    return this.router.url.indexOf("search/people") >= 0;
  }

  private isPhotoSearchActive(): boolean {
    return this.router.url.indexOf("search/photos") >= 0;
  }
}
