import {Status} from "./Status";
import {Animal} from "./Animal";

export class ExecuteExperiment {

    constructor(
        public status: Status,
        public animal: Animal
    ) {
    }
}