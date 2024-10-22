import { useMutation, useQueryClient } from '@tanstack/react-query';
import { completeSchedule } from '../../../api/scheduleApi';
import { ApiError } from '../../../api/ApiError';

const useCompleteSchedule = () => {
  const queryClient = useQueryClient();
  return useMutation<
    DefaultApiResponseType,
    ApiError,
    { houseId: number; scheduleId: number }
  >({
    mutationFn: ({
      houseId,
      scheduleId,
    }: {
      houseId: number;
      scheduleId: number;
    }) => completeSchedule(houseId, scheduleId),
    onSuccess: (data, { scheduleId }) => {
      queryClient.invalidateQueries({ queryKey: ['my-schedules'] });
    },
    onError: error => {
      alert('다시 시도해 주세요');
    },
  });
};

export default useCompleteSchedule;
