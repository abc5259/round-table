import { View } from "react-native";
import * as Styled from "./Styled";
import useMe from "../../hooks/queries/member/useMe";
import ChoreCards from "../../components/organisms/ChoreCards/ChoreCards";
import useGetChoresOfHouse from "../../hooks/queries/chore/useGetChoresOfHouse";
import { WithLocalSvg } from "react-native-svg/css";
import ChoreOfHouseCards from "../../components/organisms/ChoresOfHouseCards/ChoreOfHouseCards";

const MainScreen = () => {
  const { data: meData } = useMe();
  const { data } = useGetChoresOfHouse();
  console.log(data);
  return (
    <Styled.Wrapper>
      <Styled.Header
        style={{
          paddingLeft: 20,
          paddingTop: 20,
          paddingBottom: 20,
          backgroundColor: "#FFFFFF",
        }}
      >
        <View style={{ gap: 6 }}>
          <Styled.SubTitle>{meData?.data.house.name} 하우스</Styled.SubTitle>
          <Styled.Title>안녕하세요, {meData?.data.name}님!</Styled.Title>
          <Styled.Title>하우스의 하루를 시작해볼까요?</Styled.Title>
        </View>
      </Styled.Header>
      <Styled.ContentWrapper>
        <ChoreCards />
        <Styled.Bar></Styled.Bar>
        <ChoreOfHouseCards />
      </Styled.ContentWrapper>
    </Styled.Wrapper>
  );
};

export default MainScreen;
