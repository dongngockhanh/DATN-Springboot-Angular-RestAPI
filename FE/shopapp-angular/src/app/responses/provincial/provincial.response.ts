import { District } from "./district";
import { Province } from "./province";
import { Ward } from "./ward";

export class ProvincialResponse {
    provinces: Province[] | undefined;
    districts: District[] | undefined;
    wards: Ward[] | undefined;
}