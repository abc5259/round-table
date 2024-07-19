import { View } from "react-native";
import * as Styled from "./Styled";
import ChoreOfHousCard from "../../molecules/ChoreOfHousCard/ChoreOfHousCard";
import { formatDateToKorean } from "../../../util/formatDateToKorean";

const ChoreOfHouseCards = () => {
  return (
    <Styled.Wrapper>
      <View
        style={{
          flexDirection: "row",
          gap: 10,
          alignItems: "center",
          justifyContent: "space-between",
        }}
      >
        <Styled.Title>하우스 할 일 현황</Styled.Title>
        <Styled.SubTitle>{formatDateToKorean(new Date())}</Styled.SubTitle>
      </View>
      {/* <Styled.NoContentWrapper>
        <Styled.ContentTitle>하우스의 휴일인가요?</Styled.ContentTitle>
        <Styled.ContentTitle>오늘은 할 일이 없어요!</Styled.ContentTitle>
      </Styled.NoContentWrapper> */}
      <ChoreOfHousCard
        isCompleted={false}
        scheduleName="분리수거"
        time="13:30"
        allocatorNames={["재훈", "쏘냥"]}
      />
    </Styled.Wrapper>
  );
};

export default ChoreOfHouseCards;
