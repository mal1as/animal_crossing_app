import {Climate} from "./Climate";

export class Reserve {

    constructor(
        public id: number,
        public name: string,
        public description: string,
        public climate: Climate
    ) {
    }
}