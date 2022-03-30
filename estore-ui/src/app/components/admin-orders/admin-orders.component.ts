import { transformAll } from '@angular/compiler/src/render3/r3_ast';
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
                  console.log(transactions);
              });
    }

    ngOnInit(): void {}

    transactions?: Transaction[];
    search: string = '';
    showUnfulfilled = true;
    showFulfilled = false;

    searchFilter(transaction: Transaction): boolean {
        if(this.search === '') return true;
        else return(this.search.includes(transaction.id ? transaction.id?.toString() : '-1'))
    }

    hasTransactions(): boolean {
        if(this.transactions) return this.transactions.length > 0;
        return false;
    }
}
