import * as Styled from "./Styled";

type TopBottomLayoutProps = {
  topText: string;
  bottomText?: string;
  inputs: React.JSX.Element;
  button: React.JSX.Element;
};

const TopBottomLayout = ({
  topText,
  bottomText,
  inputs,
  button,
}: TopBottomLayoutProps) => {
  return (
    <Styled.Wrapper>
      <Styled.Top>
        <Styled.H1>{topText}</Styled.H1>
        {bottomText && <Styled.H1 isBottom>{bottomText}</Styled.H1>}
      </Styled.Top>
      <Styled.InputContainer>{inputs}</Styled.InputContainer>
      {button}
    </Styled.Wrapper>
  );
};

export default TopBottomLayout;
