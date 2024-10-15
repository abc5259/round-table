import { Controller, useForm } from "react-hook-form";
import LoginLayout from "../../../layouts/LoginLayout/LoginLayout";
import Button from "../../../components/atoms/Button/Button";
import LabelInput from "../../../components/molecules/LabelInput/LabelInput";
import { login } from "../../../api/authApi";
import * as SecureStore from "expo-secure-store";
import { NavigationProp, RouteProp } from "@react-navigation/native";
import { RootStackParamList } from "../../../App";
import { useQuery } from "@tanstack/react-query";
import { GetMeResponse, getMe } from "../../../api/memberApi";

type LoginScreenRouteProp = RouteProp<RootStackParamList, "Login">;

type LoginScreenProps = {
  navigation: NavigationProp<RootStackParamList, "Login">;
  route: LoginScreenRouteProp;
};

type FormValue = {
  email: string;
  password: string;
};

const LoginScreen = ({ navigation }: LoginScreenProps) => {
  const { refetch } = useQuery({
    queryKey: ["me"],
    queryFn: getMe,
    enabled: false,
  });

  const {
    control,
    handleSubmit,
    setValue,
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

    const { isSuccess, isError, data: meData } = await refetch();

    if (isSuccess && meData.success) {
      navigateBy(meData);
    }

    if (isError) {
      alert("다시 시도해주세요");
    }
  };

  const navigateBy = (meData: GetMeResponse) => {
    const { name, gender, house } = meData.data;
    if (name == null || gender == null) {
      navigation.navigate("ProfileSetting");
      return;
    }

    if (house == null) {
      navigation.navigate("CreateHouse");
      return;
    }
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
