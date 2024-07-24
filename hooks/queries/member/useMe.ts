import { UseQueryOptions, useQuery } from "@tanstack/react-query";
import { GetMeResponse, getMe } from "../../../api/memberApi";

interface UseMeOptions
  extends Omit<
    UseQueryOptions<GetMeResponse, any, GetMeResponse, any>,
    "queryKey" | "queryFn"
  > {}

const useMe = (options?: UseMeOptions) => {
  return useQuery({
    queryKey: ["me"],
    queryFn: getMe,
    ...options,
  });
};

export default useMe;
