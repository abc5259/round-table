import axios from "axios";
import { ApiError } from "./ApiError";
import * as SecureStore from "expo-secure-store";

const resolveHttpStatus = [400, 404];

axios.interceptors.request.use(
  async config => {
    const accessToken = await SecureStore.getItemAsync("accessToken");

    if (accessToken != null) {
      config.headers["Authorization"] = `Bearer ${accessToken}`;
    }

    return config;
  },
  (error: any) => {
    console.log(error);
    return Promise.reject(error);
  }
);

axios.interceptors.response.use(
  response => {
    // 응답을 그대로 반환 (모든 성공적인 상태 코드)
    return response;
  },
  async error => {
    // 특정 상태 코드 처리 로직
    if (error.response && resolveHttpStatus.includes(error.response.status)) {
      // 여기서 error.response 또는 커스텀 데이터 객체를 반환할 수 있음
      return Promise.resolve(error.response);
    }

    if (error.response && error.response.status === 401) {
      if (error.response.code === "auth-001") {
        //
        const refreshToken = await SecureStore.getItemAsync("refreshToken");
        if (refreshToken != null) {
          const { success, data } = await refresh(refreshToken);
          if (success) {
            await Promise.all([
              SecureStore.setItemAsync("accessToken", data.accessToken),
              SecureStore.setItemAsync("refreshToken", data.refreshToken),
            ]);

            error.config.headers.Authorization = `Bearer ${data.accessToken}`;
            return axios(error.config);
          }
        }
      }
    }
    // 다른 모든 에러는 그대로 다음으로 넘김
    return Promise.reject(error);
  }
);

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

export const checkEmailAuthCode = async (email: string, authCode: string) => {
  try {
    const res = await axios.get<DefaultApiResponseType>(
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
    const res = await axios.post<DefaultApiResponseType>(
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
    const res = await axios.post<LoginResponseType>(
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
    const res = await axios.post<LoginResponseType>(
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
