import AppLayout from "../../../layouts/AppLayout/AppLayout";
import CreateOneTimeSchedule from "../../../components/template/CreateOneTimeSchedule/CreateOneTimeSchedule";
import useMe from "../../../hooks/queries/member/useMe";
import useHouseMembers from "../../../hooks/queries/house/useHouseMembers";
import { ActivityIndicator, View } from "react-native";
import Button from "../../../components/atoms/Button/Button";
import { useState } from "react";

type ScheduleType = "once" | "repeat";

const CreateScheduleScreen = () => {
  const [scehduleType, setScheduleType] = useState<ScheduleType>("once");

  return (
    <AppLayout>
      {scehduleType === "once" && <CreateOneTimeSchedule />}
      <Button>완료</Button>
    </AppLayout>
  );
};

export default CreateScheduleScreen;
