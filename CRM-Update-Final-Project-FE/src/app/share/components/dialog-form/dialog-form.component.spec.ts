import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogFormComponent } from './dialog-form.component';

describe('DialogComponent', () => {
  let component: DialogFormComponent;
  let fixture: ComponentFixture<DialogFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DialogFormComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DialogFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
