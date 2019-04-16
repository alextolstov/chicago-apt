class UiUser {
  constructor() {
    this.user_id = "";
    this.email = "";
    this.password = "";
    this.avatar = "";
    this.first_name = "";
    this.middle_name = "";
    this.last_name = "";
    this.nick_name = "";
    this.cell_phone = "";
    this.home_phone = "";
    this.work_phone = "";
    this.passport_number = "";
    this.date_of_birth = 0;
    this.employment_date = 0;
    this.actual_employment_date = 0;
    this.dismissal_date = 0;
    this.actual_dismissal_date=0;
    this.tax_payer_id = 0;
    this.diploma_number = 0;
    this.diploma_date = 0;
    this.retirement_id_number = 0;
    this.retirement_date = 0;
    this.medical_book = 0;
    this.medical_book_date = 0;
    this.employment_book_number = 0;
    this.organization_id = 0;
    this.address_id = 0;
    this.permission_names = [];
    this.positions = new Map();
    this.create_datetime = 0;
  }

  getCopy = () => {
    return Object.assign({}, this);
  }
}

export default UiUser;

