import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth/auth.service';
import { User } from 'src/app/types/User';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {
    formData: User = {
        username: '',
        admin: false,
    };
    redirect: string = '/';

    constructor(private authService: AuthService, private router: Router) {}

    ngOnInit(): void {
        const url = this.router
            .parseUrl(this.router.url)
            .queryParamMap.get('redirect');
        if (url) {
            this.redirect = url;
        }

        this.authService.currentUser.subscribe((user) => {
            if (user) {
                this.router.navigate([this.redirect]);
            }
        });
    }

    login() {
        if (this.formData.username) {
            this.authService.login(this.formData.username).subscribe((user) => {
                if (user) {
                    this.router.navigate([this.redirect]);
                }
            });
        }
    }
}
