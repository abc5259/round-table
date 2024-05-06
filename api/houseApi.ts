import { ApiError } from "./ApiError";
import customAxios from "./Axios";
import { API_PREFIX } from "./common";

export const canInviteHouseEmail = async (email: string) => {
  try {
    const res = await customAxios.get<DefaultApiResponseType>(
      `${API_PREFIX.HOUSE}/check-invite?email=${email}`
    );
    return res.data;
  } catch (error) {
    console.error(error);
    throw new ApiError();
  }
};
