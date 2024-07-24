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

export type HouseMemberResponse = {
  memberId: number;
  name: string;
  profileUrl?: string;
};

export const getHouseMembers = async (houseId: number) => {
  try {
    const res = await customAxios.get<ApiResponseType<HouseMemberResponse[]>>(
      `${API_PREFIX.HOUSE}/${houseId}`
    );
    return res.data;
  } catch (error) {
    console.error(error);
    throw new ApiError();
  }
};
