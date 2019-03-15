import { ResourceLink } from "../ResourceLink";

export class UserReferenceResource {
  public identifier: string;
  public displayName: string;

  public _links: UserReferenceLinks;
}

class UserReferenceLinks {
  public profilePictureUrl: ResourceLink;
}