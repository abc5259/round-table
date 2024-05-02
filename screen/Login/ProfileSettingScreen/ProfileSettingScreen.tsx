import { Controller, useForm } from "react-hook-form";
import LoginLayout from "../../../layouts/LoginLayout/LoginLayout";
import LabelInput from "../../../components/LabelInput/LabelInput";
import Button from "../../../components/Button/Button";
import SelectButton from "../../../components/SelectButton/SelectButton";
import { useState } from "react";
import { Gender, updateProfile } from "../../../api/memberApi";

type FormValue = {
  name: string;
};

const ProfileSettingScreen = () => {
  const [gender, setGender] = useState<Gender>(Gender.MEN);
  const {
    control,
    handleSubmit,
    setValue,
    formState: { errors },
  } = useForm<FormValue>({
    defaultValues: {
      name: "",
    },
  });

  const onChangeGender = (korGender: string) => {
    setGender(changeGenderFromKorean(korGender));
  };

  function changeGenderFromKorean(korGender: string): Gender {
    switch (korGender) {
      case "남성":
        return Gender.MEN;
      case "여성":
        return Gender.GIRL;
      default:
        throw new Error("Invalid Korean gender");
    }
  }

  const onSubmit = async ({ name }: FormValue) => {
    const res = await updateProfile(name, gender);
    console.log(res);
    if (!res.success) {
      alert(res.message || "다시 시도해주세요");
      return;
    }
  };

  return (
    <LoginLayout
      topText="본인의 정보를 입력해주세요"
      inputs={
        <>
          <Controller
            control={control}
            rules={{
              required: "이름은 필수입니다.",
            }}
            render={({ field: { value, onChange } }) => (
              <LabelInput
                labelProps={{ text: "이름" }}
                inputProps={{
                  onChange,
                  value,
                  onPressCancel: () => setValue("name", ""),
                }}
                errorMessage={errors.name?.message}
              />
            )}
            name="name"
          />
          <SelectButton
            labelText="성별"
            texts={["남성", "여성"]}
            defulatValue="남성"
            onChange={onChangeGender}
          />
        </>
      }
      button={<Button onPress={handleSubmit(onSubmit)}>시작하기</Button>}
    />
  );
};

export default ProfileSettingScreen;
