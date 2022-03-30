import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TransactionProductComponent } from './transaction-product.component';

describe('TransactionProductComponent', () => {
  let component: TransactionProductComponent;
  let fixture: ComponentFixture<TransactionProductComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TransactionProductComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TransactionProductComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
