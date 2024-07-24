import { useState } from "react";
import Checkbox from "../../atoms/\bCheckbox/\bCheckbox";
import * as Styled from "./Styled";
import ReactNativeModal from "react-native-modal";
import Button from "../../atoms/Button/Button";

type Props = {
  isVisible: boolean;
  setVisible: (visible: boolean) => void;
  defaultValue: string;
  onChange: (value: string) => void;
  title: string;
  values: string[];
};

const ModalSelector = ({
  isVisible,
  setVisible,
  defaultValue,
  onChange,
  title,
  values,
}: Props) => {
  const [selectValue, setSelectValue] = useState(defaultValue);

  const onPressCheckBox = (value: string) => {
    setSelectValue(value);
  };

  const onPressOkButton = () => {
    if (onChange !== null) onChange(selectValue);
    setVisible(false);
  };
  return (
    <ReactNativeModal
      animationOutTiming={600}
      backdropTransitionOutTiming={600}
      isVisible={isVisible}
      style={{ padding: 0, margin: 0 }}
      onBackdropPress={() => setVisible(false)}
      onBackButtonPress={() => setVisible(false)}
    >
      <Styled.Wrapper>
        <Styled.Title>{title}</Styled.Title>
        <Styled.CheckboxWrapper>
          {values.map(value => (
            <Checkbox
              key={value}
              value={value}
              isChecked={selectValue === value}
              onPressCheckBox={() => onPressCheckBox(value)}
            />
          ))}
        </Styled.CheckboxWrapper>
        <Button onPress={onPressOkButton}>선택 완료</Button>
      </Styled.Wrapper>
    </ReactNativeModal>
  );
};

export default ModalSelector;
