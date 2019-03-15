import { Component, ViewChild, ElementRef } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

import { AuthenticationService } from './../../services/authentication.service';
import { Select, Store } from '@ngxs/store';
import { AuthenticationModel } from '../../states/authentication.state';
import { UserResource } from '../../resources/user/UserResource';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FormControl, Validators, FormGroup } from '@angular/forms';

import { FileUploader, FileItem, FileLikeObject } from 'ng2-file-upload';
import { environment } from '../../../environments/environment';
import { MatSnackBar } from '@angular/material';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.sass']
})
export class AppComponent {

  title = 'webapp';

  errorMessage: string;
  
  allowedMimeType = ['image/jpg', 'image/jpeg'];
  
  maxFileSize = 10 * 1024 * 1024;

  public uploader:FileUploader;
  
  protected searchTerm: string;

  protected registered: boolean;

  protected loggedIn: boolean;

  protected titleFormControl = new FormControl('', [Validators.maxLength(256)]);

  protected uploadImageFormGroup = new FormGroup({
    title: this.titleFormControl
  });

  constructor(private router: Router, private route: ActivatedRoute, private authenticationService: AuthenticationService, private store: Store,
    private modalService: NgbModal, private snackBar: MatSnackBar) {
    store.select(state => state.currentUser).subscribe(currentUser => this.registered = (currentUser as UserResource).registered);
    store.select(state => state.authentication).subscribe(auth => this.loggedIn = (auth as AuthenticationModel).loggedIn);
    this.uploader = new FileUploader({
      url: environment.api + "/images/binaries", 
      isHTML5: true,
      allowedMimeType: this.allowedMimeType,
      maxFileSize: this.maxFileSize,
      method: 'POST',
      itemAlias: 'file'
    });

    this.uploader.onWhenAddingFileFailed = (item, filter, options) => this.onWhenAddingFileFailed(item, filter, options);
    this.uploader.onAfterAddingFile = (item) => { 
      this.errorMessage = null;
      item.withCredentials = false;
    };
    this.uploader.onErrorItem = (item, response, status) => {
      snackBar.open(response, null, { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' });
      this.resetUpload();
    };
    this.uploader.onSuccessItem = (item, response, status) => {
      snackBar.open(`${item._file.name} successfully uploaded`, null, { duration: 3000, horizontalPosition: 'center', verticalPosition: 'top' });
      this.resetUpload();
    }

    this.uploader.onBeforeUploadItem = (item: FileItem) => {
      this.uploader.authToken = authenticationService.getAccessToken();
      this.uploader.options.additionalParameter = {
        name: item.file.name,
        title: this.titleFormControl.value 
      };
    };
  }

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      if (params.term) {
        this.searchTerm = params.term;
      }
    });
  }

  onWhenAddingFileFailed(item: FileLikeObject, filter: any, options: any) {
    switch (filter.name) {
        case 'fileSize':
            this.errorMessage = `Maximum upload size exceeded (${item.size} of ${this.maxFileSize} allowed)`;
            break;
        case 'mimeType':
            const allowedTypes = this.allowedMimeType.join();
            this.errorMessage = `Type "${item.type} is not allowed. Allowed types: "${allowedTypes}"`;
            break;
        default:
            this.errorMessage = `Unknown error (filter is ${filter.name})`;
    }
  }

  open(upload) {
    this.modalService.open(upload, { ariaLabelledBy: 'modal-basic-title' }).result.then((result) => {
      this.uploadImage();
    });
  }

  search(searchTerm: string) {
    if(this.isPeopleSearchActive()) {
      this.router.navigate(['/search/people'], { queryParams: { term: searchTerm } })
    } else {
      this.router.navigate(['/search/photos'], { queryParams: { term: searchTerm } })
    }
  }

  public login(): void {
    this.authenticationService.login();
  }

  public logout(): void {
    this.authenticationService.logout();
  }

  uploadImage(){
    if (this.uploadImageFormGroup.valid) {
      if(this.uploader.queue.length > 0){
        let item = this.uploader.queue[0];
        this.uploader.uploadItem(item);
      }
    }
  }

  private isPeopleSearchActive(): boolean {
    return this.router.url.indexOf("search/people") >= 0;
  }

  private resetUpload() {
    this.uploader.clearQueue();
    this.errorMessage = null;
    this.titleFormControl.setValue(null);
  }
}
