import AppLayout from "../../../layouts/AppLayout/AppLayout";
import { useState } from "react";
import CalendarInput from "../../../components/Input/CalendarInput";
import CalendarPicker from "../../../components/CalendarPickerModal/CalendarPickerModal";
import TimePicker, { Time } from "../../../components/TimePicker/TimePicker";
import TimePickerModal from "../../../components/TimePickerModal/TimePcikerModal";
import CalendarInputPickerModal from "../../../components/CalendarInputPickerModal/CalendarInputPickerModal";
import TimeInputPickerModal from "../../../components/TimeInputPickerModal/TimeInputPickerModal";

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
      <CalendarInputPickerModal />
      <TimeInputPickerModal />
    </AppLayout>
  );
};

export default CreateScheduleScreen;
