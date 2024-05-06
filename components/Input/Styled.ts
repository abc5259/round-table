import styled from "styled-components/native";

export const Wrapper = styled.View`
  width: 100%;
  flex-direction: row;
  gap: 10px;
`;

export const CustomInput = styled.TextInput`
  width: 100%;
  height: 48px;
  background-color: #eff5fe;
  border-radius: 8px;
  font-size: 15px;
  padding: 10px 20px;
  position: relative;
`;

export const SvgWrapper = styled.TouchableOpacity`
  position: absolute;
  right: 20px;
  top: 50%;
  transform: translateY(-8px);
`;

export const ErrorMessage = styled.Text`
  color: #e52828;
  font-size: 12px;
`;
