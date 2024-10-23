import { create } from 'zustand';
import * as SecureStore from 'expo-secure-store';

type TokenStoreType = {
  accessToken: string | null;
  refreshToken: string | null;
  initializeToken: () => void;
  setAccessToken: (token: string) => void;
  setRefreshToken: (token: string) => void;
};

const useTokenStore = create<TokenStoreType>((set, get) => ({
  accessToken: null,
  refreshToken: null,
  initializeToken: async () => {
    if (get().accessToken) return;
    const accessToken = await SecureStore.getItemAsync('accessToken');
    const refreshToken = await SecureStore.getItemAsync('refreshToken');
    set({
      accessToken,
      refreshToken,
    });
  },
  setAccessToken: token => {
    set({ accessToken: token });
  },
  setRefreshToken: token => {
    set({ refreshToken: token });
  },
}));

export default useTokenStore;
