import React from 'react';
import ReactDOM from 'react-dom';
import ListUsers from './ListUsers';

jest.mock('react-chartjs-2', () => ({
    Line: () => null,
}));

it('renders without crashing', () => {
    const div = document.createElement('div');
    ReactDOM.render(<ListUsers />, div);
    ReactDOM.unmountComponentAtNode(div);
});
