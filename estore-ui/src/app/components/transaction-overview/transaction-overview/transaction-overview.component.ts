import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/services/auth/auth.service';
import { CartService } from 'src/app/services/cart/cart.service';
import { Cart } from 'src/app/types/Cart';
import { CartProduct } from 'src/app/types/CartProduct';
import { Product } from 'src/app/types/Product';

@Component({
  selector: 'app-transaction-overview',
  templateUrl: './transaction-overview.component.html',
  styleUrls: ['./transaction-overview.component.css']
})
export class TransactionOverviewComponent implements OnInit {
  cart?: Cart;
  cartProducts: CartProduct[] | undefined;
  shipping = 0;
  paymentMethod = '';
  personalInfo = {
    firstName: '',
    lastName: '',
    billingAddress: '',
    state: '',
    zipCode: ''
  }

  card = {
    account: '',
    expiration: '',
    ccv: ''
  }
  formErrorMsg = '';

  constructor(private cartService: CartService, private authService: AuthService) {}

  ngOnInit() {
    if(this.authService.getCurrentUser() == null) console.log('no current user');
    else {
      this.cartService.getCart().subscribe((cart) => {
        console.log('got cart with ' + cart.numItems + ' items');
        this.cart = cart;
        this.cartProducts = cart.products;
      })
    }
  }

  paymentIsCard(): boolean {
    return this.paymentMethod == 'card';
  }

  hasProducts(): boolean {
    return this.cart != null;
  }

  private personalComplete(): boolean {
    if(this.personalInfo.firstName.length < 2) this.formErrorMsg = 'First name must be at least 2 characters, is only ' + this.personalInfo.firstName.length;
    else if(this.personalInfo.lastName.length < 2) this.formErrorMsg = 'Last name must be at least 2 characters';
    else if(this.personalInfo.billingAddress.length < 2) this.formErrorMsg = 'Billing address must be at least 2 characters';
    else if(this.personalInfo.state.length < 5) this.formErrorMsg = 'State must be at least 5 characters';
    else if(this.personalInfo.zipCode.toString().length != 5) this.formErrorMsg = 'Zip code must be 5 numbers';
    else {
      this.formErrorMsg = '';
      return true;
    }
    return false;
  }

  private cardComplete(): boolean {
    if(this.card.account.toString().length != 12) this.formErrorMsg = 'Invalid card number';
    else if(Date.parse(this.card.expiration) < Date.now()) this.formErrorMsg = 'Select a date in the future';
    else if(this.card.ccv.toString().length != 3) this.formErrorMsg = 'Invalid CCV';
    else {
      this.formErrorMsg = '';
      return true;
    }
    return false;
  }

  private paypalComplete(): boolean {
    this.formErrorMsg = 'PayPal integration not implemented.';
    return false;
  }

  formComplete(): boolean {
    this.formErrorMsg = '';
    return(this.paymentIsCard() ? this.personalComplete() && this.cardComplete() : this.paypalComplete());
  }

  submitTransaction(): boolean {
    if(this.paymentMethod == '') {
      this.formErrorMsg = 'Select a Payment Method';
      return false;
    }
    if(this.formComplete()) {
      console.log('Submitting Transaction');
      return true;
    } else {
      console.log('failed to submit')
      return false;
    }
  }
}
