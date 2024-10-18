import { NavigationProp, RouteProp } from "@react-navigation/native";
import CreateHouseComposition from "../../../components/template/CreateHouseComposition/CreateHouseComposition";
import CreateHouseName from "../../../components/template/CreateHouseName/CreateHouseName";
import HouseInviteEmail from "../../../components/template/HouseInviteEmail/HouseInviteEmail";
import { useHouseAppenderStore } from "../../../store/house/houseAppenderStore";
import { RootStackParamList } from "../../../App";

const CreateHouseScreen = () => {
  const { step } = useHouseAppenderStore();

  return (
    <>
      {step == 1 && <CreateHouseName />}
      {step == 2 && <HouseInviteEmail />}
      {step == 3 && <CreateHouseComposition />}
    </>
  );
};

export default CreateHouseScreen;
