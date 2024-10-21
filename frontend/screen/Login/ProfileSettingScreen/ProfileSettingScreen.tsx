import { Controller, useForm } from 'react-hook-form';
import LoginLayout from '../../../layouts/LoginLayout/LoginLayout';
import LabelInput from '../../../components/molecules/LabelInput/LabelInput';
import Button from '../../../components/atoms/Button/Button';
import SelectButton from '../../../components/molecules/SelectButton/SelectButton';
import { useState } from 'react';
import { Gender, updateProfile } from '../../../api/memberApi';
import { NavigationProp, RouteProp } from '@react-navigation/native';
import { RootStackParamList } from '../../../App';
import useMe from '../../../hooks/queries/member/useMe';

type ProfileSettingScreenRouteProp = RouteProp<
  RootStackParamList,
  'ProfileSetting'
>;

type ProfileSettingScreenProps = {
  navigation: NavigationProp<RootStackParamList, 'ProfileSetting'>;
  route: ProfileSettingScreenRouteProp;
};

type FormValue = {
  name: string;
};

const ProfileSettingScreen = ({ navigation }: ProfileSettingScreenProps) => {
  const { data } = useMe({ enabled: false });
  const [gender, setGender] = useState<Gender>(Gender.MALE);
  const {
    control,
    handleSubmit,
    setValue,
    formState: { errors },
  } = useForm<FormValue>({
    defaultValues: {
      name: '',
    },
  });

  const onChangeGender = (korGender: string) => {
    setGender(changeGenderFromKorean(korGender));
  };

  function changeGenderFromKorean(korGender: string): Gender {
    switch (korGender) {
      case '남성':
        return Gender.MALE;
      case '여성':
        return Gender.FEMALE;
      default:
        throw new Error('Invalid Korean gender');
    }
  }

  const onSubmit = async ({ name }: FormValue) => {
    const res = await updateProfile(name, gender);
    console.log(res);
    if (!res.success) {
      alert(res.message || '다시 시도해주세요');
      return;
    }

    if (data?.data.house == null) {
      navigation.navigate('CreateHouse');
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
              required: '이름은 필수입니다.',
            }}
            render={({ field: { value, onChange } }) => (
              <LabelInput
                labelProps={{ text: '이름' }}
                inputProps={{
                  onChange,
                  value,
                  onPressCancel: () => setValue('name', ''),
                }}
                errorMessage={errors.name?.message}
              />
            )}
            name="name"
          />
          <SelectButton
            labelText="성별"
            texts={['남성', '여성']}
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
