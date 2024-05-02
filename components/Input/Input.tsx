import * as Styled from "./Styled";
import { WithLocalSvg } from "react-native-svg/css";
import CancelIcon from "../../assets/vectors/cancel.svg";

export type InputProps = {
  placeholder?: string;
  defaultValue?: string;
  value: string;
  onChange: () => void;
  onPressCancel: () => void;
  secureTextEntry?: boolean;
};

const Input = ({
  placeholder,
  defaultValue,
  value,
  onChange,
  onPressCancel,
  secureTextEntry = false,
}: InputProps) => {
  return (
    <Styled.Wrapper>
      <Styled.CustomInput
        autoCapitalize="none"
        placeholder={placeholder}
        defaultValue={defaultValue}
        value={value}
        onChangeText={onChange}
        secureTextEntry={secureTextEntry}
      />
      <Styled.SvgWrapper onPress={onPressCancel}>
        <WithLocalSvg asset={CancelIcon} />
      </Styled.SvgWrapper>
    </Styled.Wrapper>
  );
};

export default Input;
