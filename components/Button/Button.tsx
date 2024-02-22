import * as Styled from "./Styled";
import { PropsWithChildren } from "react";

type ButtonProps = {
  onPress?: () => void;
  isBg?: boolean;
};

const Button = ({
  children,
  onPress,
  isBg = true,
}: PropsWithChildren<ButtonProps>) => {
  return (
    <Styled.Wrapper isBg={isBg} onPress={onPress}>
      <Styled.Text>{children}</Styled.Text>
    </Styled.Wrapper>
  );
};

export default Button;
