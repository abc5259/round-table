import { useQuery, UseQueryOptions } from '@tanstack/react-query';
import useMe from '../member/useMe';
import {
  getSchedulesOfHouse,
  ScheduleOfHouseResponse,
} from '../../../api/scheduleApi';

interface UseGetScheduleOfHouseOptions
  extends Omit<
    UseQueryOptions<
      ApiCursorBasedResponseType<ScheduleOfHouseResponse[]>,
      any,
      ApiCursorBasedResponseType<ScheduleOfHouseResponse[]>,
      any
    >,
    'queryKey' | 'queryFn'
  > {}

const useGetChoresOfHouse = (options?: UseGetScheduleOfHouseOptions) => {
  const { data: meData } = useMe();
  const houseId = meData?.data?.house?.houseId;
  return useQuery({
    queryKey: ['chores'],
    queryFn: () => getSchedulesOfHouse(houseId as number),
    enabled: !!houseId,
    ...options,
  });
};

export default useGetChoresOfHouse;
