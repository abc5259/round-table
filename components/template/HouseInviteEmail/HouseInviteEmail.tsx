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
import { canInviteHouseEmail } from "../../../api/houseApi";

const HouseInviteEmail = () => {
  const { next } = useHouseAppenderStore();
  const [inputDatas, setInputDatas] = useState<
    {
      email: string;
      errorMessage: string | null;
      successMessage: string | null;
    }[]
  >([{ email: "", successMessage: null, errorMessage: null }]);

  const onPressInviteBtn = () => {};

  const onPressSkipBtn = () => {
    next();
  };

  const onPressAddIcon = () => {
    setInputDatas(state => [
      ...state,
      { email: "", successMessage: null, errorMessage: null },
    ]);
  };

  const onChangeEmail = (idx: number, newEmail: string) => {
    setInputDatas(state => {
      const newState = [...state];
      const newFiled = {
        ...newState[idx],
        email: newEmail,
        errorMessage: null,
        successMessage: null,
      };
      newState[idx] = newFiled;
      return newState;
    });
  };

  const onPressButton = async (email: string, idx: number) => {
    const res = await canInviteHouseEmail(email);
    if (!res.success) {
      setInputDatas(state => {
        const newState = [...state];
        const newFiled = {
          ...newState[idx],
          errorMessage: "초대할 수 없는 이메일입니다.",
          successMessage: null,
        };
        newState[idx] = newFiled;
        return newState;
      });
    }

    if (res.success) {
      setInputDatas(state => {
        const newState = [...state];
        const newFiled = {
          ...newState[idx],
          errorMessage: null,
          successMessage: "초대가능한 이메일입니다.",
        };
        newState[idx] = newFiled;
        return newState;
      });
    }
  };

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
            <TouchableOpacity onPress={onPressAddIcon}>
              <WithLocalSvg asset={AddIcon} />
            </TouchableOpacity>
          </Styled.LabelWrapper>
          {inputDatas.map((inputData, idx) => (
            <InputButton
              key={idx}
              placeholder="example@domain.com"
              value={inputData.email}
              onChange={text => onChangeEmail(idx, text)}
              onPressCancel={() => onChangeEmail(idx, "")}
              onPressButton={() => onPressButton(inputData.email, idx)}
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
