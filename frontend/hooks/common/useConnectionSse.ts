import EventSource, { EventSourceListener } from 'react-native-sse';
import { useEffect } from 'react';
import 'react-native-url-polyfill/auto';
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
          ? new URL(`http://localhost:8080/sse/connect/house/${house.houseId}`)
          : new URL(`http://localhost:8080/sse/connect`);
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
            console.error('Connection error:', event.message);
          } else if (event.type === 'exception') {
            console.error('Error:', event.message, event.error);
          } else if (event.type === 'CONNECT') {
            console.log('event ', event);
          } else if (event.type === 'schedule_completion') {
            console.log('event ', event);
          }
        };
        eventSource.addEventListener('CONNECT', listener);
        eventSource.addEventListener('open', listener);
        eventSource.addEventListener('message', listener);
        eventSource.addEventListener('error', listener);
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
