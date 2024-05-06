import TopBottomLayout from "../../../layouts/TopBottomLayout/TopBottomLayout";
import { useHouseAppenderStore } from "../../../store/house/houseAppenderStore";
import Button from "../../Button/Button";
import Label from "../../Label/Label";
import AddIcon from "../../../assets/vectors/add.svg";
import { WithLocalSvg } from "react-native-svg/css";
import * as Styled from "./Styled";
import { TouchableOpacity, View } from "react-native";
import InputButton from "../../Input/InputButton";
import { useState } from "react";

const HouseInviteEmail = () => {
  const { next } = useHouseAppenderStore();
  const [emails, setEmails] = useState([""]);

  const onPressInviteBtn = () => {};

  const onPressSkipBtn = () => {
    next();
  };

  const onChangeEmail = (idx: number, newEmail: string) => {
    setEmails(state => {
      const newState = [...state];
      newState[idx] = newEmail;
      return newState;
    });
  };

  const onPressButton = (email: string) => {};
  return (
    <TopBottomLayout
      topText="하우스에 함깨할"
      bottomText="멤버를 초대해보세요!"
      inputs={
        <>
          <Styled.LabelWrapper>
            <Label
              style={{ flex: 1 }}
              text="초대할 맴버의 이메일을 알려주세요."
            />
            <TouchableOpacity
              onPress={() => setEmails(state => [...state, ""])}
            >
              <WithLocalSvg asset={AddIcon} />
            </TouchableOpacity>
          </Styled.LabelWrapper>
          {emails.map((email, idx) => (
            <InputButton
              key={idx}
              placeholder="example@domain.com"
              value={email}
              onChange={text => onChangeEmail(idx, text)}
              onPressCancel={() => onChangeEmail(idx, "")}
              onPressButton={() => onPressButton(email)}
            />
          ))}
        </>
      }
      button={
        <>
          <Button onPress={onPressInviteBtn}>초대하기</Button>
          <Button
            isBg={false}
            textStyled={{ color: "#9CA3AB" }}
            onPress={onPressSkipBtn}
          >
            건너뛰기
          </Button>
        </>
      }
    />
  );
};

export default HouseInviteEmail;
