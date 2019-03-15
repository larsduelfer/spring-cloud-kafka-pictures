import { Action, StateContext, State, createSelector } from "@ngxs/store";
import { CommentResource } from "../resources/comment/CommentResource";
import { CommentService } from "../services/comment.service";
import { tap } from "rxjs/operators";

export class FindCommentAction {
  static readonly type = 'FindComment';
  constructor(public imageId: string) { }
}

export class AddCommentAction {
  static readonly type = 'AddComment';
  constructor(public imageId: string, public comment: string) { }
}

export class ImageComment {
  imageId: string;
  comments: CommentResource[] = [];
}

export class CommentStateModel {
  comments: ImageComment[] = [];
  initialized: boolean;
}

@State<CommentStateModel>({
  name: 'comments',
})
export class CommentsState {

  constructor(private commentService: CommentService) { }


  @Action(FindCommentAction)
  findComments(ctx: StateContext<CommentStateModel>, action: FindCommentAction) {
    return this.commentService.getComments(action.imageId).pipe(tap(page => {
      let model = ctx.getState();
      if (!model.initialized) {
        model = new CommentStateModel();
        model.initialized = true;
        ctx.setState(model);
      }
      let existingImageComments = model.comments.filter(imageComment => imageComment.imageId == action.imageId);
      let existingImageComment = existingImageComments.length > 0 ? existingImageComments[0] : undefined;
      if (!existingImageComment) {
        let newComment = new ImageComment();
        newComment.imageId = action.imageId;
        newComment.comments = page.content;
        model.comments.push(newComment);
      } else {
        existingImageComment.comments = page.content;
      }
      ctx.patchState(model);
    }));
  }

  @Action(AddCommentAction)
  addComment(ctx: StateContext<CommentStateModel>, action: AddCommentAction) {
    return this.commentService.addComment(action.imageId, action.comment).pipe(tap(comment => {
      let model = ctx.getState();
      let existingImageComment = model.comments.filter(imageComment => imageComment.imageId == action.imageId)[0];
      existingImageComment.comments.unshift(comment);
    }));
  }

  static comments(imageId: string) {
    return createSelector([CommentsState], (state: CommentStateModel) => {
      let comments = state.comments.filter(c => c.imageId == imageId);
      if (comments.length > 0) {
        return comments[0];
      }
    });
  }
}
