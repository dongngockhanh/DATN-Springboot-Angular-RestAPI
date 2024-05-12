import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ControlUserComponent } from './control-user.component';

describe('ControlUserComponent', () => {
  let component: ControlUserComponent;
  let fixture: ComponentFixture<ControlUserComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ControlUserComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ControlUserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
