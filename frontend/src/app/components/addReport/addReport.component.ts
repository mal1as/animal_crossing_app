import {AfterContentInit, Component} from "@angular/core";
import {ReserveService} from "../../services/reserve.service";
import {ReportService} from "../../services/report.service";
import {Reserve} from "../../models/Reserve";
import {Animal} from "../../models/Animal";
import {Report} from "../../models/Report";

@Component({
    selector: 'app-add-report',
    templateUrl: './addReport.component.html'
})

export class AddReportComponent implements AfterContentInit {

    reserves: Reserve[];
    selectedReserve: Reserve;
    animals: Animal[];
    selectedAnimal: Animal;
    reportDate: string;
    healthRate: number;
    comment: string;

    constructor(private reserveService: ReserveService,
                private reportService: ReportService) {
    }

    ngAfterContentInit(): void {
        this.reserveService.getAll().subscribe(
            (allReserves: Reserve[]) => {
                this.reserves = allReserves;
                this.reportService.showAvailableAnimals(null).subscribe(
                    (availableAnimals) => {
                        this.animals = availableAnimals.animals;
                        this.selectedReserve = availableAnimals.reserve;
                    },
                    error => console.log("Error while getting available animals to report")
                )
            }, error => console.log("Error while getting all reserves")
        );
    }

    onSelectReserveClick(): void {
        this.reportService.showAvailableAnimals(this.selectedReserve.id).subscribe(
            (availableAnimals) => {
                this.animals = availableAnimals.animals;
                this.selectedReserve = availableAnimals.reserve;
                this.comment = '';
                this.healthRate = 5;
            },
            error => console.log("Error while getting available animals to report")
        )
    }

    isInvalidRate(): boolean {
        return this.healthRate === undefined || this.healthRate < 1 || this.healthRate > 5;
    }

    onSaveReportClick(): void {
        const report = new Report(0, this.selectedAnimal, this.selectedReserve, this.healthRate, this.comment, this.getStringDate(), null);
        this.reportService.upsertReport(report).subscribe(
            (upserted: Report) => {
                this.selectedAnimal = undefined;
            },
            error => console.log("Error while upserting report")
        );
    }

    getStringDate(): string {
        return JSON.stringify(this.reportDate).substring(1, 11);
    }
}