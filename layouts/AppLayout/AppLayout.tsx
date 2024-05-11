import { PropsWithChildren } from "react";
import * as Styled from "./Styled";

type Props = {};

const AppLayout = ({ children }: PropsWithChildren<Props>) => {
  return <Styled.Wrapper>{children}</Styled.Wrapper>;
};

export default AppLayout;
