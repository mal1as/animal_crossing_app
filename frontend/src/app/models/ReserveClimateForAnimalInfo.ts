export class ReserveClimateForAnimalInfo {

    constructor(
        public reserveId: number,
        public reserveName: string,
        public reserveClimateName: string,
        public animalClimateName: string,
        public tempMinDiff: number,
        public tempMaxDiff: number,
        public totalTempDiff: number
    ) {
    }
}