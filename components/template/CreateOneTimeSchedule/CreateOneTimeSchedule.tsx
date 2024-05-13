import { View } from "react-native";
import Label from "../../atoms/Label/Label";
import CalendarInputPickerModal from "../../organisms/CalendarInputPickerModal/CalendarInputPickerModal";
import TimeInputPickerModal from "../../organisms/TimeInputPickerModal/TimeInputPickerModal";
import * as Styled from "./Styled";
import LabelInput from "../../molecules/LabelInput/LabelInput";
import { useOneTimeScheduleAppednerStore } from "../../../store/schedule/oneTimeScheduleAppenderStore";
import Profile from "../../atoms/Profile/Profile";

const CreateOneTimeSchedule = () => {
  const { name, time, date, changeName, changeDate, changeTime } =
    useOneTimeScheduleAppednerStore();

  console.log(name, date, time);

  return (
    <Styled.Wrapper>
      <View style={{ gap: 20 }}>
        <LabelInput
          labelProps={{
            text: "어떤 이벤트인가요?",
            textStyle: { fontSize: 17, fontWeight: "bold" },
          }}
          inputProps={{
            value: name,
            defaultValue: "",
            onChange: changeName,
            onPressCancel: () => changeName(""),
          }}
        />
      </View>
      <View style={{ gap: 20 }}>
        <Label
          textStyle={{ fontSize: 17, fontWeight: "bold" }}
          text="언제 실행 예정인가요?"
        />
        <View style={{ gap: 10 }}>
          <CalendarInputPickerModal onChange={changeDate} />
          <TimeInputPickerModal onChangeValue={changeTime} />
        </View>
      </View>
      <View style={{ gap: 20 }}>
        <Label
          textStyle={{ fontSize: 17, fontWeight: "bold" }}
          text="담당자를 선택해주세요!"
        />
        <View>
          <Profile
            url="https://reactnative.dev/img/tiny_logo.png"
            name="지수"
          />
        </View>
      </View>
    </Styled.Wrapper>
  );
};

export default CreateOneTimeSchedule;
