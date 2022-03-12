import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, map, Observable, of } from 'rxjs';
import { User } from 'src/app/types/User';

@Injectable({
    providedIn: 'root',
})
export class AuthService {
    private currentUserSubject = new BehaviorSubject<User | undefined>(
        undefined
    );

    currentUser = this.currentUserSubject.asObservable();
    constructor(private httpClient: HttpClient) {}

    login(username: string, password: string): Observable<User> {
        console.log('login');
        return this.fakeUserAuth(username, password).pipe(
            map((user) => {
                this.currentUserSubject.next(user);
                localStorage.setItem('currentUser', JSON.stringify(user));
                return user;
            })
        );
    }

    logout(): void {
        this.currentUserSubject.next(undefined);
        localStorage.removeItem('currentUser');
    }

    private fakeUserAuth(username: string, password: string): Observable<User> {
        return of({
            username,
            password,
            admin: username === 'admin' ? true : false,
        });
    }
}
