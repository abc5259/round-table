import CategorySelector, {
  Category,
} from "../../organisms/CategorySelector/CategorySelector";
import * as Styled from "./Styled";

const CreateRepeatScheduleForm = () => {
  const changeCategory = (cateogory: Category | null) => {
    console.log(cateogory);
  };
  return (
    <Styled.Wrapper>
      <CategorySelector changeCategory={changeCategory} />
    </Styled.Wrapper>
  );
};

export default CreateRepeatScheduleForm;
