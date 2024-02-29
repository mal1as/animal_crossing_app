import {Status} from "./Status";
import {Animal} from "./Animal";
import {User} from "./User";

export class Experiment {

    constructor(
        public id: number,
        public status: Status,
        public startDate: string,
        public createdAnimal: Animal,
        public experimenter: User
    ) {
    }
}