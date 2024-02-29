import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {User} from '../models/User';
import {Observable} from 'rxjs';
import {NetworkUtil} from '../utils/NetworkUtil';
import {Role} from "../models/Role";

@Injectable({providedIn: 'root'})
export class LoginService {

    constructor(private httpClient: HttpClient) {
    }

    logIn(user: User): Observable<any> {
        const base64Credentials = btoa(user.username + ':' + user.passwordHash);
        const httpOptions = {
            headers: new HttpHeaders({
                'Authorization': 'Basic ' + base64Credentials
            })
        };
        return this.httpClient.post(NetworkUtil.BACKEND_URL + NetworkUtil.LOGIN_URL, {username: user.username}, httpOptions);
    }

    logOut(): void {
        localStorage.removeItem('currentUser');
        localStorage.removeItem('basic64Credentials')
        localStorage.removeItem('isAdmin');
        localStorage.removeItem('isScientist');
        localStorage.removeItem('isManager');
        localStorage.removeItem('isWorker');
        // this.httpClient.get(this.URL + 'logout').subscribe();
    }
}

