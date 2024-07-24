import { useState } from "react";
import { View } from "react-native";
import Label from "../../atoms/Label/Label";
import * as Styled from "./Styled";
import { WithLocalSvg } from "react-native-svg/css";
import ModalSelector from "../../molecules/ModalSelector/ModalSelector";

type Props = {
  labelText: string;
  selectedValue: string;
  modalTitle: string;
  values: string[];
  onChange: (value: string) => void;
};

const Select = ({
  labelText,
  selectedValue,
  modalTitle,
  values,
  onChange,
}: Props) => {
  const [visible, setVisible] = useState(false);

  return (
    <>
      <View style={{ gap: 10 }}>
        <Label
          text={labelText}
          textStyle={{ fontSize: 17, fontWeight: "bold" }}
        />
        <Styled.Wrapper onPress={() => setVisible(true)}>
          <Styled.Text>{selectedValue}</Styled.Text>
          <Styled.SvgWrapper>
            <WithLocalSvg
              asset={require("../../../assets/vectors/arrow-right.svg")}
            />
          </Styled.SvgWrapper>
        </Styled.Wrapper>
      </View>
      <ModalSelector
        title={modalTitle}
        values={values}
        isVisible={visible}
        setVisible={setVisible}
        defaultValue={selectedValue}
        onChange={onChange}
      />
    </>
  );
};

export default Select;
