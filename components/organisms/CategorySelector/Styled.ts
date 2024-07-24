import styled, { css } from "styled-components/native";

export const Wrapper = styled.View``;

export const Box = styled.Pressable<{ isSelected?: boolean }>`
  width: 139px;
  height: 132px;
  justify-content: center;
  align-items: center;
  background-color: #f8f9fd;
  border-radius: 10%;
  gap: 5px;
  ${props =>
    props.isSelected &&
    css`
      border: 2px solid #5badff;
    `}
`;
