import styled from "styled-components/native";

export const Wrapper = styled.View``;

export const CustomInput = styled.TextInput`
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
