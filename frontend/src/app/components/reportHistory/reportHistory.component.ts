import {AfterContentInit, Component} from "@angular/core";
import {AnimalService} from "../../services/animal.service";
import {ReserveService} from "../../services/reserve.service";
import {Animal} from "../../models/Animal";
import {User} from "../../models/User";
import {Reserve} from "../../models/Reserve";
import {Report} from "../../models/Report";
import {ReportService} from "../../services/report.service";
import {ReportHistoryParams} from "../../models/ReportHistoryParams";

@Component({
    selector: 'app-report-history',
    templateUrl: './reportHistory.component.html'
})

export class ReportHistoryComponent implements AfterContentInit {

    animals: Animal[];
    reporters: User[];
    reserves: Reserve[];
    reports: Report[];

    selectedAnimal: Animal;
    selectedReporter: User;
    selectedReserve: Reserve;
    minRate: number;
    maxRate: number;
    startDate: string;
    endDate: string;

    constructor(private animalService: AnimalService,
                private reserveService: ReserveService,
                private reportService: ReportService) {
    }

    ngAfterContentInit() {
        this.animalService.getAll().subscribe(
            (allAnimals: Animal[]) => {
                this.animals = allAnimals;
                this.reserveService.getAll().subscribe(
                    (allReserves: Reserve[]) => {
                        this.reserves = allReserves;
                        this.reserveService.getAllUsers().subscribe(
                            (allReporters: User[]) => this.reporters = allReporters,
                            error => console.log("Error while getting all reporters")
                        );
                    },
                    error => console.log("Error while getting all reserves")
                );
            }, error => console.log("Error while getting all animals")
        );
    }

    onSearchClick(): void {
        const params: ReportHistoryParams = new ReportHistoryParams(
            this.selectedAnimal === undefined ? null : this.selectedAnimal.id,
            this.selectedReserve === undefined ? null : this.selectedReserve.id,
            this.minRate === undefined ? null : this.minRate,
            this.maxRate === undefined ? null : this.maxRate,
            this.startDate === undefined ? null : this.parseDate(this.startDate),
            this.endDate === undefined ? null : this.parseDate(this.endDate),
            this.selectedReporter === undefined ? null : this.selectedReporter.id
        );

        this.reportService.getHistory(params).subscribe(
            (foundReports: Report[]) => {
                this.reports = foundReports;
            }, error => console.log("Error while search reports")
        );
    }

    parseDate(date: string): string {
        return JSON.stringify(date).substring(1, 11);
    }

    isInvalidMinRate(): boolean {
        return this.minRate !== undefined && this.minRate !== null && (this.minRate < 1 || this.minRate > 5);
    }

    isInvalidMaxRate(): boolean {
        return this.maxRate !== undefined && this.maxRate !== null && (this.maxRate < 1 || this.maxRate > 5);
    }

    onUnselectClick(): void {
        this.selectedAnimal = undefined;
        this.selectedReserve = undefined;
        this.selectedReporter = undefined;
        this.minRate = undefined;
        this.maxRate = undefined;
        this.startDate = undefined;
        this.endDate = undefined;
    }
}