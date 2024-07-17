import { ScrollView, View } from "react-native";
import * as Styled from "./Styled";
import useMe from "../../hooks/queries/member/useMe";
import ChoreCard from "../../components/molecules/ChoreCard/ChoreCard";

const MainScreen = () => {
  const { data: meData } = useMe();
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
          <Styled.SubTitle>{meData?.data.house.name}</Styled.SubTitle>
          <Styled.Title>안녕하세요, {meData?.data.name}님!</Styled.Title>
          <Styled.Title>하우스의 하루를 시작해볼까요?</Styled.Title>
        </View>
      </Styled.Header>
      <Styled.ContentWrapper>
        <View style={{ flexDirection: "row", gap: 10 }}>
          <Styled.ContentTitle>오늘 할 일</Styled.ContentTitle>
          <Styled.ContentTitle style={{ color: "#5BADFF" }}>
            0건
          </Styled.ContentTitle>
        </View>
        <ScrollView horizontal={true} showsHorizontalScrollIndicator={false}>
          <View>
            <ChoreCard
              isCompleted={false}
              name="test"
              category="TRASH"
              startTime="11:30"
            />
          </View>
        </ScrollView>
      </Styled.ContentWrapper>
    </Styled.Wrapper>
  );
};

export default MainScreen;
