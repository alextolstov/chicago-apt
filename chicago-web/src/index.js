import './polyfill'
import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import registerServiceWorker from './registerServiceWorker';
import config from 'react-global-configuration';
import configuration from './config';

config.set(configuration);
ReactDOM.render(<App />, document.getElementById('root'));
registerServiceWorker();
