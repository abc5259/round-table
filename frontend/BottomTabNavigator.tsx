import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import MainScreen from './screen/Main/MainScreen';
import MainHeader from './components/molecules/MainHeader/MainHeader';
import ScheduleMainScreen from './screen/Schedule/ScheduleMainScreen/ScheduleMainScreen';

export type BottomTabParamList = {
  ScheduleMain: undefined;
  Main: undefined;
};

const Tab = createBottomTabNavigator<BottomTabParamList>();

const BottomTabNavigator = (appStateType: any) => {
  return (
    <Tab.Navigator initialRouteName={'Main'}>
      <Tab.Screen
        name="Main"
        component={MainScreen}
        options={{ header: () => <MainHeader /> }}
      />

      <Tab.Screen
        name="ScheduleMain"
        component={ScheduleMainScreen}
        options={{ headerShown: false }}
      />
    </Tab.Navigator>
  );
};

export default BottomTabNavigator;
