import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth/auth.service';
import { TransactionService } from 'src/app/services/transaction/transaction.service';
import { Transaction } from 'src/app/types/Transaction';

@Component({
    selector: 'app-admin-orders',
    templateUrl: './admin-orders.component.html',
    styleUrls: ['./admin-orders.component.css']
})
export class AdminOrdersComponent implements OnInit {
    constructor(transactionService: TransactionService, authService: AuthService, router: Router) { 
        if(!authService.getCurrentUser()?.admin) router.navigateByUrl('');
        transactionService.getAllTransactions().subscribe((transactions) => {
                  if(transactions != null) this.transactions = transactions;
              });
    }

    ngOnInit(): void { }

    transactions?: Transaction[] = [
        {
            id: 1,
            user: 1,
            products: [
                {
                    id: 1,
                    name: 'testname1',
                    description: 'testdesc1',
                    price: 5.99,
                    quantity: 3
                }
            ],
            address: 'address',
            dateTime: '2022-03-25',
            paymentMethod: 'card',
            fulfilled: true
        }, {
          id: 2,
          user: 1,
          products: [
              {
                  id: 2,
                  name: 'testname2',
                  description: 'testdesc2',
                  price: 7.99,
                  quantity: 1
              }, 
              {
                  id: 3,
                  name: 'testname3',
                  description: 'testdesc3',
                  price: 3.99,
                  quantity: 2
              }
          ],
          address: 'address',
          dateTime: '2022-03-25',
          paymentMethod: 'card',
          fulfilled: false
      }
    ]

    hasTransactions(): boolean {
        if(this.transactions) return this.transactions.length > 0;
        return false;
    }
}
