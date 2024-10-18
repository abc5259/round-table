import AppLayout from '../../../layouts/AppLayout/AppLayout';
import CreateOneTimeSchedule from '../../../components/template/CreateOneTimeSchedule/CreateOneTimeSchedule';
import { View } from 'react-native';
import Button from '../../../components/atoms/Button/Button';
import { useState } from 'react';

import * as Styled from './Styled';
import { useOneTimeScheduleAppednerStore } from '../../../store/schedule/oneTimeScheduleAppenderStore';
import {
  createOneTimeSchedule,
  createRepeatSchedule,
} from '../../../api/scheduleApi';
import useMe from '../../../hooks/queries/member/useMe';
import { Time } from '../../../components/molecules/TimePicker/TimePicker';
import CreateRepeatScheduleForm from '../../../components/template/CreateRepeatScheduleForm/CreateRepeatScheduleForm';
import { useRepeatScheduleAppednerStore } from '../../../store/schedule/repeateScheduleAppenderStore';
import { Day } from '../../../components/organisms/DaySelector/DaySelector';
import { RepeateCategory } from '../../../type/Chore';

type ScheduleType = '일회성 일정' | '반복 일정';

const scheduleTypeArr: ScheduleType[] = ['일회성 일정', '반복 일정'];

const CreateScheduleScreen = () => {
  const { data: meData } = useMe();
  const { validateSubmit, name, date, time, allocators } =
    useOneTimeScheduleAppednerStore();
  const {
    validateSubmit: validateRepeatSubmit,
    category,
    name: repeatName,
    time: repeatTime,
    allocators: repeatAllocators,
    days,
    divisionType,
  } = useRepeatScheduleAppednerStore();
  const [scehduleType, setScheduleType] = useState<ScheduleType>('일회성 일정');

  const onSubmit = () => {
    if (scehduleType === '일회성 일정') {
      onSubmitOneTimeSchedule();
    } else if (scehduleType === '반복 일정') {
      onSubmitRepeatSchedule();
    }
  };

  const onSubmitOneTimeSchedule = async () => {
    const result = validateSubmit();
    if (!result.success) {
      alert(result.message);
      return;
    }

    if (!meData) {
      alert('다시 시도해 주세요');
      return;
    }

    const houseId = meData.data.house.houseId;
    const res = await createOneTimeSchedule({
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

    alert('스케줄 생성 완료');
  };

  const onSubmitRepeatSchedule = async () => {
    const result = validateRepeatSubmit();
    if (!result.success) {
      alert(result.message);
      return;
    }

    if (!meData) {
      alert('다시 시도해 주세요');
      return;
    }

    console.log(
      category,
      repeatName,
      repeatTime,
      repeatAllocators,
      days,
      divisionType,
    );

    const houseId = meData.data.house.houseId;
    const res = await createRepeatSchedule({
      houseId,
      category,
      name: repeatName,
      date: new Date(),
      time: repeatTime,
      allocators: repeatAllocators,
      divisionType,
      days,
    } as {
      houseId: number;
      category: RepeateCategory;
      name: string;
      date: Date;
      time: Time;
      allocators: number[];
      divisionType: string;
      days: Day[];
    });
    if (!res.success) {
      alert(res.message);
      return;
    }

    alert('스케줄 생성 완료');
  };

  return (
    <AppLayout>
      <View
        style={{
          gap: 10,
          flexDirection: 'row',
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
      {scehduleType === '일회성 일정' && <CreateOneTimeSchedule />}
      {scehduleType === '반복 일정' && <CreateRepeatScheduleForm />}
      <Button onPress={onSubmit}>완료</Button>
    </AppLayout>
  );
};

export default CreateScheduleScreen;
