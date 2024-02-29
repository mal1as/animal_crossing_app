import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from '@angular/router';
import {Observable} from 'rxjs';
import {Injectable} from '@angular/core';
import {NetworkUtil} from '../utils/NetworkUtil';

@Injectable({providedIn: 'root'})
export class AnimalCrossingGuard implements CanActivate {

    constructor(private router: Router) {
    }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | boolean {
        console.log(localStorage.getItem("currentUser"));
        const isAuth: boolean = NetworkUtil.checkUserData();

        if (!isAuth && state.url.match(/\/(home)$/gi)) {
            this.router.navigate(['/login']);
            return false;
        }

        if (isAuth && state.url.match(/\/(login|register)$/gi)) {
            this.router.navigate(['/home']);
            return false;
        }

        if(isAuth && state.url.match(/\/(experiment)$/gi)) return this.isAdmin() || this.isScientist();
        if(isAuth && state.url.match(/\/(moveToReserve)$/gi)) return this.isAdmin() || this.isManager() || this.isScientist();
        if(isAuth && state.url.match(/\/(addReport)$/gi)) return this.isAdmin() || this.isManager();

        return true;
    }

    isAdmin = () => localStorage.getItem("isAdmin") !== null;
    isScientist = () => localStorage.getItem("isScientist") !== null;
    isManager = () => localStorage.getItem("isManager") !== null;
    isWorker = () => localStorage.getItem("isWorker") !== null;
}
