export interface ITaskModel {
    id?: string,
    name?: string,
    description?: string,
    startDateExpected: string,
    endDateExpected: string,
    startDateInFact: string,
    endDateInFact: string,
    status?: string,
    projectName?: string,
    reporterUsername?: string,
}