import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {NetworkUtil} from "../utils/NetworkUtil";

@Injectable({providedIn: 'root'})
export class ReserveService {

    static RESERVE: string = "/reserve";
    static ALL_USERS: string = "/auth/users/all";
    static BY_NAME: string = "/reserve/reservesByName";
    static BY_TEMP: string = "/reserve/reservesByTemp";
    static RESERVES_ANIMAL_IN: string = "/reserve/reservesAnimalIn";
    static MOVE_ANIMAL: string = "/reserve/moveAnimal";
    static MOVE_WORKER: string = "/reserve/moveReserveWorker";
    static AVAILABLE_WORKERS: string = "/reserve/availableWorkers";
    static AVAILABLE_LOAD: string = "/reserve/availableLoad";

    private currentUser = JSON.parse(localStorage.getItem('currentUser'));
    private httpOptions = {
        headers: new HttpHeaders({
            'Authorization': 'Basic ' + localStorage.getItem('basic64Credentials'),
            'Content-Type': 'application/json'
        })
    };

    constructor(private httpClient: HttpClient) {
    }

    getAll(): Observable<any> {
        return this.httpClient.get(NetworkUtil.BACKEND_URL + ReserveService.RESERVE, this.httpOptions);
    }

    getAllUsers(): Observable<any> {
        return this.httpClient.get(NetworkUtil.BACKEND_URL + ReserveService.ALL_USERS, this.httpOptions);
    }

    getReservesByName(animalId: number): Observable<any> {
        return this.httpClient.get(NetworkUtil.BACKEND_URL + ReserveService.BY_NAME + "?animalId=" + animalId, this.httpOptions);
    }

    getReservesByTemp(animalId: number): Observable<any> {
        return this.httpClient.get(NetworkUtil.BACKEND_URL + ReserveService.BY_TEMP + "?animalId=" + animalId, this.httpOptions);
    }

    getReservesAnimalIn(animalId: number): Observable<any> {
        return this.httpClient.get(NetworkUtil.BACKEND_URL + ReserveService.RESERVES_ANIMAL_IN + "?animalId=" + animalId, this.httpOptions);
    }

    moveAnimal(animalId: number, reserveId: number): Observable<any> {
        return this.httpClient.get(NetworkUtil.BACKEND_URL + ReserveService.MOVE_ANIMAL + "?animalId=" + animalId + "&reserveId=" + reserveId, this.httpOptions);
    }

    moveWorker(userId: number, newId: number, oldId: number): Observable<any> {
        return this.httpClient.get(NetworkUtil.BACKEND_URL + ReserveService.MOVE_WORKER + "?userId=" + userId + "&oldId=" + oldId + "&newId=" + newId, this.httpOptions);
    }

    getAvailableWorkers(): Observable<any> {
        return this.httpClient.get(NetworkUtil.BACKEND_URL + ReserveService.AVAILABLE_WORKERS, this.httpOptions);
    }

    getAvailableLoad(reserveId: number): Observable<any> {
        return this.httpClient.get(NetworkUtil.BACKEND_URL + ReserveService.AVAILABLE_LOAD + "?id=" + reserveId, this.httpOptions);
    }
}