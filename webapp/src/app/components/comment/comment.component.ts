import { Component, OnInit, Input } from '@angular/core';
import { CommentResource } from '../../resources/comment/CommentResource';

@Component({
  selector: 'app-comment',
  templateUrl: './comment.component.html',
  styleUrls: ['./comment.component.sass']
})
export class CommentComponent implements OnInit {

  @Input()
  protected comment: CommentResource;

  constructor() { }

  ngOnInit() {
  }

  username(): string {
    return this.comment._embedded.user.displayName;
  }

  content(): string {
    return this.comment.comment;
  }

}
