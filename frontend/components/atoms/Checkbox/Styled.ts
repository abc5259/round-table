import styled from "styled-components/native";

export const Wrapper = styled.Pressable``;

export const CheckIcon = styled.View<{ isChecked: boolean }>`
  border: 2px solid #bdc2d3;
  width: 18px;
  height: 18px;
  border-radius: 100%;
  align-items: center;
  justify-content: center;
  border-color: ${({ isChecked }) => (isChecked ? "#5badff" : "#BFCEE4")};
`;

export const InnerCircle = styled.View<{ isChecked: boolean }>`
  width: 10px;
  height: 10px;
  border-radius: 100%;
  background-color: ${({ isChecked }) => (isChecked ? "#5badff" : "#e2edfe")};
`;

export const Box = styled.View`
  width: 100%;
  height: 52px;
  background-color: #e2edfe;
  border-radius: 8%;
  flex-direction: row;
  align-items: center;
  gap: 10px;
  padding-left: 20px;
`;

export const Text = styled.Text<{ isChecked: boolean }>`
  font-size: 14px;
  color: ${({ isChecked }) => (isChecked ? "#565960" : "#A7ADBD")};
`;
