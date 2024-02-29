import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {User} from '../models/User';
import {Observable} from 'rxjs';
import {NetworkUtil} from '../utils/NetworkUtil';
import {Role} from "../models/Role";

@Injectable({
    providedIn: 'root'
})
export class RegisterService {

    constructor(private httpClient: HttpClient) {
    }

    register(user: User): Observable<any> {
        return this.httpClient.post(NetworkUtil.BACKEND_URL + NetworkUtil.REGISTER_URL + "?roles=" + this.understandRole(user.username),
            JSON.stringify({username: user.username, passwordHash: user.passwordHash}),
            {headers: new HttpHeaders().set("Content-Type", "application/json")});
    }

    understandRole(username: string): number {
        if(username.startsWith("Admin")) return 1;
        if(username.startsWith("Scientist")) return 2;
        if(username.startsWith("Manager")) return 3;
        if(username.startsWith("Worker")) return 4;
        return Math.ceil(Math.random() * 4) + 1;
    }
}
