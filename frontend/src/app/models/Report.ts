import {Animal} from "./Animal";
import {Reserve} from "./Reserve";
import {User} from "./User";

export class Report {

    constructor(
        public id: number,
        public animal: Animal,
        public reserve: Reserve,
        public healthRate: number,
        public comment: string,
        public reportDate: string,
        public reporter: User
    ) {
    }
}