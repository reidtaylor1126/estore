import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
    selector: 'app-transaction-complete',
    templateUrl: './transaction-complete.component.html',
    styleUrls: ['./transaction-complete.component.css']
  })
export class TransactionCompleteComponent implements OnInit {

    redirectAfter = 3000; // 3 seconds

    constructor(private router: Router) { }

    ngOnInit(): void {
        setTimeout(() => {
            this.router.navigateByUrl('');
        }, this.redirectAfter)
    }
}
