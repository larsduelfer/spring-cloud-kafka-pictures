import { ResourceLink } from "../ResourceLink";

export class UserResource {
  public identifier: string;
  public version: number;
  public displayName: string;
  public firstName: string;
  public lastName: string;
  public registered: boolean;

  public _links: UserLinks;
}

class UserLinks {
  public profilePictureUrl: ResourceLink;
}