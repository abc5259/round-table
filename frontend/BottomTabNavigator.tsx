import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import MainScreen from './screen/Main/MainScreen';
import MainHeader from './components/molecules/MainHeader/MainHeader';

export type BottomTabParamList = {
  CreateSchedule: undefined;
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
    </Tab.Navigator>
  );
};

export default BottomTabNavigator;
