import {language} from '../index';

export default class PersonNameLocalizeApi {
  constructor() {
    this.fio = ['ru', 'uk', 'be'];
  }

  toPersonName = (first, middle, last) => {
    return ((this.fio.indexOf(language) === 0) ? last + ' ' + first + ' ' + middle :
      first + ' ' + middle + (middle.length > 0 ? ' ' : "") + last).trim();
  }

  fromPersonName(fillName) {
    let parts = fillName.split(' ');
    if (parts.length == 1)
      return {first: fillName};
    if (parts.length == 2) {
      return (this.fio.indexOf(language) === 0) ? {last: parts[0], first: parts[1]} : {first: parts[0], last: parts[1]}
    }
    if (parts.length == 3) {
      return (this.fio.indexOf(language) === 0) ? {
        last: parts[0],
        first: parts[1],
        middle: parts[3]
      } : {first: parts[0], middle: parts[1], last: parts[2]}
    }
  }
}

