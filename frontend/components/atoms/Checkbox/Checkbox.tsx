import * as Styled from "./Styled";

type Props = {
  value: string;
  isChecked: boolean;
  onPressCheckBox: () => void;
};

const Checkbox = ({ value, isChecked, onPressCheckBox }: Props) => {
  return (
    <Styled.Wrapper onPress={onPressCheckBox}>
      <Styled.Box>
        <Styled.CheckIcon isChecked={isChecked}>
          <Styled.InnerCircle isChecked={isChecked} />
        </Styled.CheckIcon>
        <Styled.Text isChecked={isChecked}>{value}</Styled.Text>
      </Styled.Box>
    </Styled.Wrapper>
  );
};

export default Checkbox;
