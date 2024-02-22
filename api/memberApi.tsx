import axios from "axios";
import { ApiError } from "./ApiError";

export const existMemberEmail = async (email: string) => {
  try {
    const res = await axios.get(
      `http://localhost:8080/members/exista?email=${email}`
    );
    return res.data;
  } catch (error) {
    console.error(error);
    throw new ApiError();
  }
};
