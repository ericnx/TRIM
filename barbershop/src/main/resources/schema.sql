-- Drop in reverse dependency order
DROP TABLE IF EXISTS appointment;
DROP TABLE IF EXISTS slot;
DROP TABLE IF EXISTS schedule;
DROP TABLE IF EXISTS barber_service;
DROP TABLE IF EXISTS client;
DROP TABLE IF EXISTS barber;
DROP TABLE IF EXISTS person;

-- Base person table
CREATE TABLE person (
    person_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name  VARCHAR(100) NOT NULL,
    last_name   VARCHAR(100) NOT NULL,
    phone       VARCHAR(20),
    email       VARCHAR(150) NOT NULL UNIQUE
);

-- Client subtype
CREATE TABLE client (
    client_id BIGINT PRIMARY KEY,  -- FK to person
    FOREIGN KEY (client_id) REFERENCES person(person_id)
);

-- Barber subtype  (role = 'staff' or 'admin')
CREATE TABLE barber (
    barber_id  BIGINT PRIMARY KEY,  -- FK to person
    license_no VARCHAR(50) NOT NULL UNIQUE,
    role       VARCHAR(10) NOT NULL CHECK (role IN ('staff', 'admin')),
    FOREIGN KEY (barber_id) REFERENCES person(person_id)
);

-- Services offered
CREATE TABLE barber_service (
    service_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    type       VARCHAR(100)   NOT NULL,
    price      DECIMAL(8, 2)  NOT NULL,
    duration   INT            NOT NULL  -- minutes
);

-- Weekly schedule: one row per barber per shift day
-- admin_id must reference a barber with role = 'admin'
CREATE TABLE schedule (
    schedule_id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    admin_id         BIGINT NOT NULL,
    staff_id         BIGINT NOT NULL,
    day_of_week      VARCHAR(10) NOT NULL,
    shift_start_time TIME NOT NULL,
    shift_end_time   TIME NOT NULL,
    FOREIGN KEY (admin_id) REFERENCES barber(barber_id),
    FOREIGN KEY (staff_id) REFERENCES barber(barber_id)
);

-- Slot: weak entity, PK = (schedule_id, slot_start_time)
CREATE TABLE slot (
    schedule_id     BIGINT NOT NULL,
    slot_start_time TIME   NOT NULL,
    slot_end_time   TIME,
    status          VARCHAR(15) NOT NULL DEFAULT 'AVAILABLE',
    date            DATE NOT NULL,
    PRIMARY KEY (schedule_id, slot_start_time),
    FOREIGN KEY (schedule_id) REFERENCES schedule(schedule_id)
);

-- Appointment: weak entity
-- PK = (client_id, service_id, schedule_id, slot_start_time)
CREATE TABLE appointment (
    client_id       BIGINT        NOT NULL,
    service_id      BIGINT        NOT NULL,
    schedule_id     BIGINT        NOT NULL,
    slot_start_time TIME          NOT NULL,
    status          VARCHAR(15)   NOT NULL DEFAULT 'PENDING',
    current_price   DECIMAL(8, 2) NOT NULL,
    PRIMARY KEY (client_id, service_id, schedule_id, slot_start_time),
    FOREIGN KEY (client_id)                          REFERENCES client(client_id),
    FOREIGN KEY (service_id)                         REFERENCES barber_service(service_id),
    FOREIGN KEY (schedule_id, slot_start_time)       REFERENCES slot(schedule_id, slot_start_time)
);
