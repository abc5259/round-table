export type Category =
    | "COOKING"
    | "CLEANING"
    | "LAUNDRY"
    | "TRASH"
    | "GROCERY"
    | "ONE_TIME";

export type RepeateCategory = Exclude<Category, "ONE_TIME">;