import { create } from "zustand";

type HouseAppenderStoreType = {
  name: string;
  inviteEmails: string[];
  step: number;
  next: () => void;
  updateName: (name: string) => void;
};

export const useHouseAppenderStore = create<HouseAppenderStoreType>(
  (set, get) => ({
    name: "",
    inviteEmails: [],
    step: 1,
    updateName: (name: string) =>
      set(state => {
        return {
          ...state,
          name,
        };
      }),
    next: () =>
      set(state => {
        return {
          ...state,
          step: state.step + 1,
        };
      }),
  })
);
