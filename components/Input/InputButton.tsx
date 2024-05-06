import * as Styled from "./Styled";
import { WithLocalSvg } from "react-native-svg/css";
import CancelIcon from "../../assets/vectors/cancel.svg";
import { View } from "react-native";
import Button from "../Button/Button";

export type InputButtonProps = {
  placeholder?: string;
  defaultValue?: string;
  value: string;
  onChange: (text: string) => void;
  onPressCancel: () => void;
  onPressButton: () => void;
  secureTextEntry?: boolean;
  errorMessage?: string;
  successMessage?: string;
};

const InputButton = ({
  placeholder,
  defaultValue,
  value,
  onChange,
  onPressCancel,
  secureTextEntry = false,
  onPressButton,
  errorMessage,
  successMessage,
}: InputButtonProps) => {
  return (
    <View>
      <Styled.Wrapper>
        <View style={{ flex: 0.8, width: "100%" }}>
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
        </View>
        <Button
          onPress={onPressButton}
          wrapperStyle={{
            backgroundColor: "#EFF5FE",
            flex: 0.2,
            height: 48,
            paddingTop: 0,
            paddingBottom: 0,
          }}
          textStyled={{ color: "#3F4245" }}
        >
          확인
        </Button>
      </Styled.Wrapper>
      {successMessage != null && errorMessage && (
        <Styled.ErrorMessage>{errorMessage}</Styled.ErrorMessage>
      )}
      {successMessage && (
        <Styled.SuccessMessage>{successMessage}</Styled.SuccessMessage>
      )}
    </View>
  );
};

export default InputButton;
