import { create } from "zustand";
import { Time } from "../../components/molecules/TimePicker/TimePicker";
import { Category } from "../../components/organisms/CategorySelector/CategorySelector";
import { Day } from "../../components/organisms/DaySelector/DaySelector";

type RepeatScheduleAppenderStoreType = {
  category: Category | null;
  name: string;
  time: Time | null;
  days: Day[];
  allocators: number[];
  divisionType: string;
  changeCategory: (category: Category | null) => void;
  changeName: (name: string) => void;
  changeTime: (time: Time) => void;
  changeDivisionType: (divisionType: string) => void;
  toggleAllocator: (memberId: number) => void;
  changeDays: (days: Day[]) => void;
  validateSubmit: () => { success: boolean; message?: string };
};

export const useRepeatScheduleAppednerStore =
  create<RepeatScheduleAppenderStoreType>((set, get) => ({
    category: null,
    name: "",
    time: null,
    allocators: [],
    days: [],
    divisionType: "선택 인원 고정",
    changeCategory: (category: Category | null) => set(() => ({ category })),
    changeName: (name: string) => set(() => ({ name })),
    changeTime: (time: Time) => set(() => ({ time })),
    changeDays: (days: Day[]) => set(() => ({ days })),
    changeDivisionType: (divisionType: string) => set(() => ({ divisionType })),
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
      if (get().category == null) {
        return { success: false, message: "카테고리는 필수입니다." };
      }

      if (get().name == null || get().name.trim().length === 0) {
        return { success: false, message: "이벤트 이름은 필수입니다." };
      }

      if (get().days.length === 0) {
        return { success: false, message: "반복 요일은 필수입니다." };
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
