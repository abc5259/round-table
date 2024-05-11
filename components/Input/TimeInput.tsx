import * as Styled from "./Styled";
import { WithLocalSvg } from "react-native-svg/css";
import TimeIcon from "../../assets/vectors/time.svg";

export type TimeInputProps = {
  placeholder?: string;
  defaultValue?: string;
  value: string;
  onPressTime: () => void;
};

const TimeInput = ({
  placeholder,
  defaultValue,
  value,
  onPressTime,
}: TimeInputProps) => {
  return (
    <Styled.Wrapper>
      <Styled.CustomInput
        autoCapitalize="none"
        placeholder={placeholder || "시간을 선택해주세요"}
        defaultValue={defaultValue}
        value={value}
        editable={false}
      />
      <Styled.SvgWrapper onPress={onPressTime} style={{ top: 20 }}>
        <WithLocalSvg asset={TimeIcon} />
      </Styled.SvgWrapper>
    </Styled.Wrapper>
  );
};

export default TimeInput;
