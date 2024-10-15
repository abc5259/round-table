import { View } from "react-native";
import * as Styled from "./Styled";
import useMe from "../../hooks/queries/member/useMe";
import ChoreCards from "../../components/organisms/ChoreCards/ChoreCards";
import ChoreOfHouseCards from "../../components/organisms/ChoresOfHouseCards/ChoreOfHouseCards";
import useGetMySchedules from "../../hooks/queries/chore/useGetMyScgedules";

const MainScreen = () => {
  const { data: meData } = useMe();
  const { data } = useGetMySchedules();
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
          <Styled.SubTitle>{meData?.data?.house.name} 하우스</Styled.SubTitle>
          <Styled.Title>안녕하세요, {meData?.data?.name}님!</Styled.Title>
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
