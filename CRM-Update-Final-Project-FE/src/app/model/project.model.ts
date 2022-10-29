import { IStaffModel } from "./staff.model";

export interface IProjectModel {
    id?: string,
    name?: string,
    description?: string,
    symbol?: string,
    status?: string,
    creatorUsername?: string,
    leaderUsername?: string,
}
