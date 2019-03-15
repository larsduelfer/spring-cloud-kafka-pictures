import { ResourceLink } from "./../ResourceLink";
import { ExifData } from "../picture/ExifData";
import { UserReferenceResource } from "../user/UserReferenceResource";

export class PictureResource {
  public identifier: string;
  public title: string;
  public exifData: ExifData;

  public _embedded?: PictureEmbeddedResource;
  public _links?: PictureLinks;
}

class PictureEmbeddedResource {
  user: UserReferenceResource;
}

class PictureLinks {
  public self: ResourceLink;
  public imageSmall: ResourceLink;
  public imageMedium: ResourceLink;
  public imageRaw: ResourceLink;
  public updateTitle?: ResourceLink;
  public addComment?: ResourceLink;
}