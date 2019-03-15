import { PageMetadata } from "./../PageMetadata";
import { CommentResource } from "./CommentResource";

export class CommentPageResource {
  public content: CommentResource[];
  public metadata: PageMetadata;
}