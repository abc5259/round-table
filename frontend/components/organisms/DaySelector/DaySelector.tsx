import { Pressable, Text, View } from "react-native";
import Label from "../../atoms/Label/Label";
import * as Styled from "./Styled";
import { useState } from "react";

export type Day = "월" | "화" | "수" | "목" | "금" | "토" | "일";

const days: Day[] = ["월", "화", "수", "목", "금", "토", "일"];

type Props = {
  changeDays: (days: Day[]) => void;
};

const DaySelector = ({ changeDays }: Props) => {
  const [selectedDays, setSelectedDays] = useState<Day[]>([]);
  const onPressDay = (day: Day) => {
    if (selectedDays.includes(day)) {
      const newDay = selectedDays.filter(d => d !== day);
      changeDays(newDay);
      setSelectedDays(newDay);
      return;
    }
    changeDays([...selectedDays, day]);
    setSelectedDays(v => [...v, day]);
  };
  return (
    <Styled.Wrapper>
      <Label
        textStyle={{ fontSize: 17, fontWeight: "bold" }}
        text={"반복할 요일을 선택해주세요!"}
      />
      <View
        style={{
          flexDirection: "row",
          gap: 10,
          justifyContent: "space-between",
        }}
      >
        {days.map(day => (
          <Pressable
            onPress={() => onPressDay(day)}
            key={day}
            style={{
              width: 40,
              height: 40,
              borderRadius: 50,
              justifyContent: "center",
              alignItems: "center",
              backgroundColor: selectedDays.includes(day) ? "#5BADFF" : "#FFF",
            }}
          >
            <Text
              style={{
                fontSize: 15,
                color: selectedDays.includes(day) ? "#FFFFFF" : "#A7ADBD",
                fontWeight: "bold",
              }}
            >
              {day}
            </Text>
          </Pressable>
        ))}
      </View>
    </Styled.Wrapper>
  );
};

export default DaySelector;
