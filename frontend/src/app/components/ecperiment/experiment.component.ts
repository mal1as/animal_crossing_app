import {Component} from '@angular/core';
import {ExperimentService} from "../../services/experiment.service";
import {Experiment} from "../../models/Experiment";
import {Animal} from "../../models/Animal";
import {AnimalService} from "../../services/animal.service";
import {Characteristic} from "../../models/Characteristic";
import {ConflictedChars} from "../../models/ConflictedChars";
import {ExecuteExperiment} from "../../models/ExecuteExperiment";
import {Status} from "../../models/Status";

@Component({
    selector: 'app-experiment',
    templateUrl: './experiment.component.html'
})

export class ExperimentComponent {

    experiment: Experiment = new Experiment(0, null, '', null, null);
    createdAnimal: Animal;
    experimentStatus: Status;

    animals: Animal[] = [];
    selectedAnimals: Animal[];
    availableChars: Characteristic[] = [];
    selectedChars: Characteristic[];
    conflictedChars: ConflictedChars[];

    isNewAnimalNameSaved = false;
    currentStep: number = 0;

    constructor(private experimentService: ExperimentService,
                private animalService:AnimalService) {
    }

    onStartClick(): void {
        this.experimentService.startExperiment().subscribe(
            (experimentId: number) => {
                this.experiment.id = experimentId;
                this.animalService.getAll().subscribe(
                    (allAnimals: Animal[]) => {
                        this.animals = allAnimals;
                        this.currentStep++;
                    }, error => console.log("Error while get all animals")
                )
            }, error => console.log("Error while start experiment")
        );
    }

    onSelectAnimalsClick(): void {
        this.experimentService.selectAnimals(this.selectedAnimals, this.experiment.id).subscribe(
            () => {
                this.experimentService.getAvailableCharacteristics(this.experiment.id).subscribe(
                    (chars: Characteristic[]) => {
                        this.availableChars = chars;
                        this.experimentService.getConflictedCharacteristics(chars).subscribe(
                            (confChars: ConflictedChars[]) => {
                                this.conflictedChars = confChars;
                                this.currentStep++;
                            }, error => console.log("Error while getting conflicted chars")
                        );
                    }, error => console.log("Error while getting available chars")
                );
            }, error => console.log("Error while select animals")
        );
    }

    onSelectCharsClick(): void {
        this.experimentService.chooseCharacteristics(this.selectedChars, this.experiment.id).subscribe(
            () => {
                this.currentStep++;
            }, error => console.log("Error while choosing chars")
        );
    }

    onExecuteClick(): void {
        this.experimentService.executeExperiment(this.experiment.id).subscribe(
            (executeExperiment: ExecuteExperiment) => {
                this.experimentStatus = executeExperiment.status;
                if(executeExperiment.status.name !== 'FAILED') this.createdAnimal = executeExperiment.animal;
                this.currentStep++;
            }, error => console.log("Error while executing experiment")
        );
    }

    onSaveAnimalNameClick(): void {
        this.animalService.upsertAnimal(this.createdAnimal).subscribe(
            (upserted: Animal) => this.isNewAnimalNameSaved = true,
            error => console.log("Error while saving animal name")
        );
    }

    isSelectedAnimalsCorrect(): boolean {
        return this.selectedAnimals !== undefined && this.selectedAnimals.length >= 2 && this.selectedAnimals.length <= 5 &&
            this.selectedAnimals.filter(a => a.animalClass.id === this.selectedAnimals[0].animalClass.id).length === this.selectedAnimals.length;
    }

    isSelectedCharsCorrect(): boolean {
        // todo message about every conflicts
        if(this.conflictedChars === undefined || this.selectedChars === undefined) return false;
        const selectedIds = this.selectedChars.map(c => c.characteristicId);
        return this.conflictedChars
            .filter(confChars => selectedIds.includes(confChars.firstCharId) && selectedIds.includes(confChars.secondCharId)).length === 0;
    }

    isSuccessExperiment = () => this.experimentStatus !== undefined && this.experimentStatus.name === 'SUCCESS';
    isMutationExperiment = () => this.experimentStatus !== undefined && this.experimentStatus.name === 'MUTATION';
    isFailedExperiment = () => this.experimentStatus !== undefined && this.experimentStatus.name === 'FAILURE';

    onToHomeClick(): void {
        window.location.href = '/home';
    }

    onToAnimalsClick(): void {
        window.location.href = '/animal'
    }

    isDisableAnimalSelect = () => this.currentStep !== 1;
    isDisableCharsSelect = () => this.currentStep !== 2;
}