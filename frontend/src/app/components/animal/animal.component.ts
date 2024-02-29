import {AfterContentInit, Component} from "@angular/core";
import {AnimalService} from "../../services/animal.service";
import {AnimalWithCharacteristics} from "../../models/AnimalWithCharacteristics";

@Component({
    selector: 'app-animals',
    templateUrl: './animal.component.html'
})

export class AnimalComponent implements AfterContentInit {

    animals: AnimalWithCharacteristics[];

    constructor(private animalService: AnimalService) {
    }

    ngAfterContentInit(): void {
        this.animalService.getAnimalsWithCharacteristics().subscribe(
            (response: AnimalWithCharacteristics[]) => this.animals = response,
            error => console.log("Error while getting animals")
        );
    }

    getCharacteristicsString(animal: AnimalWithCharacteristics): string {
        return animal.characteristics.map(c => c.name).join(", ");
    }
}