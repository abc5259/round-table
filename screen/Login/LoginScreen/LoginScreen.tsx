import { Controller, useForm } from "react-hook-form";
import LoginLayout from "../../../layouts/LoginLayout/LoginLayout";
import Button from "../../../components/Button/Button";
import LabelInput from "../../../components/LabelInput/LabelInput";
import { login } from "../../../api/authApi";
import * as SecureStore from "expo-secure-store";

type FormValue = {
  email: string;
  password: string;
};

const LoginScreen = () => {
  const {
    control,
    handleSubmit,
    setValue,
    watch,
    formState: { errors },
  } = useForm<FormValue>({
    defaultValues: {
      email: "",
      password: "",
    },
  });

  const onSubmit = async ({ email, password }: FormValue) => {
    const { success, data, message } = await login(email, password);

    if (!success && message) {
      alert(message);
      return;
    }

    await Promise.all([
      SecureStore.setItemAsync("accessToken", data.accessToken),
      SecureStore.setItemAsync("refreshToken", data.refreshToken),
    ]);
    return;
  };

  return (
    <LoginLayout
      topText="회원가입에 사용한"
      bottomText="이메일과 비밀번호를 입력해주세요"
      inputs={
        <>
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
        </>
      }
      button={<Button onPress={handleSubmit(onSubmit)}>시작하기</Button>}
    />
  );
};

export default LoginScreen;
