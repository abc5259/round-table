import { useForm, Controller } from "react-hook-form";
import JoinLayout from "../../../layouts/JoinLayout/JoinLayout";
import LabelInput from "../../../components/LabelInput/LabelInput";
import Button from "../../../components/Button/Button";
import { NavigationProp, RouteProp } from "@react-navigation/native";
import { RootStackParamList } from "../../../App";

type EmailCheckScreenRouteProp = RouteProp<RootStackParamList, "EmailCheck">;

type EmailCheckScreenProps = {
  navigation: NavigationProp<RootStackParamList, "EmailCheck">;
  route: EmailCheckScreenRouteProp;
};

type FormValue = {
  email: string;
};

const EmailCheckScreen = ({ navigation, route }: EmailCheckScreenProps) => {
  const { email } = route.params;
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
  const onSubmit = (data: FormValue) => {
    console.log(data);
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

export default EmailCheckScreen;
