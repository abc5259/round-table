import { ViewStyle } from "react-native";
import * as Styled from "./Styled";

export type LabelProps = { text: string; style?: ViewStyle };

const Label = ({ text, style }: LabelProps) => {
  return (
    <Styled.Wrapper style={style}>
      <Styled.LabelText>{text}</Styled.LabelText>
    </Styled.Wrapper>
  );
};

export default Label;
