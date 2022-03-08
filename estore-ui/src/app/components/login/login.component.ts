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
        password: '',
    };

    constructor(private authService: AuthService, private router: Router) {}

    ngOnInit(): void {}

    login() {
        if (this.formData.username !== '' && this.formData.password !== '') {
            this.authService
                .login(this.formData.username, this.formData.password)
                .subscribe((user) => {
                    if (user) {
                        this.router.navigate(['/']);
                    }
                });
        }
    }
}
