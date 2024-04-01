import styled from "styled-components/native";

export const Wrapper = styled.View`
  width: 100%;
  height: 50px;
  background-color: #fff;
  margin-top: 35px;
  padding-left: 20px;
  padding-right: 20px;

  display: flex;
  flex-direction: row;
  align-items: center;
`;

export const SvgWrapper = styled.TouchableOpacity`
  flex: 1;
`;

export const TitleWrapper = styled.View`
  flex: 1;
  display: flex;
  flex-direction: row;
  justify-content: center;
`;

export const Title = styled.Text`
  font-size: 16px;
  color: #3f4245;
`;

export const Item = styled.Text`
  flex: 1;
  display: flex;
  flex-direction: row;
  justify-content: flex-end;
  align-items: center;
`;
