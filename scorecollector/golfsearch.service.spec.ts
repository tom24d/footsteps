import { TestBed } from '@angular/core/testing';

import { GolfsearchService } from './golfsearch.service';

describe('GolfsearchService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: GolfsearchService = TestBed.get(GolfsearchService);
    expect(service).toBeTruthy();
  });
});
