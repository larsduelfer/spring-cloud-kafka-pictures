import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PictureFullsizeComponent } from './picture-fullsize.component';

describe('PictureFullsizeComponent', () => {
  let component: PictureFullsizeComponent;
  let fixture: ComponentFixture<PictureFullsizeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PictureFullsizeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PictureFullsizeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
