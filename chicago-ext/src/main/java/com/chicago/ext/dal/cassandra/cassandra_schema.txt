CREATE KEYSPACE ChicagoErp  WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 3 };

CREATE TABLE IF NOT EXISTS ChicagoErp.Holdings (
    holding_id uuid PRIMARY KEY,
    name varchar,
    description varchar,
    web_site varchar,
    email_domain varchar,
    phones set<varchar>,
    fax set<varchar>,
    users set<uuid>,
    companies set<uuid>,
    address_id uuid,
    create_datetime timestamp);

CREATE TABLE IF NOT EXISTS ChicagoErp.Companies(
    company_id uuid PRIMARY KEY,
    name varchar,
    description varchar,
    web_site varchar,
    email_domain varchar,
    holding_id uuid,
    phones set<varchar>,
    fax set<varchar>,
    users set<uuid>,
    branches set<uuid>,
    address_id uuid,
    create_datetime timestamp);

CREATE TABLE IF NOT EXISTS ChicagoErp.Branches(
    branch_id uuid PRIMARY KEY,
    name varchar,
    description varchar,
    company_id uuid,
    phones set<varchar>,
    fax set<varchar>,
    users set<uuid>,
    address_id uuid,
    create_datetime timestamp);

CREATE TABLE IF NOT EXISTS ChicagoErp.Users (
    user_id uuid PRIMARY KEY,
    email varchar,
    password_hash varchar,
    password_salt blob,
    avatar blob,
    first_name varchar,
    middle_name varchar,
    last_name varchar,
    nick_name varchar,
    cell_phone varchar,
    home_phone varchar,
    work_phone varchar,
    passport_number varchar,
    date_of_birth timestamp,
    employment_date timestamp,
    actual_employment_date timestamp,
    dismissal_date timestamp,
    actual_dismissal_date timestamp,
    tax_payer_id varchar,
    diploma_number varchar,
    diploma_date timestamp,
    retirement_id_number varchar,
    retirement_date timestamp,
    medical_book varchar,
    medical_book_date timestamp,
    employment_book_number varchar,
    organization_id uuid,
    address_id uuid,
    positions set<uuid>,
    permissions set<varchar>,
    create_datetime timestamp);

CREATE MATERIALIZED VIEW ChicagoErp.UsersByEmail AS SELECT
    user_id,
    email,
    password_hash,
    password_salt,
    avatar,
    first_name,
    middle_name,
    last_name,
    nick_name,
    cell_phone,
    home_phone,
    work_phone,
    passport_number,
    date_of_birth,
    employment_date,
    actual_employment_date,
    dismissal_date,
    actual_dismissal_date,
    tax_payer_id,
    diploma_number,
    diploma_date,
    retirement_id_number,
    retirement_date,
    medical_book,
    medical_book_date,
    employment_book_number,
    organization_id,
    address_id,
    positions,
    permissions,
    create_datetime
    FROM ChicagoErp.Users
    WHERE email IS NOT NULL
    PRIMARY KEY(email, user_id);

CREATE TABLE IF NOT EXISTS ChicagoErp.Schedule (
    user_id uuid PRIMARY KEY,
    template_id uuid,
    work_date date,
    appointment_id set<uuid>);

CREATE TABLE IF NOT EXISTS ChicagoErp.ScheduleTemplate (
    template_id uuid PRIMARY KEY,
    description varchar,
    monday map<timestamp, timestamp>,
    tuesday map<timestamp, timestamp>,
    wednesday map<timestamp, timestamp>,
    thursday map<timestamp, timestamp>,
    friday map<timestamp, timestamp>,
    saturday map<timestamp, timestamp>,
    sunday map<timestamp, timestamp>,
    create_datetime timestamp);

CREATE TABLE IF NOT EXISTS ChicagoErp.Appointment (
    user_id uuid,
    appointment_date date,
    client_id uuid,
    start_time timestamp,
    duration_min int,
    procedure_id uuid,
    comment varchar,
    create_datetime timestamp,
    PRIMARY KEY (user_id, appointment_date));

CREATE TABLE IF NOT EXISTS ChicagoErp.Procedure (
    procedure_id uuid PRIMARY KEY,
    description varchar);

CREATE TABLE IF NOT EXISTS ChicagoErp.UserAttributes (
    user_id uuid PRIMARY KEY,
    create_datetime timestamp);

CREATE TABLE IF NOT EXISTS ChicagoErp.UserPermissions(
    user_id uuid PRIMARY KEY,
    role_ids set<uuid>,
    extra_permission_ids set<int>);

CREATE TABLE IF NOT EXISTS ChicagoErp.Positions(
    organization_id uuid PRIMARY KEY, -- holding, company, branch id
    positions map<uuid, varchar>);

CREATE TABLE IF NOT EXISTS ChicagoErp.Addresses(
    address_id uuid PRIMARY KEY, -- uses: user_id, company_id, customer_id, holding_id, branch_id
    street_name varchar,
    house_number varchar,
    building_info varchar,
    office_apt_number varchar,
    city varchar,
    place_name varchar,
    county varchar,
    state varchar,
    zip_code varchar,
    country varchar);

CREATE TABLE IF NOT EXISTS ChicagoErp.Customers (
    customer_id uuid PRIMARY KEY,
    avatar blob,
    first_name varchar,
    middle_name varchar,
    last_name varchar,
    phone varchar,
    address_id uuid);

CREATE TABLE IF NOT EXISTS ChicagoErp.Permissions (
    permission_id int PRIMARY KEY,
    permission_name varchar,
    description varchar);
INSERT INTO ChicagoErp.Permissions (permission_id, permission_name, description) VALUES(0, '*:*:*', 'Can do anything');

INSERT INTO ChicagoErp.Permissions (permission_id, permission_name, description) VALUES(1, 'user:create', 'Can create the new users');
INSERT INTO ChicagoErp.Permissions (permission_id, permission_name, description) VALUES(2, 'user:edit', 'Can edit the users');
INSERT INTO ChicagoErp.Permissions (permission_id, permission_name, description) VALUES(3, 'user:delete', 'Can delete the users');

INSERT INTO ChicagoErp.Permissions (permission_id, permission_name, description) VALUES(4, 'customer:create', 'Can create the new customer');
INSERT INTO ChicagoErp.Permissions (permission_id, permission_name, description) VALUES(5, 'customer:edit', 'Can edit the customer');
INSERT INTO ChicagoErp.Permissions (permission_id, permission_name, description) VALUES(6, 'customer:delete', 'Can delete the customer');

INSERT INTO ChicagoErp.Permissions (permission_id, permission_name, description) VALUES(7, 'company:create:branch', 'Can create new branch inside the company');
INSERT INTO ChicagoErp.Permissions (permission_id, permission_name, description) VALUES(8, 'company:edit:branch', 'Can edit branch inside the company');
INSERT INTO ChicagoErp.Permissions (permission_id, permission_name, description) VALUES(9, 'company:delete:branch', 'Can delete branch from the company');

INSERT INTO ChicagoErp.Permissions (permission_id, permission_name, description) VALUES(10, 'holding:create', 'Can create the new holding');
INSERT INTO ChicagoErp.Permissions (permission_id, permission_name, description) VALUES(11, 'holding:edit', 'Can edit the holding');
INSERT INTO ChicagoErp.Permissions (permission_id, permission_name, description) VALUES(12, 'holding:delete', 'Can delete the holding');

INSERT INTO ChicagoErp.Permissions (permission_id, permission_name, description) VALUES(13, 'holding:create:company', 'Can create the new company inside the holding');
INSERT INTO ChicagoErp.Permissions (permission_id, permission_name, description) VALUES(14, 'holding:edit:company', 'Can edit the company inside the holding');
INSERT INTO ChicagoErp.Permissions (permission_id, permission_name, description) VALUES(15, 'holding:delete:company', 'Can delete the company inside the holding');

CREATE TABLE IF NOT EXISTS ChicagoErp.Roles (
    role_id uuid PRIMARY KEY,
    role_name varchar,
    permission_ids set<int>,
    description varchar);
INSERT INTO ChicagoErp.Roles (role_id, role_name, permission_ids, description) VALUES(uuid(), 'system:admin',
{
0 -- *:*:*
}, 'System level admin. God mode');
INSERT INTO ChicagoErp.Roles (role_id, role_name, permission_ids, description) VALUES(uuid(), 'holding:admin',
{
1, --user:create
2, --user:edit
3  --user:delete
}, 'Holding level admin');
INSERT INTO ChicagoErp.Roles (role_id, role_name, permission_ids, description) VALUES(uuid(), 'company:admin',
{
1,  --user:create
2,  --user:edit
3,  --user:delete
4,  --customer:create
5,  --customer:edit
6,  --customer:delete
7,  --company:create:branch
8,  --company:edit:branch
9,  --company:delete:branch
10, --holding:create
11, --holding:edit
12  --holding:delete
}, 'Company level admin');
INSERT INTO ChicagoErp.Roles (role_id, role_name, permission_ids, description) VALUES(uuid(), 'branch:admin',
{
1,  --user:create
2,  --user:edit
3,  --user:delete
4,  --customer:create
5,  --customer:edit
6   --customer:delete
}, 'Branch level admin');
