import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Customers } from './customers';

describe('Customers', () => {
  let component: Customers;
  let fixture: ComponentFixture<Customers>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Customers]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Customers);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
