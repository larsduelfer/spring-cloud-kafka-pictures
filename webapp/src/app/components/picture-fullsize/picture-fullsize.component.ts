import { Component, OnInit, Input, ViewChild, ElementRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { PictureResource } from '../../resources/picture/PictureResource';
import { PictureSearchService } from '../../services/picture-search.service';
import { Store, Select } from '@ngxs/store';
import { FindPictureAction, PictureFullsizeState } from '../../states/picture-fullsize.state';
import { Observable, of } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Validators, FormControl, FormGroup } from '@angular/forms';
import { UpdatePictureTitleAction } from '../../states/update-picture-title-action';
import { ImageComment, FindCommentAction, CommentsState, AddCommentAction } from '../../states/comment.state';
import { PictureService } from '../../services/picture.service';

@Component({
  selector: 'app-picture-fullsize',
  templateUrl: './picture-fullsize.component.html',
  styleUrls: ['./picture-fullsize.component.sass']
})
export class PictureFullsizeComponent implements OnInit {

  @Select(PictureFullsizeState)
  protected picture$: Observable<PictureResource>;

  @Select(PictureFullsizeState.pictureMediumUrl)
  protected pictureMediumUrl$: Observable<String>;

  @Select(PictureFullsizeState.profilePictureUrl)
  protected profilePictureUrl$: Observable<String>;

  @Select(PictureFullsizeState.user)
  protected user$: Observable<String>;

  protected comments$: Observable<ImageComment>;

  protected commentsCollapsed = true;

  @ViewChild("newComment", { read: ElementRef, static: false })
  private newComment: ElementRef;

  protected titleFormControl = new FormControl('', [Validators.maxLength(256)]);

  protected updateTitleFormGroup = new FormGroup({
    title: this.titleFormControl
  });

  constructor(private route: ActivatedRoute, private store: Store, private modalService: NgbModal, private pictureService: PictureService) { }

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    this.store.dispatch(new FindPictureAction(id));
  }

  open(content) {
    this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' }).result.then((result) => {
      this.updateTitle()
    });
  }

  updateTitle() {
    if (this.updateTitleFormGroup.valid) {
      const id = this.route.snapshot.paramMap.get('id');
      this.store.dispatch(new UpdatePictureTitleAction(id, this.titleFormControl.value));
    }
  }

  toggleComments() {
    this.commentsCollapsed = !this.commentsCollapsed;
    if (!this.commentsCollapsed) {
      this.loadComments();
    }
  }

  loadComments() {
    const id = this.route.snapshot.paramMap.get('id');
    this.store.dispatch(new FindCommentAction(id)).subscribe(_ =>
      this.store.select<ImageComment>(CommentsState.comments(id)).subscribe(c => {
        this.comments$ = of(c);
      }));
  }

  addComment() {
    let input = this.newComment.nativeElement.value;
    const id = this.route.snapshot.paramMap.get('id');
    if (input && input.trim()) {
      this.store.dispatch(new AddCommentAction(id, input)).subscribe(_ => {
        this.loadComments();
        this.newComment.nativeElement.value = "";
      });
    }
  }

  commentLinkDescription() {
    if (this.commentsCollapsed) {
      return "Show comments";
    }
    return "Hide comments";
  }

  commentButtonDescription() {
    if (this.commentsCollapsed) {
      return "COMMENT";
    }
    return "HIDE COMMENTS";
  }

  downloadPicture() {
    let fileName = 'picture.jpg';
    let picture = this.store.snapshot().pictureFullsize as PictureResource;
    if (picture.title) {
      fileName = picture.title.replace(' ', '_').toLowerCase() + '.jpg'
    }
    this.pictureService.downloadImage(picture._links.imageRaw.href, fileName);
  }
}
