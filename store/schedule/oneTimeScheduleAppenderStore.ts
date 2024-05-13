import { create } from "zustand";
import { Time } from "../../components/molecules/TimePicker/TimePicker";

type OneTimeScheduleAppenderStoreType = {
  name: string;
  time: Time | null;
  date: string;
  allocators: number[];
  changeName: (name: string) => void;
  changeDate: (date: string) => void;
  changeTime: (time: Time) => void;
};

export const useOneTimeScheduleAppednerStore =
  create<OneTimeScheduleAppenderStoreType>((set, get) => ({
    name: "",
    time: null,
    date: "",
    allocators: [],
    changeName: (name: string) => set(() => ({ name })),
    changeDate: (date: string) => set(() => ({ date })),
    changeTime: (time: Time) => set(() => ({ time })),
  }));
