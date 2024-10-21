import EventSource, { EventSourceListener } from 'react-native-sse';
import { useEffect } from 'react';
import 'react-native-url-polyfill/auto';
import * as SecureStore from 'expo-secure-store';

type CustomEvent =
  | 'CONNECT'
  | 'delegation'
  | 'feedback'
  | 'house_invite'
  | 'schedule_completion';

const useConnectionSse = () => {
  useEffect(() => {
    const url = new URL(`http://localhost:8080/sse/connect/house/${1}`);
    let eventSource: EventSource<CustomEvent>;
    SecureStore.getItemAsync('accessToken').then(token => {
      eventSource = new EventSource<'CONNECT'>(url, {
        headers: {
          Authorization: `Bearer ${token}`,
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
    });

    return () => {
      if (eventSource != null) {
        eventSource.removeAllEventListeners();
        eventSource.close();
      }
    };
  }, []);
};

export default useConnectionSse;
