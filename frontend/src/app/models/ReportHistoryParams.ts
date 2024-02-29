export class ReportHistoryParams {

    constructor(
        public animalId: number,
        public reserveId: number,
        public minRate: number,
        public maxRate: number,
        public startDate: string,
        public endDate: string,
        public reporterId: number
    ) {
    }
}