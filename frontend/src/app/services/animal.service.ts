import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {NetworkUtil} from "../utils/NetworkUtil";
import {Animal} from "../models/Animal";

@Injectable({providedIn: 'root'})
export class AnimalService {

    static ANIMAL: string = "/animal";
    static WITH_CHARACTERISTICS: string = "/animal/withCharacteristics";
    static CLIMATES_BY_REPORT: string = "/animal/climatesByReport";

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
        return this.httpClient.get(NetworkUtil.BACKEND_URL + AnimalService.ANIMAL, this.httpOptions);
    }

    upsertAnimal(animal: Animal): Observable<any> {
        return this.httpClient.post(NetworkUtil.BACKEND_URL + AnimalService.ANIMAL, JSON.stringify(animal), this.httpOptions);
    }

    getAnimalsWithCharacteristics(): Observable<any> {
        return this.httpClient.get(NetworkUtil.BACKEND_URL + AnimalService.WITH_CHARACTERISTICS, this.httpOptions);
    }

    getClimatesByReport(animalId: number): Observable<any> {
        return this.httpClient.get(NetworkUtil.BACKEND_URL + AnimalService.CLIMATES_BY_REPORT + "?animalId=" + animalId, this.httpOptions);
    }
}