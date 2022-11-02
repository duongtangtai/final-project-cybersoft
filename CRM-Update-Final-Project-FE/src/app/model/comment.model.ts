export interface ICommentModel {
    id?: string,
    description?: string,
    createdAt?: string,
    lastModifiedAt?: string,
    writer?: string,
    responseToId?: string,
}