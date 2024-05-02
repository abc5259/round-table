import { ApiError } from "./ApiError";
import customAxios from "./Axios";
import { API_PREFIX } from "./common";

export const existMemberEmail = async (email: string) => {
  try {
    const res = await customAxios.post<DefaultApiResponseType>(
      `${API_PREFIX.MEMBER}/exists`,
      {
        email,
      }
    );
    return res.data;
  } catch (error) {
    console.error(error);
    throw new ApiError();
  }
};

export const enum Gender {
  MEN = "MEN",
  GIRL = "GIRL",
}

export const updateProfile = async (name: string, gender: Gender) => {
  try {
    const res = await customAxios.patch<DefaultApiResponseType>(
      `${API_PREFIX.MEMBER}/setting/profile`,
      {
        name,
        gender,
      }
    );
    return res.data;
  } catch (error) {
    console.error(error);
    throw new ApiError();
  }
};

export const getMe = async () => {
  try {
    const res = await customAxios.get<DefaultApiResponseType>(
      `${API_PREFIX.MEMBER}/me`
    );
    return res.data;
  } catch (error) {
    console.error(error);
    throw new ApiError();
  }
};
