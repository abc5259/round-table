import { Time } from '../components/molecules/TimePicker/TimePicker';
import { Day } from '../components/organisms/DaySelector/DaySelector';
import { Category } from '../type/Chore';
import { ApiError } from './ApiError';
import customAxios from './Axios';
import { API_PREFIX } from './common';

function formatTime(timeObj: Time) {
  let { ampm, hour, minute } = timeObj;
  let numberHour = Number(hour);
  if (ampm === '오후' && numberHour < 12) numberHour += 12; // 오후 시간 변환
  if (ampm === '오전' && numberHour === 12) numberHour = 0; // 자정 처리

  // 시간과 분을 두 자리 숫자 형식으로 포맷팅
  let formattedHour = numberHour.toString().padStart(2, '0');

  return `${formattedHour}:${minute}`;
}

function getDayOfWeekNumber(dateString: string): number {
  const date = new Date(dateString);
  const dayOfWeek = date.getDay();
  return dayOfWeek === 0 ? 7 : dayOfWeek;
}

function getFormattedDate(date: Date): string {
  const year = date.getFullYear();
  const month = (date.getMonth() + 1).toString().padStart(2, '0'); // 월은 0부터 시작하므로 1을 더합니다.
  const day = date.getDate().toString().padStart(2, '0');

  return `${year}-${month}-${day}`;
}

export const createOneTimeSchedule = async ({
  houseId,
  name,
  date,
  time,
  allocators,
}: {
  houseId: number;
  name: string;
  date: string;
  time: Time;
  allocators: number[];
}) => {
  try {
    console.log(getDayOfWeekNumber(date));
    const res = await customAxios.post<ApiResponseType<number>>(
      `/house/${houseId}${API_PREFIX.SCHEDULE}/one-time`,
      {
        name,
        startDate: date,
        startTime: formatTime(time),
        memberIds: allocators,
      },
    );
    return res.data;
  } catch (error) {
    console.error(error);
    throw new ApiError();
  }
};

const engDays: Record<Day, string> = {
  월: 'MONDAY',
  화: 'TUESDAY',
  수: 'WEDNESDAY',
  목: 'THURSDAY',
  금: 'FRIDAY',
  토: 'SATURDAY',
  일: 'SUNDAY',
};

export const createRepeatSchedule = async ({
  houseId,
  category,
  name,
  date,
  time,
  allocators,
  divisionType,
  days,
}: {
  houseId: number;
  category: Category;
  name: string;
  date: Date;
  time: Time;
  allocators: number[];
  divisionType: string;
  days: Day[];
}) => {
  try {
    const res = await customAxios.post<ApiResponseType<number>>(
      `/house/${houseId}${API_PREFIX.SCHEDULE}/repeat`,
      {
        name,
        startDate: getFormattedDate(date),
        startTime: formatTime(time),
        divisionType: divisionType === '선택 인원 고정' ? 'FIX' : 'ROTATION',
        memberIds: allocators,
        category,
        days: days.map(d => engDays[d]),
      },
    );
    return res.data;
  } catch (error) {
    console.error(error);
    throw new ApiError();
  }
};

export interface ScheduleOfMeResponse {
  id: number;
  name: string;
  category: Category;
  isCompleted: boolean;
  startTime: string;
}

export const getSchedulesOfMeByNow = async (houseId: number) => {
  try {
    const res = await customAxios.get<
      ApiCursorBasedResponseType<ScheduleOfMeResponse[]>
    >(`/house/${houseId}${API_PREFIX.SCHEDULE}/me`);
    return res.data;
  } catch (error) {
    console.error(error);
    throw new ApiError();
  }
};

export interface ScheduleOfHouseResponse {
  id: number;
  name: string;
  category: Category;
  isCompleted: boolean;
  startTime: string;
  managers: string;
  extraManagers: string;
}

export const getSchedulesOfHouse = async (houseId: number) => {
  try {
    const res = await customAxios.get<
      ApiCursorBasedResponseType<ScheduleOfHouseResponse[]>
    >(`/house/${houseId}${API_PREFIX.SCHEDULE}`);
    return res.data;
  } catch (error) {
    console.error(error);
    throw new ApiError();
  }
};
