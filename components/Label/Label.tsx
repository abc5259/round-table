import * as Styled from "./Styled";

export type LabelProps = { text: string };

const Label = ({ text }: LabelProps) => {
  return (
    <Styled.Wrapper>
      <Styled.LabelText>{text}</Styled.LabelText>
    </Styled.Wrapper>
  );
};

export default Label;
