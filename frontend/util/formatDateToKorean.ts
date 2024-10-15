export function formatDateToKorean(date: Date): string {
  const daysOfWeek = [
    "일요일",
    "월요일",
    "화요일",
    "수요일",
    "목요일",
    "금요일",
    "토요일",
  ];
  const months = [
    "1월",
    "2월",
    "3월",
    "4월",
    "5월",
    "6월",
    "7월",
    "8월",
    "9월",
    "10월",
    "11월",
    "12월",
  ];

  const day = date.getDate();
  const dayOfWeek = daysOfWeek[date.getDay()];
  const month = months[date.getMonth()];

  return `${month} ${day}일 ${dayOfWeek}`;
}
