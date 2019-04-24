import { TestBed } from '@angular/core/testing';

import { BackfireService } from './backfire.service';

describe('BackfireService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: BackfireService = TestBed.get(BackfireService);
    expect(service).toBeTruthy();
  });
});
