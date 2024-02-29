import {AfterContentInit, Component} from "@angular/core";
import {AnimalService} from "../../services/animal.service";
import {ReserveService} from "../../services/reserve.service";
import {Animal} from "../../models/Animal";
import {ReserveClimateForAnimalInfo} from "../../models/ReserveClimateForAnimalInfo";
import {Reserve} from "../../models/Reserve";
import {AvailableWorker} from "../../models/AvailableWorker";
import {error} from "@angular/compiler-cli/src/transformers/util";
import {ClimateByReport} from "../../models/ClimateByReport";
import {Climate} from "../../models/Climate";

@Component({
    selector: 'app-move-to-reserve',
    templateUrl: './moveToReserve.component.html'
})

export class MoveToReserveComponent implements AfterContentInit {

    allAnimals: Animal[];
    allReserves: Reserve[];
    selectedAnimal: Animal;
    reservesByName: ReserveClimateForAnimalInfo[];
    reservesByTemp: ReserveClimateForAnimalInfo[];
    availableReserves: Reserve[];
    selectedReserve: Reserve;
    availableWorkers: AvailableWorker[];
    selectedWorker: AvailableWorker;
    climatesByReports: ClimateByReport[];
    selectedClimate: ClimateByReport;
    isReadyToMove: boolean = true;

    constructor(private animalService: AnimalService,
                private reserveService: ReserveService) {
    }

    ngAfterContentInit(): void {
        this.animalService.getAll().subscribe(
            (response: Animal[]) => this.allAnimals = response,
            error => console.log("Error while getting all animals")
        );
        this.reserveService.getAvailableWorkers().subscribe(
            (response: AvailableWorker[]) => this.availableWorkers = response,
            error => console.log("Error while getting available workers")
        );
    }

    onSelectAnimalClick(): void {
        if(this.selectedAnimal.climate !== undefined && this.selectedAnimal.climate !== null) {
            this.reserveService.getReservesByName(this.selectedAnimal.id).subscribe(
                (response: ReserveClimateForAnimalInfo[]) => this.reservesByName = response,
                error => console.log("Error while get reserves by name")
            );
            this.reserveService.getReservesByTemp(this.selectedAnimal.id).subscribe(
                (response: ReserveClimateForAnimalInfo[]) => this.reservesByTemp = response,
                error => console.log("Error while get reserves by temp")
            );
        } else {
            this.animalService.getClimatesByReport(this.selectedAnimal.id).subscribe(
                (response: ClimateByReport[]) => this.climatesByReports = response,
                error => console.log("Error while get climate by report")
            );
        }

        this.reserveService.getAll().subscribe(
            (response: Reserve[]) => {
                this.allReserves = response;
                this.reserveService.getReservesAnimalIn(this.selectedAnimal.id).subscribe(
                    (response: Reserve[]) => {
                        const filterReserves: number[] = response.map(r => r.id);
                        this.availableReserves = this.allReserves.filter(r => !filterReserves.includes(r.id));
                    }, error => console.log("Error while get reserves animal in")
                );
            }, error => console.log("Error while get all reserves")
        );

    }

    onMoveToReserveClick(): void {
        this.reserveService.getAvailableLoad(this.selectedReserve.id).subscribe(
            (response: number) => {
                this.isReadyToMove = response > 0;
                if(this.isReadyToMove) {
                    this.reserveService.moveAnimal(this.selectedAnimal.id, this.selectedReserve.id).subscribe(
                        () => window.location.reload(),
                        error => console.log("Error while moving animal")
                    );
                }
            },
            error => console.log("Error while getting available load")
        );
    }

    onMoveWorkerToReserveClick(): void {
        this.reserveService.moveWorker(this.selectedWorker.userId, this.selectedReserve.id,
            this.selectedWorker.curReserveId === undefined ? null : this.selectedWorker.curReserveId).subscribe(
            () => window.location.reload(),
            error => console.log("Error while moving worker")
        );
    }

    onSaveClimateClick(): void {
        this.selectedAnimal.climate = new Climate(this.selectedClimate.climateId, this.selectedClimate.climateName, null, null, null);
        this.animalService.upsertAnimal(this.selectedAnimal).subscribe(
            (response: Animal) => window.location.reload(),
            error => console.log("Error while upsert animal")
        )
    }

    onClickList() {
        console.log(this.selectedAnimal);
    }
}