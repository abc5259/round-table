import CreateHouseName from "../../../components/template/CreateHouseName/CreateHouseName";
import { useHouseAppenderStore } from "../../../store/house/houseAppenderStore";

const CreateHouseScreen = () => {
  const { step } = useHouseAppenderStore();

  return (
    <>
      {step == 1 && <CreateHouseName />}
      {step == 2 && <CreateHouseName />}
    </>
  );
};

export default CreateHouseScreen;
