class UserConvertor {
  fromDto = (dtoObj, uiObj) => {
    console.log(dtoObj);
    uiObj.user_id = dtoObj.getUserId != undefined ? dtoObj.getUserId() : "";
    uiObj.email = dtoObj.getEmail != undefined ? dtoObj.getEmail() : "";
    uiObj.password = dtoObj.getPassword != undefined ? dtoObj.getPassword() : "";
    uiObj.avatar = dtoObj.getAvatar != undefined ? dtoObj.getAvatar() : "";
    uiObj.first_name = dtoObj.getFirstName != undefined ? dtoObj.getFirstName() : "";
    uiObj.middle_name = dtoObj.getMiddleName != undefined ? dtoObj.getMiddleName() : "";
    uiObj.last_name = dtoObj.getLastName != undefined ? dtoObj.getLastName() : "";
    uiObj.nick_name = dtoObj.getNickName != undefined ? dtoObj.getNickName() : "";
    uiObj.cell_phone = dtoObj.getCellPhone != undefined ? dtoObj.getCellPhone() : "";
    uiObj.home_phone = dtoObj.getHomePhone != undefined ? dtoObj.getHomePhone() : "";
    uiObj.work_phone = dtoObj.getWorkPhone != undefined ? dtoObj.getWorkPhone() : "";
    uiObj.passport_number = dtoObj.getPassportNumber != undefined ? dtoObj.getPassportNumber() : "";
    uiObj.date_of_birth = dtoObj.getDateOfBirth != undefined ? dtoObj.getDateOfBirth() : "";
    uiObj.employment_date = dtoObj.getEmploymentDate != undefined ? dtoObj.getEmploymentDate() : "";
    uiObj.actual_employment_date = dtoObj.getActualEmploymentDate != undefined ? dtoObj.getActualEmploymentDate() : "";
    uiObj.dismissal_date = dtoObj.getDismissalDate != undefined ? dtoObj.getDismissalDate() : "";
    uiObj.actual_dismissal_date=dtoObj.getActualDismissalDate != undefined ? dtoObj.getActualDismissalDate() : "";
    uiObj.tax_payer_id = dtoObj.getTaxPayerId != undefined ? dtoObj.getTaxPayerId() : "";
    uiObj.diploma_number = dtoObj.getDiplomaNumber != undefined ? dtoObj.getDiplomaNumber() : "";
    uiObj.diploma_date = dtoObj.getDiplomaDate != undefined ? dtoObj.getDiplomaDate() : "";
    uiObj.retirement_id_number = dtoObj.getRetirementIdNumber != undefined ? dtoObj.getRetirementIdNumber() : "";
    uiObj.retirement_date = dtoObj.getRetirementDate != undefined ? dtoObj.getRetirementDate() : "";
    uiObj.medical_book = dtoObj.getMedicalBook != undefined ? dtoObj.getMedicalBook() : "";
    uiObj.medical_book_date = dtoObj.getMedicalBookDate != undefined ? dtoObj.getMedicalBookDate() : "";
    uiObj.employment_book_number = dtoObj.getEmploymentBookNumber != undefined ? dtoObj.getEmploymentBookNumber() : "";
    uiObj.organization_id = dtoObj.getOrganizationId != undefined ? dtoObj.getOrganizationId() : "";
    uiObj.address_id = dtoObj.getAddressId != undefined ? dtoObj.getAddressId() : "";
    uiObj.permission_names = dtoObj.getPermissionNames != undefined ? dtoObj.getPermissionNames() : "";
    uiObj.positions = dtoObj.getPositionsList != undefined ? dtoObj.getPositionsList() : new Map();
    uiObj.create_datetime = dtoObj.getCreateDate != undefined ? dtoObj.getCreateDate() : "";
  }

  toDto = (uiObj, dtoObj) => {
     dtoObj.getUserId(uiObj.user_id);
     dtoObj.getEmail(uiObj.email);
     dtoObj.getPassword(uiObj.password);
     dtoObj.getAvatar(uiObj.avatar);
     dtoObj.getFirstName(uiObj.first_name);
     dtoObj.getMiddleName(uiObj.middle_name);
     dtoObj.getLastName(uiObj.last_name);
     dtoObj.getNickName(uiObj.nick_name);
     dtoObj.getCellPhone(uiObj.cell_phone);
     dtoObj.getHomePhone(uiObj.home_phone);
     dtoObj.getWorkPhone(uiObj.work_phone);
     dtoObj.getPassportNumber(uiObj.passport_number);
     dtoObj.getDateOfBirth(uiObj.date_of_birth);
     dtoObj.getEmploymentDate(uiObj.employment_date);
     dtoObj.getActualEmploymentDate(uiObj.actual_employment_date);
     dtoObj.getDismissalDate(uiObj.dismissal_date);
     dtoObj.getActualDismissalDate(uiObj.actual_dismissal_date);
     dtoObj.getTaxPayerId(uiObj.tax_payer_id);
     dtoObj.getDiplomaNumber(uiObj.diploma_number);
     dtoObj.getDiplomaDate(uiObj.diploma_date);
     dtoObj.getRetirementIdNumber(uiObj.retirement_id_number);
     dtoObj.getRetirementDate(uiObj.retirement_date);
     dtoObj.getMedicalBook(uiObj.medical_book);
     dtoObj.getMedicalBookDate(uiObj.medical_book_date);
     dtoObj.getEmploymentBookNumber(uiObj.employment_book_number);
     dtoObj.getOrganizationId(uiObj.organization_id);
     dtoObj.getAddressId(uiObj.address_id);
     dtoObj.getPermissionNames(uiObj.permission_names);
     dtoObj.getPositions(uiObj.positions);
     dtoObj.getCreateDate(uiObj.create_datetime);
  }
}

export default UserConvertor;
