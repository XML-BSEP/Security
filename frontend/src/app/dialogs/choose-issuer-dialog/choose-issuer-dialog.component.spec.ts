import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChooseIssuerDialogComponent } from './choose-issuer-dialog.component';

describe('ChooseIssuerDialogComponent', () => {
  let component: ChooseIssuerDialogComponent;
  let fixture: ComponentFixture<ChooseIssuerDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ChooseIssuerDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ChooseIssuerDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
