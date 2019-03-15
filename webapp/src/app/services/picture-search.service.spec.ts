import { TestBed } from '@angular/core/testing';

import { PictureSearchService } from './picture-search.service';

describe('PictureSearchService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: PictureSearchService = TestBed.get(PictureSearchService);
    expect(service).toBeTruthy();
  });
});
