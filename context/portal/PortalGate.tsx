import * as React from "react";
import PortalContext from "./PortalContext";

function PortalGate(props: {
  gateName: string;
  children?: (
    teleport: (gateName: string, element: JSX.Element) => void
  ) => JSX.Element;
}) {
  const { gateName, children } = props;
  return (
    <PortalContext.Consumer>
      {value => {
        return (
          <React.Fragment>
            {/* {value.gates[gateName]} */}
            {children && children(value.teleport)}
          </React.Fragment>
        );
      }}
    </PortalContext.Consumer>
  );
}

export default PortalGate;
