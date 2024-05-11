import AppLayout from "../../../layouts/AppLayout/AppLayout";
import { useState } from "react";
import CalendarInput from "../../../components/Input/CalendarInput";
import CalendarPicker from "../../../components/CalendarPickerModal/CalendarPickerModal";
import TimePicker, { Time } from "../../../components/TimePicker/TimePicker";
import TimePickerModal from "../../../components/TimePickerModal/TimePcikerModal";

const CreateScheduleScreen = () => {
  const [dataValue, setDataValue] = useState("");
  const [isVisible, setVisible] = useState(false);
  const onChangeValue = (date: string) => {
    setDataValue(date);
  };

  const onChangeTime = (time: Time) => {
    console.log(time);
  };
  return (
    <AppLayout>
      <CalendarInput
        value={dataValue}
        onPressCalendar={() => setVisible(true)}
      />
      <TimePickerModal
        isVisible={isVisible}
        setVisible={setVisible}
        onChangeValue={onChangeTime}
      />
      <TimePicker
        itemHeight={36}
        onTimeChange={time => {
          console.log(time);
        }}
      />
    </AppLayout>
  );
};

export default CreateScheduleScreen;
