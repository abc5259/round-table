import { View } from 'react-native';
import * as Styled from './Styled';
import ChoreOfHouseCard from '../../molecules/ChoreOfHouseCard/ChoreOfHousCard';
import { formatDateToKorean } from '../../../util/formatDateToKorean';
import useGetScheduleOfHouse from '../../../hooks/queries/chore/useGetScheduleOfHouse';

const ChoreOfHouseCards = () => {
  const { data } = useGetScheduleOfHouse();

  return (
    <Styled.Wrapper style={{ gap: 20 }}>
      <View
        style={{
          flexDirection: 'row',
          gap: 10,
          alignItems: 'center',
          justifyContent: 'space-between',
        }}
      >
        <Styled.Title>하우스 할 일 현황</Styled.Title>
        <Styled.SubTitle>{formatDateToKorean(new Date())}</Styled.SubTitle>
      </View>
      {!data || data?.data?.content.length <= 0 ? (
        <Styled.NoContentWrapper>
          <Styled.ContentTitle>하우스의 휴일인가요?</Styled.ContentTitle>
          <Styled.ContentTitle>오늘은 할 일이 없어요!</Styled.ContentTitle>
        </Styled.NoContentWrapper>
      ) : (
        data?.data.content.map(chore => (
          <ChoreOfHouseCard
            key={chore.id}
            isCompleted={chore.isCompleted}
            scheduleName={chore.name}
            time={chore.startTime.slice(0, -3)}
            allocatorNames={chore.managers}
          />
        ))
      )}
    </Styled.Wrapper>
  );
};

export default ChoreOfHouseCards;
