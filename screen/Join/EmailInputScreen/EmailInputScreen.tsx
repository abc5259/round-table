import { useForm, Controller } from "react-hook-form";
import JoinLayout from "../../../layouts/JoinLayout/JoinLayout";
import LabelInput from "../../../components/molecules/LabelInput/LabelInput";
import Button from "../../../components/atoms/Button/Button";
import { NavigationProp, useNavigation } from "@react-navigation/native";
import { RootStackParamList } from "../../../App";
import { sendEmail } from "../../../api/authApi";

type FormValue = { email: string };

const EmailInputScreen = () => {
  const navigation = useNavigation<NavigationProp<RootStackParamList>>();
  const {
    control,
    handleSubmit,
    setValue,
    formState: { errors },
  } = useForm<FormValue>({
    defaultValues: {
      email: "",
    },
  });
  const onSubmit = async ({ email }: FormValue) => {
    await sendEmail(email);
    navigation.navigate("EmailCheck", { email });
  };

  return (
    <JoinLayout
      topText={"로그인에 사용할"}
      bottomText={"이메일을 입력해주세요"}
      inputs={
        <Controller
          control={control}
          rules={{
            required: "이메일은 필수입니다.",
            pattern: {
              value: /^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/i,
              message: "이메일 형식이 아닙니다.",
            },
          }}
          render={({ field: { value, onChange } }) => (
            <LabelInput
              labelProps={{ text: "이메일 주소" }}
              inputProps={{
                placeholder: "example@domin.com",
                onChange,
                value,
                onPressCancel: () => setValue("email", ""),
              }}
              errorMessage={errors.email?.message}
            />
          )}
          name="email"
        />
      }
      button={<Button onPress={handleSubmit(onSubmit)}>다음</Button>}
    />
  );
};

export default EmailInputScreen;
