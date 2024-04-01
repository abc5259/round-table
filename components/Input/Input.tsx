import * as Styled from "./Styled";
import { WithLocalSvg } from "react-native-svg";
import CancelIcon from "../../assets/vectors/cancel.svg";

export type InputProps = {
  placeholder?: string;
  defaultValue?: string;
  value: string;
  onChange: () => void;
  onPressCancel: () => void;
};

const Input = ({
  placeholder,
  defaultValue,
  value,
  onChange,
  onPressCancel,
}: InputProps) => {
  return (
    <Styled.Wrapper>
      <Styled.CustomInput
        autoCapitalize="none"
        placeholder={placeholder}
        defaultValue={defaultValue}
        value={value}
        onChangeText={onChange}
      />
      <Styled.SvgWrapper onPress={onPressCancel}>
        <WithLocalSvg asset={CancelIcon} />
      </Styled.SvgWrapper>
    </Styled.Wrapper>
  );
};

export default Input;
