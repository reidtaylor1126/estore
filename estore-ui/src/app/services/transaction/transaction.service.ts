import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { Transaction } from 'src/app/types/Transaction';
import { AuthService } from '../auth/auth.service';
import { CartService } from '../cart/cart.service';
import { InventoryService } from '../inventory/inventory.service';

@Injectable({
    providedIn: 'root'
})
export class TransactionService {
    constructor(
        private authService: AuthService,
        private httpClient: HttpClient,
        private inventoryService: InventoryService,
        private cartService: CartService
    )   {}

    placeholderCreateTransaction(transaction: Transaction): Observable<Transaction | null> {
        const confirmedTransaction: Transaction = {
            id: 1,
            user: 1,
            products: [],
            address: 'address',
            dateTime: 'date',
            paymentMethod: 'card',
            fulfilled: false
        }
        return of(confirmedTransaction);
    }

    createTransaction(transaction: Transaction): Observable<Transaction | null> {
        const token = this.authService.getToken();
        if(!token) return of(null);
        return this.httpClient.post<Transaction>('/api/transactions', transaction, {headers: {token: token}});
    }

    placeholderSetFulfilled(transaction: Transaction): Observable<Transaction | null> {
        return of(transaction);
    }

    getAllTransactions(): Observable<Transaction[] | null> {
        if(!this.authService.getCurrentUser()?.admin) return of(null);
        const token = this.authService.getToken();
        if(!token) return of(null);
        return this.httpClient.get<Transaction[]>('/api/transactions/all', {headers: {token: token}});
    }
}
