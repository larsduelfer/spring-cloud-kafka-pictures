export class UpdatePictureTitleAction {
  static readonly type = 'UpdatePictureTitle';
  constructor(public identifier: string, public title: string) { }
}