import EventSource, {
  ErrorEvent,
  EventSourceEvent,
  EventSourceListener,
} from 'react-native-sse';
import { useCallback, useEffect } from 'react';
import useTokenStore from '../../store/token/useTokenStore';
import * as SecureStore from 'expo-secure-store';
import { refresh } from '../../api/authApi';
import useMe from '../queries/member/useMe';
import { useFocusEffect } from '@react-navigation/native';

type CustomEvent =
  | 'CONNECT'
  | 'delegation'
  | 'feedback'
  | 'house_invite'
  | 'schedule_completion';

const useConnectionSse = () => {
  const { data: meData } = useMe();
  const {
    accessToken,
    refreshToken,
    initializeToken,
    setRefreshToken,
    setAccessToken,
  } = useTokenStore();

  useEffect(() => {
    initializeToken();
  }, [initializeToken]);

  console.log('accessToken', accessToken);

  useFocusEffect(
    useCallback(() => {
      let eventSource: EventSource<CustomEvent> | null = null;

      const initializeEventSource = async () => {
        if (!meData) return;

        const { house } = meData.data;
        const baseUrl = 'http://192.168.0.5:8080/sse/connect';
        const url = house ? `${baseUrl}/house/${house.houseId}` : baseUrl;
        eventSource = new EventSource<CustomEvent>(url, {
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
        });

        await setupListeners(eventSource);
      };

      const setupListeners = async (eventSource: EventSource<CustomEvent>) => {
        const listener: EventSourceListener<CustomEvent> = event => {
          switch (event.type) {
            case 'open':
              console.log('Open SSE connection.');
              break;
            case 'error':
              handleConnectionError(event);
              break;
            case 'exception':
              console.error('Error:', event.message, event.error);
              break;
            case 'CONNECT':
            case 'schedule_completion':
              handleCustomEvent(event);
              break;
            default:
              console.log('Unhandled event type:', event.type);
          }
        };

        eventSource.addEventListener('CONNECT', listener);
        eventSource.addEventListener('open', listener);
        eventSource.addEventListener('message', listener);
        eventSource.addEventListener('error', listener);
        eventSource.addEventListener('schedule_completion', listener);
      };

      const handleConnectionError = async (event: ErrorEvent) => {
        console.error('Connection error:', event.message, event);
        const parsedData = JSON.parse(event.message as string);
        if (eventSource && parsedData.code === 'auth-001') {
          const { success, data } = await refresh(refreshToken as string);
          if (success) {
            setAccessToken(data.accessToken);
            setRefreshToken(data.refreshToken);
            SecureStore.setItemAsync('accessToken', data.accessToken);
            SecureStore.setItemAsync('refreshToken', data.refreshToken);
          }
          eventSource.removeAllEventListeners();
          eventSource.close();
        }
      };

      const handleCustomEvent = (
        event: EventSourceEvent<'CONNECT' | 'schedule_completion'>,
      ) => {
        if (event.type === 'schedule_completion') {
          const parsedData = JSON.parse(event.data as string);
          const { scheduleId, scheduleName, scheduleCompletionMemberNames } =
            parsedData;

          console.log('Schedule ID:', scheduleId);
          console.log('Schedule Name:', scheduleName);
          console.log(
            'Completion Member Names:',
            scheduleCompletionMemberNames,
          );
        } else {
          console.log('Event:', event);
        }
      };

      initializeEventSource();

      return () => {
        if (eventSource) {
          eventSource.removeAllEventListeners();
          eventSource.close();
        }
      };
    }, [meData, accessToken]),
  );
};

export default useConnectionSse;
