import { TextStyle, ViewStyle } from "react-native";
import * as Styled from "./Styled";
import { PropsWithChildren } from "react";

type ButtonProps = {
  onPress?: () => void;
  isBg?: boolean;
  wrapperStyle?: ViewStyle;
  textStyled?: TextStyle;
};

const Button = ({
  children,
  onPress,
  isBg = true,
  wrapperStyle,
  textStyled,
}: PropsWithChildren<ButtonProps>) => {
  return (
    <Styled.Wrapper isBg={isBg} onPress={onPress} style={wrapperStyle}>
      <Styled.Text style={textStyled}>{children}</Styled.Text>
    </Styled.Wrapper>
  );
};

export default Button;
