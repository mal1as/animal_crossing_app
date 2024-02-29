import {AfterContentInit, Component} from "@angular/core";
import {AnimalService} from "../../services/animal.service";
import {ReserveService} from "../../services/reserve.service";
import {Animal} from "../../models/Animal";
import {User} from "../../models/User";
import {Status} from "../../models/Status";
import {ExperimentService} from "../../services/experiment.service";
import {Experiment} from "../../models/Experiment";
import {ExperimentHistoryParams} from "../../models/ExperimentHistoryParams";

@Component({
    selector: 'app-experiment-history',
    templateUrl: './experimentHistory.component.html'
})

export class ExperimentHistoryComponent implements AfterContentInit{

    animals: Animal[];
    experimenters: User[];
    statuses: Status[] = [new Status(1, 'SUCCESS'), new Status(2, 'FAILURE'), new Status(3, 'MUTATION')];
    experiments: Experiment[];

    selectedAnimals: Animal[];
    selectedExperimenter: User;
    selectedStatus: Status;
    startDate: string;
    endDate: string;

    constructor(private animalService: AnimalService,
                private reserveService: ReserveService,
                private experimentService: ExperimentService) {
    }

    ngAfterContentInit(): void {
        this.animalService.getAll().subscribe(
            (allAnimals: Animal[]) => {
                this.animals = allAnimals;
                this.reserveService.getAllUsers().subscribe(
                    (allExperimenters: User[]) => this.experimenters = allExperimenters,
                    error => console.log("Error while getting all experimenters")
                );
            }, error => console.log("Error while getting all animals")
        );
    }

    onSearchClick(): void {
        const params: ExperimentHistoryParams = new ExperimentHistoryParams(
            this.selectedStatus === undefined ? null : this.selectedStatus.id,
            this.startDate === undefined ? null : this.parseDate(this.startDate),
            this.endDate === undefined ? null : this.parseDate(this.endDate),
            this.selectedAnimals === undefined ? null : this.selectedAnimals.map(a => a.id),
            this.selectedExperimenter === undefined ? null : this.selectedExperimenter.id
        );

        this.experimentService.getHistory(params).subscribe(
            (foundExperiments: Experiment[]) => this.experiments = foundExperiments,
            error => console.log("Error while search experiments")
        );
    }

    parseDate(date: string): string {
        return JSON.stringify(date).substring(1, 11);
    }

    onUnselectClick(): void {
        this.selectedAnimals = undefined;
        this.selectedStatus = undefined;
        this.selectedExperimenter = undefined;
        this.startDate = undefined;
        this.endDate = undefined;
    }
}