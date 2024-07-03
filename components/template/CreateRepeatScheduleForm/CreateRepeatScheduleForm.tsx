import { ActivityIndicator, View } from "react-native";
import Label from "../../atoms/Label/Label";
import CategorySelector, {
  Category,
} from "../../organisms/CategorySelector/CategorySelector";
import * as Styled from "./Styled";
import LabelInput from "../../molecules/LabelInput/LabelInput";
import { useRepeatScheduleAppednerStore } from "../../../store/schedule/repeateScheduleAppenderStore";
import Profile from "../../atoms/Profile/Profile";
import useHouseMembers from "../../../hooks/queries/house/useHouseMembers";
import TimeInputPickerModal from "../../organisms/TimeInputPickerModal/TimeInputPickerModal";
import DaySelector from "../../organisms/DaySelector/DaySelector";

const CreateRepeatScheduleForm = () => {
  const { data: houseMemberData, isLoading } = useHouseMembers();
  const {
    category,
    name,
    time,
    allocators,
    changeCategory,
    changeName,
    changeTime,
    toggleAllocator,
    changeDays,
  } = useRepeatScheduleAppednerStore();

  return (
    <Styled.Wrapper showsVerticalScrollIndicator={false}>
      <View style={{ gap: 30 }}>
        <View style={{ gap: 20 }}>
          <Label
            textStyle={{ fontSize: 17, fontWeight: "bold" }}
            text="카테고리를 선택해주세요"
          />
          <CategorySelector changeCategory={changeCategory} />
        </View>
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
        <DaySelector changeDays={changeDays} />
        <View style={{ gap: 10 }}>
          <TimeInputPickerModal onChangeValue={changeTime} />
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
      </View>
    </Styled.Wrapper>
  );
};

export default CreateRepeatScheduleForm;
