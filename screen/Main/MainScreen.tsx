import { ScrollView, StyleSheet, Text, View } from "react-native";
import * as Styled from "./Styled";
import useMe from "../../hooks/queries/member/useMe";
import ChoreCard from "../../components/molecules/ChoreCard/ChoreCard";
import { getChoresOfMeByNow } from "../../api/choreApi";
import useGetMyChores from "../../hooks/queries/chore/useGetMyChores";
import { WithLocalSvg } from "react-native-svg/css";

const MainScreen = () => {
  const { data: meData } = useMe();
  const { data: myChoreData } = useGetMyChores();

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
          {myChoreData && myChoreData?.data.length > 0 ? (
            myChoreData?.data.map(chore => (
              <View key={chore.choreId} style={{ marginRight: 20 }}>
                <ChoreCard
                  isCompleted={chore.isCompleted}
                  name={chore.name}
                  category={chore.category}
                  startTime={chore.startTime}
                />
              </View>
            ))
          ) : (
            <Styled.Card style={styles.card}>
              <View style={{ alignItems: "center", gap: 10 }}>
                <WithLocalSvg
                  asset={require("../../assets/vectors/smile.svg")}
                />
                <Text
                  style={{ color: "#9CA3AB", fontWeight: "bold", fontSize: 14 }}
                >
                  오늘 먹을 알약이 없어요!
                </Text>
              </View>
            </Styled.Card>
          )}
        </ScrollView>
      </Styled.ContentWrapper>
    </Styled.Wrapper>
  );
};

const styles = StyleSheet.create({
  card: {
    shadowColor: "#000",
    shadowOffset: {
      width: 0,
      height: 0.8,
    },
    shadowOpacity: 0.2,
    shadowRadius: 1.2,
  },
});

export default MainScreen;
