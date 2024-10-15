import Input, { InputProps } from "../../atoms/Input/Input";
import Label, { LabelProps } from "../../atoms/Label/Label";
import * as Styled from "./Styled";

type Props = {
  labelProps: LabelProps;
  inputProps: InputProps;
  errorMessage?: String;
};

const LabelInput = ({ labelProps, inputProps, errorMessage }: Props) => {
  return (
    <Styled.Wrapper>
      <Label {...labelProps} />
      <Input {...inputProps} />
      {errorMessage && (
        <Styled.ErrorMessage>{errorMessage}</Styled.ErrorMessage>
      )}
    </Styled.Wrapper>
  );
};

export default LabelInput;
