import { useQuery, UseQueryOptions } from '@tanstack/react-query';
import useMe from '../member/useMe';
import { ChoreOfHouseResponse, getChoresOfHouse } from '../../../api/choreApi';

interface UseGetChoresOfHouseOptions
  extends Omit<
    UseQueryOptions<
      ApiCursorBasedResponseType<ChoreOfHouseResponse[]>,
      any,
      ApiCursorBasedResponseType<ChoreOfHouseResponse[]>,
      any
    >,
    'queryKey' | 'queryFn'
  > {}

const useGetChoresOfHouse = (options?: UseGetChoresOfHouseOptions) => {
  const { data: meData } = useMe();
  const houseId = meData?.data?.house?.houseId;
  return useQuery({
    queryKey: ['chores'],
    queryFn: () => getChoresOfHouse(houseId as number),
    enabled: !!houseId,
    ...options,
  });
};

export default useGetChoresOfHouse;
