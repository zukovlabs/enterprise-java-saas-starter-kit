import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomerDialog } from './customer-dialog';

describe('CustomerDialog', () => {
  let component: CustomerDialog;
  let fixture: ComponentFixture<CustomerDialog>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CustomerDialog]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CustomerDialog);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
