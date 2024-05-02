import { ApiError } from "./ApiError";
import customAxios from "./Axios";

export const existMemberEmail = async (email: string) => {
  try {
    const res = await customAxios.post<DefaultApiResponseType>(
      "http://localhost:8080/members/exists",
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
      "http://localhost:8080/members/setting/profile",
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
