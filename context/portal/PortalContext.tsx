import * as React from "react";

const PortalContext = React.createContext({
  gates: {},
  teleport: (gateName: string, element: JSX.Element) => {
    return;
  },
});

export default PortalContext;
