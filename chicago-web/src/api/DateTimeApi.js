export default class DateTimeApi {
  dateToUnixUTC(date) {
    try {
      let standardDate = new Date(date + ' 0:0:0');
      return Date.UTC(standardDate.getFullYear(), standardDate.getMonth(), standardDate.getDate());
    } catch (e) {
      console.log("Date time error. Ignore")
    }
  }
}
