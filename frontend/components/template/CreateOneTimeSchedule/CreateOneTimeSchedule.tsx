import { ActivityIndicator, View } from "react-native";
import Label from "../../atoms/Label/Label";
import CalendarInputPickerModal from "../../organisms/CalendarInputPickerModal/CalendarInputPickerModal";
import TimeInputPickerModal from "../../organisms/TimeInputPickerModal/TimeInputPickerModal";
import * as Styled from "./Styled";
import LabelInput from "../../molecules/LabelInput/LabelInput";
import { useOneTimeScheduleAppednerStore } from "../../../store/schedule/oneTimeScheduleAppenderStore";
import Profile from "../../atoms/Profile/Profile";
import useHouseMembers from "../../../hooks/queries/house/useHouseMembers";
import Button from "../../atoms/Button/Button";

const CreateOneTimeSchedule = () => {
  const { data: houseMemberData, isLoading } = useHouseMembers();
  const {
    name,
    time,
    date,
    allocators,
    changeName,
    changeDate,
    changeTime,
    toggleAllocator,
  } = useOneTimeScheduleAppednerStore();

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
            placeholder: "이벤트 이름을 입력해주세요",
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
        <View style={{ flexDirection: "row", gap: 20 }}>
          {isLoading ? (
            <ActivityIndicator />
          ) : (
            houseMemberData?.data.map(houseMember => (
              <Profile
                key={houseMember.memberId}
                url={houseMember.profileUrl}
                name={houseMember.name}
                isSelected={allocators.includes(houseMember.memberId)}
                onPressContainer={() => toggleAllocator(houseMember.memberId)}
              />
            ))
          )}
        </View>
      </View>
    </Styled.Wrapper>
  );
};

export default CreateOneTimeSchedule;
