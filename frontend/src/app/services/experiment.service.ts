import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {NetworkUtil} from "../utils/NetworkUtil";
import {Animal} from "../models/Animal";
import {Characteristic} from "../models/Characteristic";
import {ExperimentHistoryParams} from "../models/ExperimentHistoryParams";

@Injectable({providedIn: 'root'})
export class ExperimentService {

    static START: string = "/experiment/start";
    static SELECT_ANIMALS: string = "/experiment/selectAnimals";
    static AVAILABLE_CHARS: string = "/experiment/availableCharacteristics";
    static CONFLICTED_CHARS: string = "/experiment/conflictedCharacteristics";
    static CHOOSE_CHARS: string = "/experiment/chooseCharacteristics";
    static EXECUTE: string = "/experiment/execute";
    static HISTORY: string = "/experiment/history";

    private currentUser = JSON.parse(localStorage.getItem('currentUser'));
    private httpOptions = {
        headers: new HttpHeaders({
            'Authorization': 'Basic ' + localStorage.getItem('basic64Credentials'),
            'Content-Type': 'application/json'
        })
    };

    constructor(private httpClient: HttpClient) {
    }

    startExperiment(): Observable<any> {
        return this.httpClient.get(NetworkUtil.BACKEND_URL + ExperimentService.START, this.httpOptions);
    }

    selectAnimals(animals: Animal[], experimentId: number): Observable<any> {
        const urlParams = "?experimentId=" + experimentId + "&animalIds=" + animals.map(a => a.id).join(",");
        return this.httpClient.get(NetworkUtil.BACKEND_URL + ExperimentService.SELECT_ANIMALS + urlParams, this.httpOptions);
    }

    getAvailableCharacteristics(experimentId: number): Observable<any> {
        return this.httpClient.get(NetworkUtil.BACKEND_URL + ExperimentService.AVAILABLE_CHARS + "?experimentId=" + experimentId, this.httpOptions);
    }

    getConflictedCharacteristics(chars: Characteristic[]): Observable<any> {
        const urlParams: string = "?characteristicIds=" + chars.map(c => c.characteristicId).join(",");
        return this.httpClient.get(NetworkUtil.BACKEND_URL + ExperimentService.CONFLICTED_CHARS + urlParams, this.httpOptions);
    }

    chooseCharacteristics(chars: Characteristic[], experimentId: number): Observable<any> {
        const urlParams: string = "?characteristicIds=" + chars.map(c => c.characteristicId).join(",") + "&experimentId=" + experimentId;
        return this.httpClient.get(NetworkUtil.BACKEND_URL + ExperimentService.CHOOSE_CHARS + urlParams, this.httpOptions);
    }

    executeExperiment(experimentId: number): Observable<any> {
        return this.httpClient.get(NetworkUtil.BACKEND_URL + ExperimentService.EXECUTE + "?experimentId=" + experimentId, this.httpOptions);
    }

    getHistory(params: ExperimentHistoryParams): Observable<any> {
        return this.httpClient.post(NetworkUtil.BACKEND_URL + ExperimentService.HISTORY, JSON.stringify(params), this.httpOptions);
    }
}