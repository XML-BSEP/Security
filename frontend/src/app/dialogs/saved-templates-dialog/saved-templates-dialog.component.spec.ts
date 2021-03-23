import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SavedTemplatesDialogComponent } from './saved-templates-dialog.component';

describe('SavedTemplatesDialogComponent', () => {
  let component: SavedTemplatesDialogComponent;
  let fixture: ComponentFixture<SavedTemplatesDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SavedTemplatesDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SavedTemplatesDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
