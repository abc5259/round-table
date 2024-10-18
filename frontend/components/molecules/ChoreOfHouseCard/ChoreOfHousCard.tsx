import { StyleSheet, Text, View } from 'react-native';
import * as Styled from './Styled';
import { formatDateToKorean } from '../../../util/formatDateToKorean';

type Props = {
  isCompleted: boolean;
  time: string;
  allocatorNames: string;
  scheduleName: string;
};

const ChoreOfHouseCard = ({
  isCompleted,
  allocatorNames,
  scheduleName,
  time,
}: Props) => {
  return (
    <Styled.Wrapper style={styles.card}>
      <View style={{ flexDirection: 'row', gap: 10, alignItems: 'center' }}>
        <Styled.Badge isCompleted={isCompleted}>
          <Styled.BadgeText isCompleted={isCompleted}>
            {isCompleted ? '완료' : '미완료'}
          </Styled.BadgeText>
        </Styled.Badge>
        <Text style={{ fontSize: 12, color: '#80878E' }}>
          {formatDateToKorean(new Date())} {time}
        </Text>
      </View>
      <Styled.H3>
        {allocatorNames} 님이 {scheduleName}{' '}
        {isCompleted ? `하기를 완료했어요` : '하기가 미완료 상태에요'}
      </Styled.H3>
    </Styled.Wrapper>
  );
};

const styles = StyleSheet.create({
  card: {
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 0.8,
    },
    shadowOpacity: 0.1,
    shadowRadius: 1.5,
  },
});

export default ChoreOfHouseCard;
