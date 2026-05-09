DROP TABLE IF EXISTS appointment;
DROP TABLE IF EXISTS slot;
DROP TABLE IF EXISTS schedule;
DROP TABLE IF EXISTS barber_services_mapping; 
DROP TABLE IF EXISTS services;
DROP TABLE IF EXISTS client;
DROP TABLE IF EXISTS barber;
DROP TABLE IF EXISTS person;

CREATE TABLE person (
    person_id   BIGSERIAL PRIMARY KEY,
    first_name  VARCHAR(100) NOT NULL,
    last_name   VARCHAR(100) NOT NULL,
    phone       VARCHAR(20),
    email       VARCHAR(150) NOT NULL UNIQUE
);

CREATE TABLE services (
    service_id BIGSERIAL PRIMARY KEY,
    type       VARCHAR(100)   NOT NULL,
    price      DECIMAL(8, 2)  NOT NULL,
    duration   INT            NOT NULL
);

CREATE TABLE client (
    client_id BIGINT PRIMARY KEY,
    FOREIGN KEY (client_id) REFERENCES person(person_id)
);

CREATE TABLE barber (
    barber_id  BIGINT PRIMARY KEY,
    license_no VARCHAR(50) NOT NULL UNIQUE,
    role       VARCHAR(10) NOT NULL CHECK (role IN ('staff', 'admin')),
    FOREIGN KEY (barber_id) REFERENCES person(person_id)
);

CREATE TABLE barber_services_mapping (
    barber_id  BIGINT NOT NULL,
    service_id BIGINT NOT NULL,
    PRIMARY KEY (barber_id, service_id),
    FOREIGN KEY (barber_id) REFERENCES barber(barber_id),
    FOREIGN KEY (service_id) REFERENCES services(service_id)
);

CREATE TABLE schedule (
    schedule_id      BIGSERIAL PRIMARY KEY,
    admin_id         BIGINT NOT NULL,
    staff_id         BIGINT NOT NULL,
    day_of_week      VARCHAR(3) NOT NULL,
    shift_start_time TIME NOT NULL,
    shift_end_time   TIME NOT NULL,
    FOREIGN KEY (admin_id) REFERENCES barber(barber_id),
    FOREIGN KEY (staff_id) REFERENCES barber(barber_id)
);

CREATE TABLE slot (
    schedule_id     BIGINT NOT NULL,
    slot_start_time TIME   NOT NULL,
    date            DATE   NOT NULL,
    slot_end_time   TIME,
    status          VARCHAR(15) NOT NULL DEFAULT 'AVAILABLE',
    version         INT DEFAULT 0,
    PRIMARY KEY (schedule_id, slot_start_time, date), 
    FOREIGN KEY (schedule_id) REFERENCES schedule(schedule_id)
);

CREATE TABLE appointment (
    client_id       BIGINT        NOT NULL,
    schedule_id     BIGINT        NOT NULL,
    slot_start_time TIME          NOT NULL,
    date            DATE          NOT NULL, 
    service_id      BIGINT        NOT NULL,
    status          VARCHAR(15)   NOT NULL DEFAULT 'PENDING',
    current_price   DECIMAL(8, 2) NOT NULL,
    PRIMARY KEY (client_id, schedule_id, slot_start_time, date),
    UNIQUE (schedule_id, slot_start_time, date),
    FOREIGN KEY (client_id) REFERENCES client(client_id),
    FOREIGN KEY (service_id) REFERENCES services(service_id),
    FOREIGN KEY (schedule_id, slot_start_time, date) 
        REFERENCES slot(schedule_id, slot_start_time, date)
);