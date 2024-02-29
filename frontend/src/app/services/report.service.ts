import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {NetworkUtil} from "../utils/NetworkUtil";
import {Observable} from "rxjs";
import {Report} from "../models/Report";
import {ReportHistoryParams} from "../models/ReportHistoryParams";

@Injectable({providedIn: 'root'})
export class ReportService {

    static SHOW_AVAILABLE: string = "/report/showAvailable";
    static REPORT: string = "/report";
    static HISTORY: string = "/report/history";

    private currentUser = JSON.parse(localStorage.getItem('currentUser'));
    private httpOptions = {
        headers: new HttpHeaders({
            'Authorization': 'Basic ' + localStorage.getItem('basic64Credentials'),
            'Content-Type': 'application/json'
        })
    };

    constructor(private httpClient: HttpClient) {
    }

    showAvailableAnimals(reserveId: number): Observable<any> {
        const urlParams: string = "?worker=" + this.currentUser.username + (reserveId !== null ? "&reserveId=" + reserveId : "");
        return this.httpClient.get(NetworkUtil.BACKEND_URL + ReportService.SHOW_AVAILABLE + urlParams, this.httpOptions);
    }

    upsertReport(report: Report): Observable<any> {
        report.reporter = this.currentUser;
        return this.httpClient.post(NetworkUtil.BACKEND_URL + ReportService.REPORT, JSON.stringify(report), this.httpOptions);
    }

    getHistory(params: ReportHistoryParams) : Observable<any> {
        return this.httpClient.post(NetworkUtil.BACKEND_URL + ReportService.HISTORY, JSON.stringify(params), this.httpOptions);
    }
}