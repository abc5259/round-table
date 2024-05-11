import { useState } from "react";
import TimeInput from "../../atoms/Input/TimeInput";
import { Time } from "../../molecules/TimePicker/TimePicker";
import TimePickerModal from "../../molecules/TimePickerModal/TimePcikerModal";

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
