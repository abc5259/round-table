import { TextStyle, ViewStyle } from "react-native";
import * as Styled from "./Styled";

export type LabelProps = {
  text: string;
  style?: ViewStyle;
  textStyle?: TextStyle;
};

const Label = ({ text, style, textStyle }: LabelProps) => {
  return (
    <Styled.Wrapper style={style}>
      <Styled.LabelText style={textStyle}>{text}</Styled.LabelText>
    </Styled.Wrapper>
  );
};

export default Label;
