import * as Styled from "./Styled";
import { WithLocalSvg } from "react-native-svg/css";

export type CalendarInputProps = {
  placeholder?: string;
  defaultValue?: string;
  value: string;
  onPressCalendar: () => void;
};

const CalendarInput = ({
  placeholder,
  defaultValue,
  value,
  onPressCalendar,
}: CalendarInputProps) => {
  return (
    <Styled.Wrapper>
      <Styled.CustomInput
        autoCapitalize="none"
        placeholder={placeholder || "날짜를 선택해주세요"}
        defaultValue={defaultValue}
        value={value}
        editable={false}
      />
      <Styled.SvgWrapper onPress={onPressCalendar} style={{ top: 20 }}>
        <WithLocalSvg asset={require("../../../assets/vectors/calendar.svg")} />
      </Styled.SvgWrapper>
    </Styled.Wrapper>
  );
};

export default CalendarInput;
