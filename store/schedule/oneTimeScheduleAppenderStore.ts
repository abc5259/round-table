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
  toggleAllocator: (memberId: number) => void;
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
    toggleAllocator: (memberId: number) =>
      set(() => {
        const currentAllocators = get().allocators;
        const isMemberAllocated = currentAllocators.includes(memberId);

        if (isMemberAllocated) {
          return {
            allocators: currentAllocators.filter(
              allocator => allocator !== memberId
            ),
          };
        } else {
          return {
            allocators: [...currentAllocators, memberId],
          };
        }
      }),
  }));
