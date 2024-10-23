import { useEffect } from 'react';
import { View } from 'react-native';
import * as Styled from './Styled';
import useMe from '../../hooks/queries/member/useMe';
import ChoreCards from '../../components/organisms/ChoreCards/ChoreCards';
import ChoreOfHouseCards from '../../components/organisms/ChoresOfHouseCards/ChoreOfHouseCards';
import useConnectionSse from '../../hooks/common/useConnectionSse';
import { useNavigation } from '@react-navigation/native';
import { RootStackNavigationProp } from '../../App';

const MainScreen = () => {
  const navigation = useNavigation<RootStackNavigationProp>();
  const { data: meData, error, isError } = useMe();
  useConnectionSse();

  // 네비게이션을 렌더링 이후에 실행하기 위한 useEffect 사용
  useEffect(() => {
    if (isError || (meData && !meData.success)) {
      navigation.navigate('Login');
    }
  }, [isError, meData, navigation]);

  return (
    <Styled.Wrapper showsVerticalScrollIndicator={false}>
      <Styled.Header
        style={{
          paddingLeft: 20,
          paddingTop: 20,
          paddingBottom: 20,
          backgroundColor: '#FFFFFF',
        }}
      >
        <View style={{ gap: 6 }}>
          <Styled.SubTitle>{meData?.data?.house?.name} 하우스</Styled.SubTitle>
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
