import { ApiError } from "./ApiError";
import customAxios from "./Axios";
import { API_PREFIX } from "./common";

export const createHouse = async (name: string, inviteEmails?: string[]) => {
  try {
    const res = await customAxios.post<ApiResponseType<{ houseId: number }>>(
      `${API_PREFIX.HOUSE}`,
      {
        name,
        inviteEmails,
      }
    );
    return res.data;
  } catch (error) {
    console.error(error);
    throw new ApiError();
  }
};
