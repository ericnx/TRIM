-- ---- People ----
INSERT INTO person (first_name, last_name, phone, email) VALUES
    ('Andy',   'Nguyen',   '408-555-0001', 'andy@trim.com'),   -- 1 (Admin)
    ('Casi',  'Casillas', '408-555-0002', 'casi@trim.com'),  -- 2 (Barber)
    ('Chamoy',   'Rivers',     '408-555-0003', 'chamoy@trim.com'),   -- 3 (Barber)
    ('Chepe',  'Lowks',     '408-555-0004', 'chepe@trim.com'),  -- 7 (Barber)
    ('Diamonte',  'Fernandez',  '408-555-0005', 'diamonte@trim.com'),  -- 8 (Barber)
    ('Steve',  'Bernal',   '408-555-0006', 'steve@trim.com'),  -- 9 (Barber)
    ('Alice',  'Johnson',  '408-555-1001', 'alice@email.com'), -- 4 (Client)
    ('Bob',    'Smith',    '408-555-1002', 'bob@email.com'),   -- 5 (Client)
    ('Carol',  'Williams', '408-555-1003', 'carol@email.com'), -- 6 (Client)
    ('Grace',  'Ashcroft',      '408-555-2001', 'grace@email.com'), -- 10 (Client)
    ('Henry',  'Ford',     '408-555-2002', 'henry@email.com'), -- 11 (Client)
    ('Isabel', 'Soto',     '408-555-2003', 'isabel@email.com');-- 12 (Client)

-- ---- Barbers ----
INSERT INTO barber (barber_id, license_no, role) VALUES
    (1, 'LIC-001', 'admin'),
    (2, 'LIC-002', 'staff'),
    (3, 'LIC-003', 'staff'),
    (7, 'LIC-004', 'staff'),
    (8, 'LIC-005', 'staff'),
    (9, 'LIC-006', 'staff');

-- ---- Clients ----
INSERT INTO client (client_id) VALUES (4), (5), (6), (10), (11), (12);

-- ---- Services ----
INSERT INTO barber_service (type, price, duration) VALUES
    ('Regular Haircut',                          40.00, 30),
    ('Regular Haircut + Beard',                  45.00, 45),
    ('Regular Haircut + Eyebrows',               45.00, 30),
    ('Regular Haircut + Beard + Eyebrows',       50.00, 45),
    ('Long Hair Scissor Cut',                    45.00, 40),
    ('Long Hair Scissor Cut + Beard',            50.00, 55),
    ('Long Hair Scissor Cut + Eyebrows',         50.00, 40),
    ('Long Hair Scissor Cut + Beard + Eyebrows', 55.00, 55),
    ('Dye Hair',                                 75.00, 90);

-- ---- Schedules ----
-- Casi: Mon 9-3
INSERT INTO schedule (admin_id, staff_id, day_of_week, shift_start_time, shift_end_time)
    VALUES (1, 2, 'MON', '09:00:00', '15:00:00'); -- schedule_id = 1

-- : Wed 12-6
INSERT INTO schedule (admin_id, staff_id, day_of_week, shift_start_time, shift_end_time)
    VALUES (1, 3, 'WED', '12:00:00', '18:00:00'); -- schedule_id = 2

-- Chepe: Fri 10-4
INSERT INTO schedule (admin_id, staff_id, day_of_week, shift_start_time, shift_end_time)
    VALUES (1, 7, 'FRI', '10:00:00', '16:00:00'); -- schedule_id = 3

-- Diamonte: Tue 9-3
INSERT INTO schedule (admin_id, staff_id, day_of_week, shift_start_time, shift_end_time)
    VALUES (1, 8, 'TUE', '09:00:00', '15:00:00'); -- schedule_id = 4

-- Steve: Thu 10-4
INSERT INTO schedule (admin_id, staff_id, day_of_week, shift_start_time, shift_end_time)
    VALUES (1, 9, 'THU', '10:00:00', '16:00:00'); -- schedule_id = 5

-- Dynamic Schedules
-- Casi (MON), Diamonte (TUE),  (WED), Steve (THU), Chepe (FRI)
INSERT INTO schedule (admin_id, staff_id, day_of_week, shift_start_time, shift_end_time) VALUES 
    (1, 2, 'MON', '09:00:00', '15:00:00'), -- id 1
    (1, 8, 'TUE', '09:00:00', '15:00:00'), -- id 2
    (1, 3, 'WED', '12:00:00', '18:00:00'), -- id 3
    (1, 9, 'THU', '10:00:00', '16:00:00'), -- id 4
    (1, 7, 'FRI', '10:00:00', '16:00:00'); -- id 5

-- Dynamic Slots
-- Casi: Monday of the current week
SET @MON = DATEADD('DAY', (1 - DAY_OF_WEEK(CURRENT_DATE)), CURRENT_DATE);
INSERT INTO slot (schedule_id, slot_start_time, slot_end_time, status, date, version) VALUES 
(1, '09:00:00', '09:30:00', 'AVAILABLE', @MON, 0),
(1, '09:30:00', '10:00:00', 'AVAILABLE', @MON, 0),
(1, '10:00:00', '10:30:00', 'AVAILABLE', @MON, 0),
(1, '14:30:00', '15:00:00', 'AVAILABLE', @MON, 0);

-- Diamonte: Tuesday of the current week
SET @TUE = DATEADD('DAY', (2 - DAY_OF_WEEK(CURRENT_DATE)), CURRENT_DATE);
INSERT INTO slot (schedule_id, slot_start_time, slot_end_time, status, date, version) VALUES 
(2, '09:00:00', '09:30:00', 'AVAILABLE', @TUE, 0),
(2, '10:00:00', '10:30:00', 'AVAILABLE', @TUE, 0),
(2, '14:30:00', '15:00:00', 'AVAILABLE', @TUE, 0);

-- : Wednesday of the current week
SET @WED = DATEADD('DAY', (3 - DAY_OF_WEEK(CURRENT_DATE)), CURRENT_DATE);
INSERT INTO slot (schedule_id, slot_start_time, slot_end_time, status, date, version) VALUES 
(3, '12:00:00', '12:30:00', 'AVAILABLE', @WED, 0),
(3, '13:00:00', '13:30:00', 'AVAILABLE', @WED, 0),
(3, '17:30:00', '18:00:00', 'AVAILABLE', @WED, 0);

-- Steve: Thursday of the current week
SET @THU = DATEADD('DAY', (4 - DAY_OF_WEEK(CURRENT_DATE)), CURRENT_DATE);
INSERT INTO slot (schedule_id, slot_start_time, slot_end_time, status, date, version) VALUES 
(4, '10:00:00', '10:30:00', 'AVAILABLE', @THU, 0),
(4, '11:00:00', '11:30:00', 'AVAILABLE', @THU, 0),
(4, '15:30:00', '16:00:00', 'AVAILABLE', @THU, 0);

-- Chepe: Friday of the current week
SET @FRI = DATEADD('DAY', (5 - DAY_OF_WEEK(CURRENT_DATE)), CURRENT_DATE);
INSERT INTO slot (schedule_id, slot_start_time, slot_end_time, status, date, version) VALUES 
(5, '10:00:00', '10:30:00', 'AVAILABLE', @FRI, 0),
(5, '11:00:00', '11:30:00', 'AVAILABLE', @FRI, 0),
(5, '15:30:00', '16:00:00', 'AVAILABLE', @FRI, 0);

-- ---- Dynamic Demo Appointment ----
-- Book Casi's first slot on Monday
INSERT INTO appointment (client_id, service_id, schedule_id, slot_start_time, date, status, current_price)
    VALUES (4, 1, 1, '09:00:00', @MON, 'CONFIRMED', 40.00);

UPDATE slot SET status = 'BOOKED' WHERE schedule_id = 1 AND slot_start_time = '09:00:00' AND date = @MON;