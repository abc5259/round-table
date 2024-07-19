import {
  Image,
  ImageSourcePropType,
  StyleSheet,
  Text,
  View,
} from "react-native";
import { Category } from "../../../type/Chore";
import * as Styled from "./Styled";

type Props = {
  isCompleted: boolean;
  name: string;
  category: Category;
  startTime: string;
};

interface CategoryInfo {
  value: string;
  url: ImageSourcePropType;
}

const categories: Record<Category, CategoryInfo> = {
  LAUNDRY: {
    value: "세탁하기",
    url: require("../../../assets/images/basket.png"),
  },
  COOKING: {
    value: "요리하기",
    url: require("../../../assets/images/cooking.png"),
  },
  CLEANING: {
    value: "청소하기",
    url: require("../../../assets/images/bucket.png"),
  },
  TRASH: {
    value: "쓰레기 버리기",
    url: require("../../../assets/images/trash.png"),
  },
  GROCERY: {
    value: "장보기",
    url: require("../../../assets/images/grocery.png"),
  },
  ONE_TIME: {
    value: "일회성 이벤트",
    url: require("../../../assets/images/one-time.png"),
  },
};

const ChoreCard = ({ isCompleted, name, category, startTime }: Props) => {
  return (
    <Styled.Wrapper>
      <Styled.Badge isCompleted={isCompleted}>
        <Styled.BadgeText isCompleted={isCompleted}>
          {isCompleted ? "완료" : "미완료"}
        </Styled.BadgeText>
      </Styled.Badge>

      <View style={{ gap: 4 }}>
        <Styled.Name>{name}</Styled.Name>
        <Styled.Sub>{startTime}분</Styled.Sub>
      </View>
      <Styled.CategoryWrapper>
        <Image
          style={{
            width: 97,
            height: 97,
          }}
          source={categories[category].url}
        />
      </Styled.CategoryWrapper>
      <Styled.Button
        style={{ backgroundColor: isCompleted ? "#D3D9E3" : "#53A5F8" }}
      >
        <Text style={{ color: "#FFF", fontWeight: "bold" }}>
          {isCompleted ? "완료됨" : "완료하기"}
        </Text>
      </Styled.Button>
    </Styled.Wrapper>
  );
};

export default ChoreCard;
