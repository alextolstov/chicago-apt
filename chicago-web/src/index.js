import './polyfill'
import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';

import config from 'react-global-configuration';
import configuration from './config';
import {IntlProvider, addLocaleData} from "react-intl";
import locale_en from 'react-intl/locale-data/en';
import locale_ru from 'react-intl/locale-data/ru';
//import messages_en from "./translations/en.json";
import messages_ru from "./translations/ru.json";

const messages = {
  'ru': messages_ru,
//  'en': messages_en
};

// language without region code
export const language = navigator.language.split(/[-_]/)[0];

// Activate locales
addLocaleData([...locale_en, ...locale_ru]);
// Create global storage for future use
config.set(configuration);

ReactDOM.render(<IntlProvider locale={language} messages={messages[language]}><App /></IntlProvider>, document.getElementById('root'));
// disable ServiceWorker
// registerServiceWorker();
