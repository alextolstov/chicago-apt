export default class FormApi {
  handleFormEnableDisable = (owner_id, event) => {
    let el = document.getElementById(owner_id);
    let buttons = Array.prototype.slice.call(el.getElementsByTagName('button'), 0);
    let inputs = Array.prototype.slice.call(el.getElementsByTagName('input'), 0);
    let all = inputs.concat(buttons);
    for (let i = 0; i < all.length; i++) {
      // Do not block switch
      if (event.target.id !== all[i].id) {
        all[i].disabled = !all[i].disabled;
      }
    }
  }
}
