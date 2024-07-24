import { useState } from "react";
import CalendarInput from "../../atoms/Input/CalendarInput";
import CalendarPickerModal from "../../molecules/CalendarPickerModal/CalendarPickerModal";

type Props = {
  onChange: (date: string) => void;
};

const CalendarInputPickerModal = ({ onChange }: Props) => {
  const [dataValue, setDataValue] = useState("");
  const [isVisible, setVisible] = useState(false);

  const onChangeValue = (date: string) => {
    setDataValue(date);
    onChange(date);
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
