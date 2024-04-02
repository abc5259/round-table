type ApiResponseType<T> = {
  success: boolean;
  data: T;
};

type DefaultApiResponseType = ApiResponseType<void>;
