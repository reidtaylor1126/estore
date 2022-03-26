import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth/auth.service';

@Component({
    selector: 'app-account-nav',
    templateUrl: './account-nav.component.html',
    styleUrls: ['./account-nav.component.css'],
})
export class AccountNavComponent implements OnInit {
    @Input() navOpen: boolean = false;
    @Output() toggleMenu = new EventEmitter<boolean>();
    username?: string;
    constructor(private authService: AuthService, private router: Router) {}

    ngOnInit(): void {
        this.authService.currentUser.subscribe((value) => {
            if (value) {
                this.username = value.username;
            } else {
                this.username = undefined;
            }
        });
    }

    toggleNav(value: boolean) {
        this.toggleMenu.emit(value);
    }

    logout() {
        this.authService.logout();
        this.toggleNav(false);
    }

    goToLogin() {
        this.toggleNav(false);
        this.router.navigate(['/login'], {
            queryParams: { redirect: this.router.url },
        });
    }

    goToRegister() {
        this.toggleNav(false);
        this.router.navigate(['/register'], {
            queryParams: { redirect: this.router.url },
        });
    }
}
