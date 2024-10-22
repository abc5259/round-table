import { ScrollView, StyleSheet, Text, View } from 'react-native';
import * as Styled from './styled';
import ChoreCard from '../../molecules/ChoreCard/ChoreCard';
import { WithLocalSvg } from 'react-native-svg/css';
import useGetMySchedules from '../../../hooks/queries/chore/useGetMySchedules';

const ChoreCards = () => {
  const { data: myScheduleData } = useGetMySchedules();

  return (
    <Styled.Wrapper>
      <View style={{ flexDirection: 'row', gap: 10 }}>
        <Styled.ContentTitle>오늘 할 일</Styled.ContentTitle>
        <Styled.ContentTitle style={{ color: '#5BADFF' }}>
          {myScheduleData?.data.content.length || 0}건
        </Styled.ContentTitle>
      </View>
      <View>
        <ScrollView horizontal={true} showsHorizontalScrollIndicator={false}>
          {myScheduleData && myScheduleData?.data.content.length > 0 ? (
            myScheduleData?.data.content.map(schedule => (
              <View
                key={schedule.id}
                style={{ marginRight: 20, ...styles.card }}
              >
                <ChoreCard
                  id={schedule.id}
                  isCompleted={schedule.isCompleted}
                  name={schedule.name}
                  category={schedule.category}
                  startTime={schedule.startTime.slice(0, -3)}
                />
              </View>
            ))
          ) : (
            <Styled.Card style={styles.card}>
              <View style={{ alignItems: 'center', gap: 10 }}>
                <WithLocalSvg
                  asset={require('../../../assets/vectors/smile.svg')}
                />
                <Text
                  style={{
                    color: '#9CA3AB',
                    fontWeight: 'bold',
                    fontSize: 14,
                  }}
                >
                  오늘 할일이 없어요!
                </Text>
              </View>
            </Styled.Card>
          )}
        </ScrollView>
      </View>
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

export default ChoreCards;
