import { Text, View } from "react-native";
import * as Styled from "./Styled";
import { WithLocalSvg } from "react-native-svg/css";

const MainHeader = () => {
  return (
    <Styled.Wrapper>
      <View style={{ flexDirection: "row", gap: 5, alignItems: "center" }}>
        <WithLocalSvg asset={require("../../../assets/vectors/icon.svg")} />
        <Text style={{ fontWeight: "bold", fontSize: 20, color: "#C0C5CC" }}>
          RoundTable
        </Text>
      </View>
      <View>
        <WithLocalSvg asset={require("../../../assets/vectors/alarm.svg")} />
      </View>
    </Styled.Wrapper>
  );
};

export default MainHeader;
