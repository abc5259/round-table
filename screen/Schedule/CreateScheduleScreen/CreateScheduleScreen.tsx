import AppLayout from "../../../layouts/AppLayout/AppLayout";
import { useState } from "react";
import CalendarInput from "../../../components/atoms/Input/CalendarInput";
import CalendarPicker from "../../../components/molecules/CalendarPickerModal/CalendarPickerModal";
import TimePicker, {
  Time,
} from "../../../components/molecules/TimePicker/TimePicker";
import TimePickerModal from "../../../components/molecules/TimePickerModal/TimePcikerModal";
import CalendarInputPickerModal from "../../../components/organisms/CalendarInputPickerModal/CalendarInputPickerModal";
import TimeInputPickerModal from "../../../components/organisms/TimeInputPickerModal/TimeInputPickerModal";

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
