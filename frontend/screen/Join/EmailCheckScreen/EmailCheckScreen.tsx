import { useForm, Controller } from "react-hook-form";
import JoinLayout from "../../../layouts/JoinLayout/JoinLayout";
import LabelInput from "../../../components/molecules/LabelInput/LabelInput";
import Button from "../../../components/atoms/Button/Button";
import { NavigationProp, RouteProp } from "@react-navigation/native";
import { RootStackParamList } from "../../../App";
import { checkEmailAuthCode } from "../../../api/authApi";

type EmailCheckScreenRouteProp = RouteProp<RootStackParamList, "EmailCheck">;

type EmailCheckScreenProps = {
  navigation: NavigationProp<RootStackParamList, "EmailCheck">;
  route: EmailCheckScreenRouteProp;
};

type FormValue = {
  code: string;
};

const EmailCheckScreen = ({ navigation, route }: EmailCheckScreenProps) => {
  const { email } = route.params;
  const {
    control,
    handleSubmit,
    setValue,
    setError,
    formState: { errors },
  } = useForm<FormValue>({
    defaultValues: {
      code: "",
    },
  });
  const onSubmit = async ({ code }: FormValue) => {
    const data = await checkEmailAuthCode(email, code);
    if (!data.success) {
      setError("code", {
        type: "incorrectCode",
        message: "올바르지 않은 인증코드입니다.",
      });
      return;
    }
    navigation.navigate("PasswordInput", { email });
  };

  return (
    <JoinLayout
      topText={`${email}로 전송된`}
      bottomText={"인증코드를 입력해주세요"}
      inputs={
        <Controller
          control={control}
          rules={{
            required: "인증코드를 적어주세요",
          }}
          render={({ field: { value, onChange } }) => (
            <LabelInput
              labelProps={{ text: "인증코드" }}
              inputProps={{
                onChange,
                value,
                onPressCancel: () => setValue("code", ""),
              }}
              errorMessage={errors.code?.message}
            />
          )}
          name="code"
        />
      }
      button={<Button onPress={handleSubmit(onSubmit)}>다음</Button>}
    />
  );
};

export default EmailCheckScreen;
