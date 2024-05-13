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
  validateSubmit: () => { success: boolean; message?: string };
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

    validateSubmit: () => {
      if (get().name == null || get().name.trim().length === 0) {
        return { success: false, message: "이벤트 이름은 필수입니다." };
      }

      if (get().date == null || get().date.trim().length === 0) {
        return { success: false, message: "실행 날짜는 필수입니다." };
      }

      if (get().time == null) {
        return { success: false, message: "실행 시간은 필수입니다." };
      }

      if (get().allocators.length === 0) {
        return { success: false, message: "담당자를 선택해주세요." };
      }
      return { success: true };
    },
  }));
