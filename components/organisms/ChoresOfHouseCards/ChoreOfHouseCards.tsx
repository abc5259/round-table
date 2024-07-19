import { View } from "react-native";
import * as Styled from "./Styled";
import ChoreOfHousCard from "../../molecules/ChoreOfHousCard/ChoreOfHousCard";
import { formatDateToKorean } from "../../../util/formatDateToKorean";
import useGetChoresOfHouse from "../../../hooks/queries/chore/useGetChoresOfHouse";

const ChoreOfHouseCards = () => {
  const { data } = useGetChoresOfHouse();
  return (
    <Styled.Wrapper style={{ gap: 20 }}>
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
      {data && data?.data.content.length <= 0 ? (
        <Styled.NoContentWrapper>
          <Styled.ContentTitle>하우스의 휴일인가요?</Styled.ContentTitle>
          <Styled.ContentTitle>오늘은 할 일이 없어요!</Styled.ContentTitle>
        </Styled.NoContentWrapper>
      ) : (
        data?.data.content.map(chore => (
          <ChoreOfHousCard
            key={chore.choreId}
            isCompleted={chore.isCompleted}
            scheduleName={chore.name}
            time={chore.startTime.slice(0, -3)}
            allocatorNames={chore.memberNames}
          />
        ))
      )}
    </Styled.Wrapper>
  );
};

export default ChoreOfHouseCards;
