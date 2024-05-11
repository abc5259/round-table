import { useState } from "react";
import CalendarInput from "../Input/CalendarInput";
import CalendarPickerModal from "../CalendarPickerModal/CalendarPickerModal";

const CalendarInputPickerModal = () => {
  const [dataValue, setDataValue] = useState("");
  const [isVisible, setVisible] = useState(false);

  const onChangeValue = (date: string) => {
    setDataValue(date);
  };

  return (
    <>
      <CalendarInput
        value={dataValue}
        onPressCalendar={() => setVisible(true)}
      />
      <CalendarPickerModal
        isVisible={isVisible}
        setVisible={setVisible}
        onChangeValue={onChangeValue}
      />
    </>
  );
};

export default CalendarInputPickerModal;
