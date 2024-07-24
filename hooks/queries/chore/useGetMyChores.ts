import { UseQueryOptions, useQuery } from "@tanstack/react-query";
import { HouseMemberResponse, getHouseMembers } from "../../../api/houseApi";
import useMe from "../member/useMe";
import { ChoreOfMeResponse, getChoresOfMeByNow } from "../../../api/choreApi";

interface UseGetMyChoresOptions
  extends Omit<
    UseQueryOptions<
      ApiResponseType<ChoreOfMeResponse[]>,
      any,
      ApiResponseType<ChoreOfMeResponse[]>,
      any
    >,
    "queryKey" | "queryFn"
  > {}

const useGetMyChores = (options?: UseGetMyChoresOptions) => {
  const { data: meData } = useMe();
  const houseId = meData?.data.house?.houseId;
  return useQuery({
    queryKey: ["my-chores"],
    queryFn: () => getChoresOfMeByNow(houseId as number),
    enabled: !!houseId,
    ...options,
  });
};

export default useGetMyChores;
