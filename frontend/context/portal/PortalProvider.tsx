import * as React from "react";
import PortalContext from "./PortalContext";

interface IProps {
  children: JSX.Element;
}

class PortalProvider extends React.Component<IProps> {
  public state = {
    gates: {},
  };

  public render() {
    const { children } = this.props;
    return (
      <PortalContext.Provider
        value={{ gates: this.state.gates, teleport: this.teleport }}
      >
        {children}
      </PortalContext.Provider>
    );
  }

  private teleport = (gateName: string, element: JSX.Element) =>
    this.setState({ gates: { ...this.state.gates, [gateName]: element } });
}

export default PortalProvider;
