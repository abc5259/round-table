import AppLayout from "../../../layouts/AppLayout/AppLayout";
import CreateOneTimeSchedule from "../../../components/template/CreateOneTimeSchedule/CreateOneTimeSchedule";
import { View } from "react-native";
import Button from "../../../components/atoms/Button/Button";
import { useState } from "react";

import * as Styled from "./Styled";
import { useOneTimeScheduleAppednerStore } from "../../../store/schedule/oneTimeScheduleAppenderStore";
import { createOneTimeSchele } from "../../../api/scheduleApi";
import useMe from "../../../hooks/queries/member/useMe";
import { Time } from "../../../components/molecules/TimePicker/TimePicker";

type ScheduleType = "일회성 일정" | "반복 일정";

const scheduleTypeArr: ScheduleType[] = ["일회성 일정", "반복 일정"];

const CreateScheduleScreen = () => {
  const { data: meData } = useMe();
  const { validateSubmit, name, date, time, allocators } =
    useOneTimeScheduleAppednerStore();
  const [scehduleType, setScheduleType] = useState<ScheduleType>("일회성 일정");

  const onSubmit = () => {
    if (scehduleType === "일회성 일정") {
      onSubmitOneTimeSchedule();
    } else if (scehduleType === "반복 일정") {
    }
  };

  const onSubmitOneTimeSchedule = async () => {
    const result = validateSubmit();
    if (!result.success) {
      alert(result.message);
      return;
    }

    if (!meData) {
      alert("다시 시도해 주세요");
      return;
    }

    const houseId = meData.data.house.houseId;
    const res = await createOneTimeSchele({
      houseId,
      name,
      date,
      time,
      allocators,
    } as {
      houseId: number;
      name: string;
      time: Time;
      date: string;
      allocators: number[];
    });
    if (!res.success) {
      alert(res.message);
      return;
    }

    alert("스케줄 생성 완료");
  };

  return (
    <AppLayout>
      <View
        style={{
          gap: 10,
          flexDirection: "row",
          marginBottom: 20,
          marginTop: -10,
        }}
      >
        {scheduleTypeArr.map(v => (
          <Styled.ToggleButton
            key={v}
            onPress={() => setScheduleType(v)}
            isActive={v === scehduleType}
          >
            <Styled.ToggleText isActive={v === scehduleType}>
              {v}
            </Styled.ToggleText>
          </Styled.ToggleButton>
        ))}
      </View>
      {scehduleType === "일회성 일정" && <CreateOneTimeSchedule />}
      <Button onPress={onSubmit}>완료</Button>
    </AppLayout>
  );
};

export default CreateScheduleScreen;
