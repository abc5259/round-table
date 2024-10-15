import { NavigationProp, RouteProp } from "@react-navigation/native";
import { RootStackParamList } from "../../../App";
import { Controller, useForm } from "react-hook-form";
import JoinLayout from "../../../layouts/JoinLayout/JoinLayout";
import LabelInput from "../../../components/molecules/LabelInput/LabelInput";
import Button from "../../../components/atoms/Button/Button";
import { registerMember } from "../../../api/authApi";

type PasswordInputScreenRouteProp = RouteProp<
  RootStackParamList,
  "PasswordInput"
>;

type PasswordInputScreenProps = {
  navigation: NavigationProp<RootStackParamList, "PasswordInput">;
  route: PasswordInputScreenRouteProp;
};

type FormValue = {
  password: string;
  passwordCheck: string;
};

const PasswordInputScreen = ({
  navigation,
  route,
}: PasswordInputScreenProps) => {
  const { email } = route.params;
  const {
    control,
    handleSubmit,
    setValue,
    watch,
    formState: { errors },
  } = useForm<FormValue>({
    defaultValues: {
      password: "",
      passwordCheck: "",
    },
  });

  const onSubmit = async ({ password }: FormValue) => {
    const data = await registerMember(email, password);
    if (!data.success) {
      alert(data.message);
      return;
    }
    alert("회원가입 완료");
    navigation.navigate("Home");
  };

  return (
    <JoinLayout
      topText="로그인에 사용할"
      bottomText="비밀번호를 입력해주세요"
      inputs={
        <>
          <Controller
            control={control}
            rules={{
              required: "비밀번호를 적어주세요",
              pattern: {
                value: /^(?=.*[A-Z])(?=.*\d).{8,}$/,
                message: "대문자와 숫자를 포함해 8자 이상 입력해주세요",
              },
            }}
            render={({ field: { value, onChange } }) => (
              <LabelInput
                labelProps={{ text: "비밀번호" }}
                inputProps={{
                  onChange,
                  value,
                  onPressCancel: () => setValue("password", ""),
                  secureTextEntry: true,
                }}
                errorMessage={errors.password?.message}
              />
            )}
            name="password"
          />
          <Controller
            control={control}
            rules={{
              required: "비밀번호 확인을 적어주세요",
              validate: value =>
                value === watch("password") || "비밀번호가 일치하지 않습니다.",
            }}
            render={({ field: { value, onChange } }) => (
              <LabelInput
                labelProps={{ text: "비밀번호 확인" }}
                inputProps={{
                  onChange,
                  value,
                  onPressCancel: () => setValue("passwordCheck", ""),
                  secureTextEntry: true,
                }}
                errorMessage={errors.passwordCheck?.message}
              />
            )}
            name="passwordCheck"
          />
        </>
      }
      button={<Button onPress={handleSubmit(onSubmit)}>다음</Button>}
    />
  );
};

export default PasswordInputScreen;
