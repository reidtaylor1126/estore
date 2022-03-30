import { Component, Input, OnInit } from '@angular/core';
import { TransactionService } from 'src/app/services/transaction/transaction.service';
import { Product } from 'src/app/types/Product';
import { Transaction } from 'src/app/types/Transaction';

@Component({
    selector: 'app-admin-order',
    templateUrl: './admin-order.component.html',
    styleUrls: ['./admin-order.component.css']
})
export class AdminOrderComponent implements OnInit {

    constructor(private transactionService: TransactionService) { }

    @Input() transaction?: Transaction;

    ngOnInit(): void { }

    setFulfilled(fulfilled: boolean) {
        if(this.transaction) {
            this.transaction.fulfilled = fulfilled;
            this.transactionService.setFulfilled(this.transaction).subscribe((result) => {
                if(result != null) {
                    this.transaction = result;
                }
            })
        }
    }

    hasProducts(): boolean {
        if(this.transaction) return this.transaction.products.length > 0;
        return false;
    }
}
