import {User} from '../models/User';
import {Role} from "../models/Role";

export class NetworkUtil {

    static BACKEND_URL: string = "http://localhost:8082/backend/api/v1"
    static REGISTER_URL: string = '/auth/register';
    static USERS_URL: string = '';
    static LOGIN_URL: string = '/auth/login';
    static ROLES_URL: string = '/auth/roles';

    static clearStorage(): void {
        localStorage.clear();
    }

    static clearUserData(): void {
        localStorage.removeItem('currentUser');
        localStorage.removeItem('basic64Credentials');
    }

    static checkUserData(): boolean {
        return localStorage.getItem('currentUser') !== null && localStorage.getItem('basic64Credentials') !== null;
    }

    static authFailed(): void {
        this.clearUserData();
        window.location.href = '/login';
    }

    static authSuccess(userWithId: User, userWithOpenPass: User): void {
        localStorage.setItem('currentUser', JSON.stringify(userWithId));
        localStorage.setItem('basic64Credentials', btoa(userWithOpenPass.username + ':' + userWithOpenPass.passwordHash));
        NetworkUtil.saveRoles(userWithId.roles);
        window.location.href = '/login';
    }

    private static saveRoles(roles: Role[]): void {
        console.log(roles);
        roles.forEach(role => {
            if (role.name == 'ADMIN') localStorage.setItem("isAdmin", "true");
            if (role.name == 'SCIENTIST') localStorage.setItem("isScientist", "true");
            if (role.name == 'MANAGER') localStorage.setItem("isManager", "true");
            if (role.name == 'RESERVE_WORKER') localStorage.setItem("isWorker", "true");
        });
    }
}
