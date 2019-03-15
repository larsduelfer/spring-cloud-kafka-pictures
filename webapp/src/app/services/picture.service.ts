import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ResponseContentType } from '@angular/http';
import { saveAs } from 'file-saver';

@Injectable({
  providedIn: 'root'
})
export class PictureService {

  constructor(private httpClient: HttpClient) { }

  public downloadImage(url: string, fileName: string) {
    this.httpClient.get(url,{responseType: 'blob'}).subscribe(res => saveAs(res, fileName));
  }
}
