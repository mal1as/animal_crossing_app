import {Climate} from "./Climate";
import {AnimalClass} from "./AnimalClass";

export class Animal {

    constructor(
        public id: number,
        public name: string,
        public isArtificial: boolean,
        public climate: Climate,
        public animalClass: AnimalClass
    ) {
    }
}