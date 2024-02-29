export class ClimateByReport {

    constructor(
        public climateId: number,
        public climateName: string,
        public reportsNum: number,
        public avgHealthRate: number
    ) {
    }
}