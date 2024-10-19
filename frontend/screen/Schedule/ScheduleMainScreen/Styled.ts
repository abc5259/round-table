import styled from 'styled-components/native';

export const Wrapper = styled.View`
  padding-left: 20px;
  padding-right: 20px;
  flex: 1;
  background-color: #fff;
`;

export const Header = styled.View`
  gap: 10px;
  margin-top: 90px;
  margin-bottom: 100px;
`;

export const HeaderLabel = styled.Text`
  color: #808793;
  font-size: 15px;
`;

export const HeaderSubTitle = styled.Text`
  color: #3f4245;
  font-size: 22px;
  font-weight: bold;
`;

export const Message = styled.View`
  align-items: center;
  margin-top: 10px;
  gap: 5px;
`;

export const MessageChild1 = styled.Text`
  font-weight: bold;
  font-size: 17px;
  color: #3f4245;
`;

export const MessageChild2 = styled.Text`
  font-size: 15px;
  color: #61676d;
`;

export const AddWrapper = styled.TouchableOpacity`
  width: 62px;
  height: 62px;
  border-radius: 50%;
  background-color: #5badff;
  justify-content: center;
  align-items: center;
  position: absolute;
  bottom: 40px;
  right: 20px;
`;
