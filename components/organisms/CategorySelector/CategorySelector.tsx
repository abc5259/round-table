import {
  Image,
  ImageSourcePropType,
  ScrollView,
  Text,
  View,
} from "react-native";
import * as Styled from "./Styled";
import { useState } from "react";
import { RepeateCategory } from "../../../type/Chore";

//COOKING, CLEANING, LAUNDRY, TRASH, GROCERY, ONE_TIME

// const category = ["빨래하기", "청소하기", "요리하기", "쓰레기 버리기", "장보기"]

// 각 카테고리의 타입 정의
interface CategoryInfo {
  value: string;
  url: ImageSourcePropType;
}

const categories: Record<RepeateCategory, CategoryInfo> = {
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
};

const keys = Object.keys(categories) as (keyof typeof categories)[];

type Props = {
  changeCategory: (category: RepeateCategory | null) => void;
};

const CategorySelector = ({ changeCategory }: Props) => {
  const [selectedCategory, setSelectedCategory] =
    useState<RepeateCategory | null>(null);

  const onChangeCategory = (category: RepeateCategory) => {
    if (selectedCategory === category) {
      setSelectedCategory(null);
      changeCategory(null);
      return;
    }

    setSelectedCategory(category);
    changeCategory(category);
  };

  return (
    <Styled.Wrapper>
      <ScrollView horizontal={true} showsHorizontalScrollIndicator={false}>
        <View style={{ flexDirection: "row", gap: 10 }}>
          {keys.map(key => (
            <Styled.Box
              key={key}
              onPress={() => onChangeCategory(key)}
              isSelected={selectedCategory === key}
            >
              <Image
                style={{
                  width: 80,
                  height: 80,
                }}
                source={categories[key].url}
              />
              <Text
                style={{ color: "#B2B6BF", fontSize: 13, fontWeight: "bold" }}
              >
                {categories[key].value}
              </Text>
            </Styled.Box>
          ))}
        </View>
      </ScrollView>
    </Styled.Wrapper>
  );
};

export default CategorySelector;
