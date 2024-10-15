import { useState } from "react";
import Button from "../../atoms/Button/Button";
import * as Styled from "./Styled";
import Label from "../../atoms/Label/Label";

type SelectButtonProps = {
  texts: string[];
  defulatValue: string;
  onChange?: (text: string) => void;
  labelText?: string;
};

const SelectButton = ({
  texts,
  onChange,
  defulatValue,
  labelText,
}: SelectButtonProps) => {
  const [selected, setSelected] = useState(defulatValue);
  const onClickButton = (text: string) => {
    if (onChange != null) {
      onChange(text);
    }
    setSelected(text);
  };
  return (
    <Styled.Wrapper>
      {labelText && <Label text={labelText} />}
      <Styled.RowFlex>
        {texts.map((text, idx) => (
          <Styled.ButtonWrapper key={idx}>
            <Button
              onPress={() => onClickButton(text)}
              wrapperStyle={{
                backgroundColor: text === selected ? "#69b4ff" : "#EFF5FE",
              }}
              textStyled={{
                color: text === selected ? "#fff" : "#3F4245",
              }}
            >
              {text}
            </Button>
          </Styled.ButtonWrapper>
        ))}
      </Styled.RowFlex>
    </Styled.Wrapper>
  );
};

export default SelectButton;
