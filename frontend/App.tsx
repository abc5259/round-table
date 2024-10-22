import { NavigationContainer } from '@react-navigation/native';
import {
  createNativeStackNavigator,
  NativeStackNavigationProp,
} from '@react-navigation/native-stack';
import Home from './screen/Home/home';
import EmailCheckScreen from './screen/Join/EmailCheckScreen/EmailCheckScreen';
import Header from './components/atoms/Header/Header';
import EmailInputScreen from './screen/Join/EmailInputScreen/EmailInputScreen';
import PasswordInputScreen from './screen/Join/PasswordInputScreen/PasswordInputScreen';
import LoginScreen from './screen/Login/LoginScreen/LoginScreen';
import ProfileSettingScreen from './screen/Login/ProfileSettingScreen/ProfileSettingScreen';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { DevToolsBubble } from 'react-native-react-query-devtools';
import CreateHouseScreen from './screen/House/CreateHouseScreen/CreateHouseScreen';
import CreateScheduleScreen from './screen/Schedule/CreateScheduleScreen/CreateScheduleScreen';
import BottomTabNavigator from './BottomTabNavigator';
import { StatusBar } from 'react-native';

export type RootStackParamList = {
  Home: undefined;
  EmailInput: undefined;
  EmailCheck: { email: string };
  PasswordInput: { email: string };
  Login: undefined;
  ProfileSetting: undefined;
  CreateHouse: undefined;
  CreateSchedule: undefined;
  BottomTabNavigator: undefined;
};

const Stack = createNativeStackNavigator<RootStackParamList>();

export type RootStackNavigationProp =
  NativeStackNavigationProp<RootStackParamList>;

export default function App() {
  const queryClient = new QueryClient({});

  return (
    <QueryClientProvider client={queryClient}>
      <DevToolsBubble />
      <StatusBar />
      <NavigationContainer>
        <Stack.Navigator initialRouteName="BottomTabNavigator">
          <Stack.Screen
            name="Home"
            component={Home}
            options={{ headerShown: false }}
          />
          <Stack.Group
            screenOptions={{ header: () => <Header title="회원가입" /> }}
          >
            <Stack.Screen name="EmailCheck" component={EmailCheckScreen} />
            <Stack.Screen name="EmailInput" component={EmailInputScreen} />
            <Stack.Screen
              name="PasswordInput"
              component={PasswordInputScreen}
            />
          </Stack.Group>

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
          <Stack.Screen
            name="CreateHouse"
            component={CreateHouseScreen}
            options={{
              header: () => <Header title="하우스 만들기" />,
            }}
          />
          <Stack.Screen
            name="CreateSchedule"
            component={CreateScheduleScreen}
            options={{
              header: () => <Header title="일정 만들기" />,
            }}
          />
          <Stack.Screen
            name={'BottomTabNavigator'}
            component={BottomTabNavigator}
            options={{ headerShown: false }}
          />
        </Stack.Navigator>
      </NavigationContainer>
    </QueryClientProvider>
  );
}
