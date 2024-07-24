import { UseQueryOptions, useQuery } from "@tanstack/react-query";
import { HouseMemberResponse, getHouseMembers } from "../../../api/houseApi";
import useMe from "../member/useMe";

interface UseHouseMembersOptions
  extends Omit<
    UseQueryOptions<
      ApiResponseType<HouseMemberResponse[]>,
      any,
      ApiResponseType<HouseMemberResponse[]>,
      any
    >,
    "queryKey" | "queryFn"
  > {}

const useHouseMembers = (options?: UseHouseMembersOptions) => {
  const { data: meData } = useMe();
  const houseId = meData?.data.house?.houseId;
  return useQuery({
    queryKey: ["houseMember"],
    queryFn: () => getHouseMembers(houseId as number),
    enabled: !!houseId,
    ...options,
  });
};

export default useHouseMembers;
