import styled from "styled-components/native";

export const Wrapper = styled.TouchableOpacity`
  background-color: #eff5fe;
  border-radius: 8%;
  width: 100%;
  height: 52px;
  justify-content: center;
  padding-left: 20px;
`;

export const SvgWrapper = styled.View`
  position: absolute;
  right: 20px;
  top: 50%;
  transform: translateY(-5px);
`;

export const Text = styled.Text`
  color: #5b6574;
  font-size: 14px;
`;
