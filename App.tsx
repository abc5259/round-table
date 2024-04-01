import { StyleSheet, Text, View } from "react-native";
import { NavigationContainer } from "@react-navigation/native";
import { NavigationProp } from "@react-navigation/native";
import { createNativeStackNavigator } from "@react-navigation/native-stack";
import Button from "./components/Button/Button";
import Home from "./page/Home/home";
import EmailCheckScreen from "./page/Join/EmailCheckScreen/EmailCheckScreen";
import Header from "./components/Header/Header";

export type RootStackParamList = {
  Home: undefined;
  EmailCheck: { email: string };
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
      </Stack.Navigator>
    </NavigationContainer>
  );
}
