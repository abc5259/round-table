import { NavigationContainer } from "@react-navigation/native";
import { createNativeStackNavigator } from "@react-navigation/native-stack";
import Home from "./screen/Home/home";
import EmailCheckScreen from "./screen/Join/EmailCheckScreen/EmailCheckScreen";
import Header from "./components/Header/Header";
import EmailInputScreen from "./screen/Join/EmailInputScreen/EmailInputScreen";
import PasswordInputScreen from "./screen/Join/PasswordInputScreen/PasswordInputScreen";
import LoginScreen from "./screen/Login/LoginScreen/LoginScreen";
import ProfileSettingScreen from "./screen/Login/ProfileSettingScreen/ProfileSettingScreen";

export type RootStackParamList = {
  Home: undefined;
  EmailInput: undefined;
  EmailCheck: { email: string };
  PasswordInput: { email: string };
  Login: undefined;
  ProfileSetting: undefined;
};

const Stack = createNativeStackNavigator<RootStackParamList>();

export default function App() {
  return (
    <NavigationContainer>
      <Stack.Navigator initialRouteName="Home">
        <Stack.Screen
          name="Home"
          component={Home}
          options={{ headerShown: false }}
        />
        <Stack.Screen
          name="EmailCheck"
          component={EmailCheckScreen}
          options={{
            header: () => <Header title="회원가입" />,
          }}
        />

        <Stack.Screen
          name="EmailInput"
          component={EmailInputScreen}
          options={{
            header: () => <Header title="회원가입" />,
          }}
        />

        <Stack.Screen
          name="PasswordInput"
          component={PasswordInputScreen}
          options={{
            header: () => <Header title="회원가입" />,
          }}
        />
        <Stack.Screen
          name="Login"
          component={LoginScreen}
          options={{
            header: () => <Header title="시작하기" />,
          }}
        />
        <Stack.Screen
          name="ProfileSetting"
          component={ProfileSettingScreen}
          options={{
            header: () => <Header title="프로필 설정" />,
          }}
        />
      </Stack.Navigator>
    </NavigationContainer>
  );
}
