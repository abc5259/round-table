import styled from "styled-components/native";

export const Wrapper = styled.View`
  width: 100%;
  height: 86px;
  background-color: #fff;
  border-radius: 8%;
  padding: 17px 20px;
  justify-content: space-between;
`;

export const Badge = styled.View<{ isCompleted: boolean }>`
  width: 50px;
  height: 20px;
  border-radius: 40%;
  justify-content: center;
  align-items: center;
  border: ${props =>
    props.isCompleted ? "1px solid #11D78F" : "1px solid #9CA3AB"};
`;

export const BadgeText = styled.Text<{ isCompleted: boolean }>`
  color: ${props => (props.isCompleted ? "#11D78F" : "#9CA3AB")};
  font-weight: bold;
  font-size: 12px;
`;

export const H3 = styled.Text`
  color: #3f4245;
  font-size: 14px;
  font-weight: 600;
`;
