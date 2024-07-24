import styled from "styled-components/native";

export const Wrapper = styled.View``;

export const ToggleButton = styled.Pressable<{ isActive: boolean }>`
  width: 106px;
  height: 33px;
  border-radius: 30%;
  justify-content: center;
  align-items: center;
  background-color: ${props => (props.isActive ? "#5BADFF" : "#ECEEF4")};
`;

export const ToggleText = styled.Text<{ isActive: boolean }>`
  color: ${props => (props.isActive ? "#FFFFFF" : "#A7ADBD")};
  font-weight: bold;
  font-size: 12px;
`;
