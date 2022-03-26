import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth/auth.service';
import { User } from 'src/app/types/User';

@Component({
    selector: 'app-register',
    templateUrl: './register.component.html',
    styleUrls: ['./register.component.css'],
})
export class RegisterComponent implements OnInit {
    formData: User = {
        username: '',
        admin: false,
    };

    redirect = '/';

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

    /**
     *
     */
    register() {
        if (this.formData.username) {
            this.authService
                .register(this.formData.username)
                .subscribe((user) => {
                    this.authService.login(user.username).subscribe(() => {
                        this.router.navigate([this.redirect]);
                    });
                });
        }
    }
}
