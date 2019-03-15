import { UserResource } from "./UserResource";
import { PageMetadata } from "../PageMetadata";
import { UserReferenceResource } from "./UserReferenceResource";

export class UserPageResource {
  public content: UserReferenceResource[];
  public metadata: PageMetadata;
}