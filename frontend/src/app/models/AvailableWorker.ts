export class AvailableWorker {

    constructor(
        public userId: number,
        public username: string,
        public curReserveId: number,
        public curReserveName: string,
        public reserveAvailableLoad: number
    ) {
    }
}