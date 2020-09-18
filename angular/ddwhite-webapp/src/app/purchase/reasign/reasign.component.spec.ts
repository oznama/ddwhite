import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReasignComponent } from './reasign.component';

describe('ReasignComponent', () => {
  let component: ReasignComponent;
  let fixture: ComponentFixture<ReasignComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ReasignComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReasignComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
