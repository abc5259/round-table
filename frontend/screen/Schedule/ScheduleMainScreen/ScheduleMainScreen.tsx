import * as Styled from './Styled';
import { Image, View } from 'react-native';
import { WithLocalSvg } from 'react-native-svg/css';
import { useNavigation } from '@react-navigation/native';
import { RootStackNavigationProp } from '../../../App';

const ScheduleMainScreen = () => {
  const navigation = useNavigation<RootStackNavigationProp>();
  return (
    <Styled.Wrapper>
      <Styled.Header>
        <Styled.HeaderLabel>가사 분담</Styled.HeaderLabel>
        <View style={{ gap: 5 }}>
          <Styled.HeaderSubTitle>하우스의 전체 가사</Styled.HeaderSubTitle>
          <Styled.HeaderSubTitle>일정을 확인할 수 있어요</Styled.HeaderSubTitle>
        </View>
      </Styled.Header>
      <Image source={require('../../../assets/images/calendar.png')} />
      <Styled.Message>
        <Styled.MessageChild1>아직 분담 계획이 없어요!</Styled.MessageChild1>
        <Styled.MessageChild2>
          일정을 만들고 가사를 분담해보세요.
        </Styled.MessageChild2>
      </Styled.Message>
      <Styled.AddWrapper onPress={() => navigation.navigate('CreateSchedule')}>
        <WithLocalSvg
          asset={require('../../../assets/vectors/plus-icon.svg')}
        />
      </Styled.AddWrapper>
    </Styled.Wrapper>
  );
};

export default ScheduleMainScreen;
