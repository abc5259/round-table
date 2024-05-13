import { Time } from "../components/molecules/TimePicker/TimePicker";
import { ApiError } from "./ApiError";
import customAxios from "./Axios";
import { API_PREFIX } from "./common";

function formatTime(timeObj: Time) {
  let { ampm, hour, minute } = timeObj;
  let numberHour = Number(hour);
  if (ampm === "오후" && numberHour < 12) numberHour += 12; // 오후 시간 변환
  if (ampm === "오전" && numberHour === 12) numberHour = 0; // 자정 처리

  // 시간과 분을 두 자리 숫자 형식으로 포맷팅
  let formattedHour = numberHour.toString().padStart(2, "0");

  return `${formattedHour}:${minute}`;
}

export const createOneTimeSchele = async ({
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
    const res = await customAxios.post<ApiResponseType<number>>(
      `${API_PREFIX.SCHEDULE}/house/${houseId}`,
      {
        name,
        frequencyType: "ONCE",
        frequencyInterval: 0,
        startDate: date,
        startTime: formatTime(time),
        divisionType: "FIX",
        memberIds: allocators,
        category: "ONE_TIME",
      }
    );
    return res.data;
  } catch (error) {
    console.error(error);
    throw new ApiError();
  }
};
