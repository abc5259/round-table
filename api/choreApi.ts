import { Category } from "../type/Chore";
import { ApiError } from "./ApiError";
import customAxios from "./Axios";
import { API_PREFIX } from "./common";

export type ChoreOfMeResponse = {
  choreId: number;
  name: string;
  isCompleted: boolean;
  startDate: string;
  startTime: string;
  category: Category;
};

export const getChoresOfMeByNow = async (houseId: number) => {
  try {
    const res = await customAxios.get<ApiResponseType<ChoreOfMeResponse[]>>(
      `/house/${houseId}${API_PREFIX.CHORE}/me`
    );
    return res.data;
  } catch (error) {
    console.error(error);
    throw new ApiError();
  }
};
