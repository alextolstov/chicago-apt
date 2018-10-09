import React, { Component } from 'react';
import { Button, Card, CardBody, CardHeader, CardFooter } from 'reactstrap';
import { Responsive, WidthProvider } from 'react-grid-layout';
import 'react-grid-layout/css/styles.css'
import 'react-resizable/css/styles.css'
import './Draggable.css'
import defaultLayouts from './_layouts';

const ResponsiveGridLayout = WidthProvider(Responsive);

class Draggable extends Component {

  constructor(props) {
    super(props);

    this.state = {
      layouts: JSON.parse(localStorage.getItem('CoreUI-Draggable-Layouts') || JSON.stringify(defaultLayouts))
    };
  }

  resetLayout() {
    this.setState({ layouts: JSON.parse(JSON.stringify(defaultLayouts)) });
  }

  onLayoutChange(layout, layouts) {
    localStorage.setItem('CoreUI-Draggable-Layouts', JSON.stringify(layouts))
    this.setState({ layouts });
  }

  render() {

    const loremIpsum = 'Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat.'

    return (
      <div className="animated fadeIn">
        <ResponsiveGridLayout className="layout" layouts={this.state.layouts}
                              onLayoutChange={(layout, layouts) =>
                                this.onLayoutChange(layout, layouts)
                              }
                              breakpoints={{lg: 1200, md: 992, sm: 768, xs: 576, xxs: 0}}
                              cols={{lg: 3, md: 3, sm: 3, xs: 2, xxs: 1}}
                              isResizable={false}
                              measureBeforeMount={false}
                              draggableHandle={".card-header"}>
          <Card key="a" className="card-accent-primary">
            <CardHeader>
              <i className="cui-layers"></i>
              Static Card 1 <a href="https://coreui.io/pro/react/" className="badge badge-danger">CoreUI Pro Component</a>
            </CardHeader>
            <CardBody>
              {loremIpsum}
            </CardBody>
            <CardFooter>
              <Button onClick={() => this.resetLayout()}>Reset Layout</Button>
            </CardFooter>
          </Card>
          <Card key="b" className="card-accent-secondary">
            <CardHeader>
              <i className="cui-cursor-move"></i>
              Drag & Drop Card 2 <a href="https://coreui.io/pro/react/" className="badge badge-danger">CoreUI Pro Component</a>
            </CardHeader>
            <CardBody>
              {loremIpsum}
            </CardBody>
          </Card>
          <Card key="c" className="card-accent-success">
            <CardHeader>
              <i className="cui-cursor-move"></i>
              Drag & Drop Card 3 <a href="https://coreui.io/pro/react/" className="badge badge-danger">CoreUI Pro Component</a>
            </CardHeader>
            <CardBody>
              {loremIpsum}
            </CardBody>
          </Card>
          <Card key="d" className="card-accent-info">
            <CardHeader>
              <i className="cui-cursor-move"></i>
              Drag & Drop Card 4 <a href="https://coreui.io/pro/react/" className="badge badge-danger">CoreUI Pro Component</a>
            </CardHeader>
            <CardBody>
              {loremIpsum}
            </CardBody>
          </Card>
          <Card key="e" className="card-accent-warning">
            <CardHeader>
              <i className="cui-cursor-move"></i>
              Drag & Drop Card 5 <a href="https://coreui.io/pro/react/" className="badge badge-danger">CoreUI Pro Component</a>
            </CardHeader>
            <CardBody>
              {loremIpsum}
            </CardBody>
          </Card>
          <Card key="f" className="card-accent-danger">
            <CardHeader>
              <i className="cui-cursor-move"></i>
              Drag & Drop Card 6 <a href="https://coreui.io/pro/react/" className="badge badge-danger">CoreUI Pro Component</a>
            </CardHeader>
            <CardBody>
              {loremIpsum}
            </CardBody>
          </Card>
        </ResponsiveGridLayout>
      </div>
    )
  }
}

export default Draggable
