type ApiResponseType<T> = {
  success: boolean;
  data: T;
  message: string;
};

type CursorBasedResponseType<T> = {
  content: T;
  lastCursorId: number;
};

type DefaultApiResponseType = ApiResponseType<void>;

type ApiCursorBasedResponseType<T> = ApiResponseType<
  CursorBasedResponseType<T>
>;
