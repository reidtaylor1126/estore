import { Component, OnInit } from '@angular/core';
import { AuthService } from './services/auth/auth.service';
import { User } from './types/User';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit {
    title = 'estore-ui';
    constructor(private authService: AuthService) {}
    ngOnInit(): void {
        const currentUser = localStorage.getItem('currentUser');
        if (currentUser) {
            const user: User = JSON.parse(currentUser);
            this.authService.login(user.username).subscribe();
        }
    }
}
