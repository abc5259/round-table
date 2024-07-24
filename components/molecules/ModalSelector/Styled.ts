import styled from "styled-components/native";

export const Wrapper = styled.View`
  position: absolute;
  bottom: 0;
  width: 100%;
  z-index: 1000;
  padding: 20px;
  background-color: #fff;
  border-top-left-radius: 15%;
  border-top-right-radius: 15%;
  gap: 25px;
`;

export const CheckboxWrapper = styled.View`
  gap: 10px;
  margin-bottom: 15px;
`;

export const Overlay = styled.View`
  position: absolute;
  opacity: 0.6;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1000;
  background-color: #595a5b;
`;

export const Title = styled.Text`
  font-size: 17px;
  font-weight: bold;
  color: #3f4245;
`;
