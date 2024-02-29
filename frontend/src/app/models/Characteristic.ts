export class Characteristic {

    constructor(
        // for experiment
        public characteristicId: number,
        public characteristicName: string,

        // normal fields
        public id: number,
        public name: string,
        public description: string,
        public coefficient: number
    ) {
    }
}