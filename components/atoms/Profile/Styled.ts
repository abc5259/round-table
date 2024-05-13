import styled from "styled-components/native";

export const Wrapper = styled.View<{ isDefault: boolean }>`
  width: 62px;
  height: 62px;
  border-radius: 50%;
  background-color: ${props => (props.isDefault ? "#EFF5FE" : "none")};
  border: 1px solid #e7f0fe;
  justify-content: center;
  align-items: center;
`;
