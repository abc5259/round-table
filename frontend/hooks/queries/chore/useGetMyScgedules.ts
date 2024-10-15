import { UseQueryOptions, useQuery } from "@tanstack/react-query";
import useMe from "../member/useMe";
import {
  getSchedulesOfMeByNow,
  ScheduleOfMeResponse,
} from "../../../api/scheduleApi";

interface UseGetMyScheduleOptions
  extends Omit<
    UseQueryOptions<
      ApiCursorBasedResponseType<ScheduleOfMeResponse[]>,
      any,
      ApiCursorBasedResponseType<ScheduleOfMeResponse[]>,
      any
    >,
    "queryKey" | "queryFn"
  > {}

const useGetMySchedules = (options?: UseGetMyScheduleOptions) => {
  const { data: meData } = useMe();
  const houseId = meData?.data?.house?.houseId;
  return useQuery({
    queryKey: ["my-schedules"],
    queryFn: () => getSchedulesOfMeByNow(houseId as number),
    enabled: !!houseId,
    ...options,
  });
};

export default useGetMySchedules;
