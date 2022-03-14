import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { AuthService } from 'src/app/services/auth/auth.service';

@Component({
    selector: 'app-mobile-nav',
    templateUrl: './mobile-nav.component.html',
    styleUrls: ['./mobile-nav.component.css'],
})
export class MobileNavComponent implements OnInit {
    @Input() navOpen: boolean = false;
    @Output() toggleMenu = new EventEmitter<boolean>();
    isAdmin: boolean = false;
    constructor(private authService: AuthService) {}

    ngOnInit(): void {
        this.authService.currentUser.subscribe((user) => {
            if (user) {
                this.isAdmin = user.admin;
            } else {
                this.isAdmin = false;
            }
        });
    }

    toggleNav(value: boolean) {
        this.toggleMenu.emit(value);
    }
}
