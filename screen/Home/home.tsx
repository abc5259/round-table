import { View } from "react-native";
import * as Styled from "./Styled";
import Button from "../../components/atoms/Button/Button";
import { NavigationProp } from "@react-navigation/native";
import { RootStackParamList } from "../../App";

type HomeScreenProps = {
  navigation: NavigationProp<RootStackParamList, "EmailCheck">;
};

const Home = ({ navigation }: HomeScreenProps) => {
  const onPressJoinButton = () => {
    navigation.navigate("EmailInput");
  };

  const onPressLoginButton = () => {
    navigation.navigate("Login");
  };
  return (
    <Styled.Wrapper>
      <Styled.ImageBg
        source={require("../../assets/main.png")}
        resizeMode="cover"
      >
        <View style={{ gap: 10 }}>
          <Button onPress={onPressLoginButton}>이메일로 시작하기</Button>
          <Button isBg={false} onPress={onPressJoinButton}>
            회원가입
          </Button>
        </View>
      </Styled.ImageBg>
    </Styled.Wrapper>
  );
};

export default Home;
