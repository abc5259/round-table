import { WithLocalSvg } from "react-native-svg/css";
import { useNavigation } from "@react-navigation/native";
import ArrowLeft from "../../assets/vectors/arrow-left.svg";
import * as Styled from "./Styled";

type ButtonProps = {
  title: string;
};

const Header = ({ title }: ButtonProps) => {
  const navigation = useNavigation();
  const onPressArrow = () => {
    navigation.goBack();
  };
  return (
    <Styled.Wrapper>
      <Styled.SvgWrapper onPress={onPressArrow}>
        <WithLocalSvg asset={ArrowLeft} />
      </Styled.SvgWrapper>
      <Styled.TitleWrapper>
        <Styled.Title>{title}</Styled.Title>
      </Styled.TitleWrapper>
      <Styled.Item></Styled.Item>
    </Styled.Wrapper>
  );
};

export default Header;
