import { ResourceLink } from "./../ResourceLink";
import { UserReferenceResource } from "../user/UserReferenceResource";

export class CommentResource {
  public identifier: string;
  public comment: string;

  public _embedded?: CommentEmbeddedResource;
}

class CommentEmbeddedResource {
  user: UserReferenceResource;
}