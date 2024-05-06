import { Controller, useForm } from "react-hook-form";
import TopBottomLayout from "../../../layouts/TopBottomLayout/TopBottomLayout";
import LabelInput from "../../LabelInput/LabelInput";
import Button from "../../Button/Button";
import { useHouseAppenderStore } from "../../../store/house/houseAppenderStore";

type FormValue = {
  name: string;
};

const CreateHouseName = () => {
  const updateName = useHouseAppenderStore(state => state.updateName);
  const next = useHouseAppenderStore(state => state.next);
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

  const onSubmit = ({ name }: FormValue) => {
    //하우스 이름 중복 체크

    updateName(name);
    next();
  };

  return (
    <TopBottomLayout
      topText="하우스의 이름은"
      bottomText="무엇인가요?"
      inputs={
        <Controller
          control={control}
          rules={{
            required: "하우스 이름은 필수입니다.",
          }}
          render={({ field: { value, onChange } }) => (
            <LabelInput
              labelProps={{ text: "하우스의 이름을 입력해주세요." }}
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
      }
      button={<Button onPress={handleSubmit(onSubmit)}>다음</Button>}
    />
  );
};

export default CreateHouseName;
