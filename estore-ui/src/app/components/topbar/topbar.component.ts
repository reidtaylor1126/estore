import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthService } from 'src/app/services/auth/auth.service';
import { CartService } from 'src/app/services/cart/cart.service';
import { User } from 'src/app/types/User';

@Component({
    selector: 'app-topbar',
    templateUrl: './topbar.component.html',
    styleUrls: ['./topbar.component.css'],
})
export class TopbarComponent implements OnInit {
    navOpen = false;
    acctOpen = false;
    isAdmin: boolean = false;
    isLoggedIn: boolean = false;
    numItems: number = 0;
    constructor(
        private authService: AuthService,
        private cartService: CartService
    ) {}

    ngOnInit(): void {
        this.authService.currentUser.subscribe((user) => {
            user && user.admin ? (this.isAdmin = true) : (this.isAdmin = false);
            this.isLoggedIn = user ? true : false;
        });
        this.cartService.getNumItemsObservable().subscribe((num) => {
            this.numItems = num;
        });
    }

    toggleNav(value?: boolean): void {
        if (typeof value !== 'undefined') {
            this.navOpen = value;
            return;
        }
        this.navOpen = !this.navOpen;
        if (this.acctOpen) {
            this.toggleAcct(false);
        }
    }

    toggleAcct(value?: boolean): void {
        if (typeof value !== 'undefined') {
            this.acctOpen = value;
            return;
        }
        this.acctOpen = !this.acctOpen;
        if (this.navOpen) {
            this.toggleNav(false);
        }
    }
}
