import {Component, OnInit} from '@angular/core';
import {User} from "../../models/User";
import {LoginService} from "../../services/login.service";

@Component({
    selector: 'app-home',
    templateUrl: './home.component.html'
})

export class HomeComponent {

    isVisibleSideBar: boolean = false;
    currentUser: User = JSON.parse(localStorage.getItem('currentUser'));

    constructor(private loginService: LoginService) {
    }

    onLogOutClick(): void {
        this.loginService.logOut();
        window.location.href = '/login';
    }

    onExperimentClick(): void {
        window.location.href = "/experiment";
    }

    onAnimalClick(): void {
        window.location.href = "/animal";
    }

    onMoveToReserveClick(): void {
        window.location.href = "/moveToReserve";
    }

    onAddReportClick(): void {
        window.location.href = "/addReport";
    }

    onReportHistoryClick(): void {
        window.location.href = "/reportHistory";
    }

    onExperimentHistoryClick(): void {
        window.location.href = "/experimentHistory";
    }

    isAdmin = () => localStorage.getItem("isAdmin") !== null;
    isScientist = () => localStorage.getItem("isScientist") !== null;
    isManager = () => localStorage.getItem("isManager") !== null;
    isWorker = () => localStorage.getItem("isWorker") !== null;
}
