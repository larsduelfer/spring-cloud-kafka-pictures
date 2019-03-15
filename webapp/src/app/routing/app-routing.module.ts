import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { DashboardComponent } from './../components/dashboard/dashboard.component';
import { SearchComponent } from './../components/search/search.component';
import { PictureFullsizeComponent } from './../components/picture-fullsize/picture-fullsize.component';
import { UserProfileComponent } from './../components/user-profile/user-profile.component';
import { RegisterComponent } from './../components/register/register.component';
import { RedirectGuard } from './redirect-guard';
import { environment } from '../../environments/environment';

const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'search/photos', component: SearchComponent },
  { path: 'search/people', component: SearchComponent },
  { path: 'pictures/:id', component: PictureFullsizeComponent },
  { path: 'users/:id', component: UserProfileComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'sign-up', component: RedirectGuard, canActivate: [RedirectGuard], data: { externalUrl: environment.idp + "/Account/Register" } }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
  declarations: []
})
export class AppRoutingModule { }
