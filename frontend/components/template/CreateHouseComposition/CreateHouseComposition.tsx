import { Image, Text, View } from 'react-native';
import TopBottomLayout from '../../../layouts/TopBottomLayout/TopBottomLayout';
import Button from '../../atoms/Button/Button';
import * as Styled from './Styled';
import { useHouseAppenderStore } from '../../../store/house/houseAppenderStore';
import { useNavigation } from '@react-navigation/native';
import { RootStackNavigationProp } from '../../../App';

const CreateHouseComposition = () => {
  const { name, houseTotalPeople } = useHouseAppenderStore();
  const navigation = useNavigation<RootStackNavigationProp>();
  const onPressBtn = () => {
    navigation.navigate('BottomTabNavigator');
  };
  return (
    <TopBottomLayout
      topText="준비가 모두 끝났어요"
      bottomText="하우스에 입장해볼까요?"
      inputs={
        <Styled.Wrapper>
          <Image source={require('../../../assets/images/house.png')} />
          <Styled.TextWrapper>
            <View
              style={{
                flex: 0.5,
                justifyContent: 'center',
                alignItems: 'center',
                gap: 5,
                borderRightWidth: 0.5,
                borderRightColor: '#DADDE4',
              }}
            >
              <Text style={{ fontSize: 14, color: '#565A5E' }}>
                하우스 이름
              </Text>
              <Text
                style={{ fontSize: 17, color: '#3F4245', fontWeight: 'bold' }}
              >
                {name}
              </Text>
            </View>
            <View
              style={{
                flex: 0.5,
                justifyContent: 'center',
                alignItems: 'center',
                gap: 5,
                borderLeftWidth: 0.5,
                borderLeftColor: '#DADDE4',
              }}
            >
              <Text style={{ fontSize: 14, color: '#565A5E' }}>
                하우스 구성
              </Text>
              <Text
                style={{ fontSize: 17, color: '#3F4245', fontWeight: 'bold' }}
              >
                {houseTotalPeople}명
              </Text>
            </View>
          </Styled.TextWrapper>
        </Styled.Wrapper>
      }
      button={<Button onPress={onPressBtn}>하우스 입장하기</Button>}
    />
  );
};

export default CreateHouseComposition;
