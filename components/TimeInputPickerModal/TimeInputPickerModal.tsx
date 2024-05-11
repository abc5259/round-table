import { useState } from "react";
import TimeInput from "../Input/TimeInput";
import { Time } from "../TimePicker/TimePicker";
import TimePickerModal from "../TimePickerModal/TimePcikerModal";

const TimeInputPickerModal = () => {
  const [time, setTime] = useState<Time | null>(null);
  const [isVisible, setVisible] = useState(false);

  const onChangeTime = (time: Time) => {
    setTime(time);
  };

  return (
    <>
      <TimeInput
        defaultValue=""
        value={time ? `${time.ampm} ${time.hour}시 ${time.minute}분` : ""}
        onPressTime={() => setVisible(true)}
      />
      <TimePickerModal
        isVisible={isVisible}
        setVisible={setVisible}
        onChangeValue={onChangeTime}
      />
    </>
  );
};

export default TimeInputPickerModal;
