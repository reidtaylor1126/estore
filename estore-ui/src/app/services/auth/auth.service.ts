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
        return this.httpClient
            .get(`/api/users?q=${username}`, {
                responseType: 'text',
            })
            .pipe(
                map((user) => {
                    const userArr = user.split('*');
                    const userObj = {
                        username: userArr[0],
                        id: parseInt(userArr[1], 10),
                        admin: userArr[2] === 'true',
                    };
                    this.currentUserSubject.next(userObj);
                    localStorage.setItem(
                        'currentUser',
                        JSON.stringify(userObj)
                    );
                    return userObj;
                })
            );
    }

    register(username: string): Observable<User> {
        console.log('login');
        return this.httpClient.post<User>('/api/users', { username });
    }

    logout(): void {
        this.currentUserSubject.next(undefined);
        localStorage.removeItem('currentUser');
    }

    getCurrentUser(): User | undefined {
        return this.currentUserSubject.value;
    }

    getID(): number {
        const currentUser = this.getCurrentUser();
        if(currentUser?.id != undefined) return currentUser.id;
        else return -1;
    }

    getToken(): string | undefined {
        const currentUser = this.getCurrentUser();
        return currentUser
            ? `${currentUser.username}*${currentUser.id}`
            : undefined;
    }
}
