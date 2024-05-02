import { ApiError } from "./ApiError";
import customAxios from "./Axios";

export const sendEmail = async (email: string) => {
  try {
    const res = await customAxios.post<DefaultApiResponseType>(
      "http://localhost:8080/auth/emails",
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

export const checkEmailAuthCode = async (email: string, authCode: string) => {
  try {
    const res = await customAxios.get<DefaultApiResponseType>(
      `http://localhost:8080/auth/emails?email=${email}&code=${authCode}`
    );
    return res.data;
  } catch (error) {
    console.error(error);
    throw new ApiError();
  }
};

export const registerMember = async (email: string, password: string) => {
  try {
    const res = await customAxios.post<DefaultApiResponseType>(
      `http://localhost:8080/auth/register`,
      {
        email,
        password,
      }
    );
    return res.data;
  } catch (error) {
    console.error(error);
    throw new ApiError();
  }
};

type LoginResponseType = ApiResponseType<{
  accessToken: string;
  refreshToken: string;
}>;

export const login = async (email: string, password: string) => {
  try {
    const res = await customAxios.post<LoginResponseType>(
      `http://localhost:8080/auth/login`,
      {
        email,
        password,
      }
    );

    return res.data;
  } catch (error) {
    console.error(error);
    throw new ApiError();
  }
};

export const refresh = async (refreshToken: String) => {
  try {
    const res = await customAxios.post<LoginResponseType>(
      `http://localhost:8080/token/refresh`,
      {
        refreshToken,
      }
    );

    return res.data;
  } catch (error) {
    console.error(error);
    throw new ApiError();
  }
};
