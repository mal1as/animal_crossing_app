import {Animal} from "./Animal";
import {Characteristic} from "./Characteristic";

export class AnimalWithCharacteristics {

    constructor(
        public animal: Animal,
        public characteristics: Characteristic[]
    ) {
    }
}