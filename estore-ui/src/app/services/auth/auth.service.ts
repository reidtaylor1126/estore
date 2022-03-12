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

    login(username: string): Observable<User> {
        console.log('login');
        return this.fakeUserAuth(username).pipe(
            map((user) => {
                this.currentUserSubject.next(user);
                localStorage.setItem('currentUser', JSON.stringify(user));
                return user;
            })
        );
    }

    register(username: string): Observable<User> {
        return this.fakeUserAuth(username).pipe(
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

    private fakeUserAuth(username: string): Observable<User> {
        return of({
            username,
            admin: username === 'admin' ? true : false,
        });
    }
}
