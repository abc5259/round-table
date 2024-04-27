type ApiResponseType<T> = {
  success: boolean;
  data: T;
  message: string;
};

type DefaultApiResponseType = ApiResponseType<void>;
