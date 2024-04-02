import axios from "axios";
import { ApiError } from "./ApiError";

export const sendEmail = async (email: string) => {
  try {
    const res = await axios.post<DefaultApiResponseType>(
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

export const checkEmailAuthCode = async (authCode: string) => {
  const res = await axios.get<DefaultApiResponseType>(
    `http://localhost:8080/auth/emails?code=${authCode}`
  );
  return res.data;
};
