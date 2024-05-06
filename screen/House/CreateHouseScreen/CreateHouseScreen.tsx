import CreateHouseName from "../../../components/template/CreateHouseName/CreateHouseName";
import HouseInviteEmail from "../../../components/template/HouseInviteEmail/HouseInviteEmail";
import { useHouseAppenderStore } from "../../../store/house/houseAppenderStore";

const CreateHouseScreen = () => {
  const { step } = useHouseAppenderStore();

  return (
    <>
      {step == 1 && <CreateHouseName />}
      {step == 2 && <HouseInviteEmail />}
    </>
  );
};

export default CreateHouseScreen;
