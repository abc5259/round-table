import EventSource, { EventSourceListener } from 'react-native-sse';
import { useEffect } from 'react';
import * as SecureStore from 'expo-secure-store';
import useMe from '../queries/member/useMe';

type CustomEvent =
  | 'CONNECT'
  | 'delegation'
  | 'feedback'
  | 'house_invite'
  | 'schedule_completion';

const useConnectionSse = () => {
  const { data: meData } = useMe();

  useEffect(() => {
    let eventSource: EventSource<CustomEvent> | null;

    const initializeEventSource = async () => {
      if (meData && meData.success) {
        const { house } = meData.data;
        const url = house
          ? new URL(
              `http://192.168.0.5:8080/sse/connect/house/${house.houseId}`,
            )
          : new URL(`http://192.168.0.5:8080/sse/connect`);
        const accessToken = await SecureStore.getItemAsync('accessToken');

        eventSource = new EventSource<'CONNECT'>(url, {
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
        });

        const listener: EventSourceListener<CustomEvent> = event => {
          if (event.type === 'open') {
            console.log('Open SSE connection.');
          } else if (event.type === 'error') {
            console.error('Connection error:', event.message, event);
          } else if (event.type === 'exception') {
            console.error('Error:', event.message, event.error);
          } else if (event.type === 'CONNECT') {
            console.log('event ', event);
          } else if (event.type === 'schedule_completion') {
            console.log('event ', event);
            const parsedData = JSON.parse(event.data as string);

            // 각각의 값을 추출
            const scheduleId = parsedData.scheduleId;
            const scheduleName = parsedData.scheduleName;
            const scheduleCompletionMemberNames =
              parsedData.scheduleCompletionMemberNames;

            console.log('Schedule ID:', scheduleId);
            console.log('Schedule Name:', scheduleName);
            console.log(
              'Completion Member Names:',
              scheduleCompletionMemberNames,
            );
          }
        };
        eventSource.addEventListener('CONNECT', listener);
        eventSource.addEventListener('open', listener);
        eventSource.addEventListener('message', listener);
        eventSource.addEventListener('error', listener);
        eventSource.addEventListener('schedule_completion', listener);
      }
    };

    initializeEventSource();

    return () => {
      if (eventSource != null) {
        eventSource.removeAllEventListeners();
        eventSource.close();
      }
    };
  }, [meData]);
};

export default useConnectionSse;
