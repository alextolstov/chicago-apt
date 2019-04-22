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
     dtoObj.setUserId(uiObj.user_id);
     dtoObj.setEmail(uiObj.email);
     dtoObj.setPassword(uiObj.password);
     dtoObj.setAvatar(uiObj.avatar);
     dtoObj.setFirstName(uiObj.first_name);
     dtoObj.setMiddleName(uiObj.middle_name);
     dtoObj.setLastName(uiObj.last_name);
     dtoObj.setNickName(uiObj.nick_name);
     dtoObj.setCellPhone(uiObj.cell_phone);
     dtoObj.setHomePhone(uiObj.home_phone);
     dtoObj.setWorkPhone(uiObj.work_phone);
     dtoObj.setPassportNumber(uiObj.passport_number);
     dtoObj.setDateOfBirth(uiObj.date_of_birth);
     dtoObj.setEmploymentDate(uiObj.employment_date);
     dtoObj.setActualEmploymentDate(uiObj.actual_employment_date);
     dtoObj.setDismissalDate(uiObj.dismissal_date);
     dtoObj.setActualDismissalDate(uiObj.actual_dismissal_date);
     dtoObj.setTaxPayerId(uiObj.tax_payer_id);
     dtoObj.setDiplomaNumber(uiObj.diploma_number);
     dtoObj.setDiplomaDate(uiObj.diploma_date);
     dtoObj.setRetirementIdNumber(uiObj.retirement_id_number);
     dtoObj.setRetirementDate(uiObj.retirement_date);
     dtoObj.setMedicalBook(uiObj.medical_book);
     dtoObj.setMedicalBookDate(uiObj.medical_book_date);
     dtoObj.setEmploymentBookNumber(uiObj.employment_book_number);
     dtoObj.setOrganizationId(uiObj.organization_id);
     dtoObj.setAddressId(uiObj.address_id);
     let posMap = dtoObj.getPositionsMap();
    for (let i in uiObj.positions) {
      posMap[i] = uiObj.positions[i];
    }
     dtoObj.setCreateDatetime(uiObj.create_datetime);
  }
}

export default UserConvertor;
