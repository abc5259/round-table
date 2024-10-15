import styled from "styled-components/native";

export const Wrapper = styled.View`
  background-color: #fff;
  width: 207px;
  height: 291px;
  border-radius: 6px;
  padding: 20px 15px;
  gap: 10px;
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

export const Name = styled.Text`
  font-size: 16px;
  font-weight: bold;
  color: #3f4245;
`;

export const CategoryWrapper = styled.View`
  /* justify-content: center; */
  align-items: center;
  margin: 0 auto;
  flex: 1;
`;

export const Sub = styled.Text`
  font-size: 14px;
  color: #80878e;
`;

export const Button = styled.View`
  width: 100%;
  height: 36px;
  justify-content: center;
  align-items: center;
  border-radius: 6%;
`;
