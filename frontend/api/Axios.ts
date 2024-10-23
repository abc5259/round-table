import axios from 'axios';
import * as SecureStore from 'expo-secure-store';
import { ApiError } from './ApiError';
import useTokenStore from '../store/token/useTokenStore';

const resolveHttpStatus = [400, 404];

const customAxios = axios.create({
  baseURL: 'http://192.168.0.5:8080',
});

customAxios.interceptors.request.use(
  async config => {
    const accessToken = await SecureStore.getItemAsync('accessToken');
    if (accessToken != null) {
      config.headers['Authorization'] = `Bearer ${accessToken}`;
    }

    return config;
  },
  (error: any) => {
    console.log(error);
    return Promise.reject(error);
  },
);

customAxios.interceptors.response.use(
  response => {
    // 응답을 그대로 반환 (모든 성공적인 상태 코드)
    return response;
  },

  async error => {
    console.log(error);
    // 특정 상태 코드 처리 로직
    if (error.response && resolveHttpStatus.includes(error.response.status)) {
      // 여기서 error.response 또는 커스텀 데이터 객체를 반환할 수 있음
      return Promise.resolve(error.response);
    }

    if (error.response && error.response.status === 401) {
      if (error.response.data.code === 'auth-001') {
        const refreshToken = await SecureStore.getItemAsync('refreshToken');
        if (refreshToken != null) {
          const { success, data } = await refresh(refreshToken);
          if (success) {
            useTokenStore.setState({
              accessToken: data.accessToken,
              refreshToken: data.refreshToken,
            });
            await Promise.all([
              SecureStore.setItemAsync('accessToken', data.accessToken),
              SecureStore.setItemAsync('refreshToken', data.refreshToken),
            ]);

            error.config.headers.Authorization = `Bearer ${data.accessToken}`;
            return axios(error.config);
          }
        }
      }
      return Promise.resolve(error.response);
    }
    // 다른 모든 에러는 그대로 다음으로 넘김
    return Promise.reject(error);
  },
);

type RefreshResponseType = ApiResponseType<{
  accessToken: string;
  refreshToken: string;
}>;

const refresh = async (refreshToken: String) => {
  try {
    const res = await customAxios.post<RefreshResponseType>(`/token/refresh`, {
      refreshToken,
    });

    return res.data;
  } catch (error) {
    console.error(error);
    throw new ApiError();
  }
};

export default customAxios;
