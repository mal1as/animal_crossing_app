export class ExperimentHistoryParams {

    constructor(
        public statusId: number,
        public startDate: string,
        public endDate: string,
        public animalIds: number[],
        public experimenterId: number
    ) {
    }
}