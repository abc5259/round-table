import { Image, Pressable, Text, View } from "react-native";
import * as Styled from "./Styled";
import { WithLocalSvg } from "react-native-svg/css";

type Props = {
  url?: string;
  name?: string;
  isSelected?: boolean;
  onPressContainer: () => void;
};

const Profile = ({
  url,
  name,
  isSelected = false,
  onPressContainer,
}: Props) => {
  return (
    <Pressable
      onPress={onPressContainer}
      style={{ width: 62, gap: 10, position: "relative" }}
    >
      <Styled.Wrapper isDefault={url == null}>
        {url == null ? (
          <WithLocalSvg
            width={32}
            height={32}
            asset={require("../../../assets/vectors/default-profile.svg")}
          />
        ) : (
          <Image
            source={{ uri: url }}
            width={62}
            height={62}
            borderRadius={50}
          />
        )}
      </Styled.Wrapper>
      {name && (
        <Text style={{ textAlign: "center", color: "#888585", fontSize: 13 }}>
          {name}
        </Text>
      )}
      {isSelected && (
        <View
          style={{
            position: "absolute",
            width: 62,
            height: 62,
            borderRadius: 50,
            justifyContent: "center",
            alignItems: "center",
            backgroundColor: "rgba(91, 173, 255, 0.7)", // 여기서 투명도 조절
          }}
        >
          <WithLocalSvg
            asset={require("../../../assets/vectors/checked.svg")}
          />
        </View>
      )}
    </Pressable>
  );
};

export default Profile;
