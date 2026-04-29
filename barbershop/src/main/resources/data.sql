-- ---- People ----
INSERT INTO person (first_name, last_name, phone, email) VALUES
    ('Andy',  'Nguyen',   '408-555-0001', 'andy@cuts.com'),   -- person_id = 1
    ('Chris', 'Martinez', '408-555-0002', 'chris@cuts.com'),  -- person_id = 2
    ('Maya',  'Chen',     '408-555-0003', 'maya@cuts.com'),   -- person_id = 3
    ('Alice', 'Johnson',  '408-555-1001', 'alice@email.com'), -- person_id = 4
    ('Bob',   'Smith',    '408-555-1002', 'bob@email.com'),   -- person_id = 5
    ('Carol', 'Williams', '408-555-1003', 'carol@email.com'); -- person_id = 6

-- ---- Barbers ----
INSERT INTO barber (barber_id, license_no, role) VALUES
    (1, 'LIC-001', 'admin'),  -- Andy  (admin)
    (2, 'LIC-002', 'staff'),  -- Chris (staff)
    (3, 'LIC-003', 'staff');  -- Maya  (staff)

-- ---- Clients ----
INSERT INTO client (client_id) VALUES (4), (5), (6);

-- ---- Services ----
INSERT INTO barber_service (type, price, duration) VALUES
    ('Regular Haircut',                    25.00, 30),  -- service_id = 1
    ('Regular Haircut + Beard',            35.00, 45),  -- service_id = 2
    ('Regular Haircut + Eyebrows',         30.00, 40),  -- service_id = 3
    ('Regular Haircut + Beard + Eyebrows', 45.00, 60),  -- service_id = 4
    ('Long Hair Scissor Cut',              40.00, 60),  -- service_id = 5
    ('Dye Hair',                           75.00, 90);  -- service_id = 6

-- ---- Schedules ----
-- Andy (admin) schedules Chris on Monday 9am-3pm
INSERT INTO schedule (admin_id, staff_id, day_of_week, shift_start_time, shift_end_time)
    VALUES (1, 2, 'MONDAY', '09:00:00', '15:00:00');  -- schedule_id = 1

-- Andy (admin) schedules Maya on Wednesday 12pm-6pm
INSERT INTO schedule (admin_id, staff_id, day_of_week, shift_start_time, shift_end_time)
    VALUES (1, 3, 'WEDNESDAY', '12:00:00', '18:00:00');  -- schedule_id = 2

-- ---- Slots (30-min blocks) ----
-- Chris Monday 2026-03-16
INSERT INTO slot VALUES (1, '09:00:00', '09:30:00', 'AVAILABLE', '2026-03-16');
INSERT INTO slot VALUES (1, '09:30:00', '10:00:00', 'AVAILABLE', '2026-03-16');
INSERT INTO slot VALUES (1, '10:00:00', '10:30:00', 'AVAILABLE', '2026-03-16');
INSERT INTO slot VALUES (1, '10:30:00', '11:00:00', 'AVAILABLE', '2026-03-16');
INSERT INTO slot VALUES (1, '11:00:00', '11:30:00', 'AVAILABLE', '2026-03-16');
INSERT INTO slot VALUES (1, '11:30:00', '12:00:00', 'AVAILABLE', '2026-03-16');
INSERT INTO slot VALUES (1, '12:00:00', '12:30:00', 'AVAILABLE', '2026-03-16');
INSERT INTO slot VALUES (1, '12:30:00', '13:00:00', 'AVAILABLE', '2026-03-16');
INSERT INTO slot VALUES (1, '13:00:00', '13:30:00', 'AVAILABLE', '2026-03-16');
INSERT INTO slot VALUES (1, '13:30:00', '14:00:00', 'AVAILABLE', '2026-03-16');
INSERT INTO slot VALUES (1, '14:00:00', '14:30:00', 'AVAILABLE', '2026-03-16');
INSERT INTO slot VALUES (1, '14:30:00', '15:00:00', 'AVAILABLE', '2026-03-16');

-- Maya Wednesday 2026-03-18
INSERT INTO slot VALUES (2, '12:00:00', '12:30:00', 'AVAILABLE', '2026-03-18');
INSERT INTO slot VALUES (2, '12:30:00', '13:00:00', 'AVAILABLE', '2026-03-18');
INSERT INTO slot VALUES (2, '13:00:00', '13:30:00', 'AVAILABLE', '2026-03-18');
INSERT INTO slot VALUES (2, '13:30:00', '14:00:00', 'AVAILABLE', '2026-03-18');
INSERT INTO slot VALUES (2, '14:00:00', '14:30:00', 'AVAILABLE', '2026-03-18');
INSERT INTO slot VALUES (2, '14:30:00', '15:00:00', 'AVAILABLE', '2026-03-18');
INSERT INTO slot VALUES (2, '15:00:00', '15:30:00', 'AVAILABLE', '2026-03-18');
INSERT INTO slot VALUES (2, '15:30:00', '16:00:00', 'AVAILABLE', '2026-03-18');
INSERT INTO slot VALUES (2, '16:00:00', '16:30:00', 'AVAILABLE', '2026-03-18');
INSERT INTO slot VALUES (2, '16:30:00', '17:00:00', 'AVAILABLE', '2026-03-18');
INSERT INTO slot VALUES (2, '17:00:00', '17:30:00', 'AVAILABLE', '2026-03-18');
INSERT INTO slot VALUES (2, '17:30:00', '18:00:00', 'AVAILABLE', '2026-03-18');

-- ---- Demo appointment ----
-- Alice books a Regular Haircut with Chris on Monday at 9am
INSERT INTO appointment (client_id, service_id, schedule_id, slot_start_time, status, current_price)
    VALUES (4, 1, 1, '09:00:00', 'CONFIRMED', 25.00);
UPDATE slot SET status = 'BOOKED' WHERE schedule_id = 1 AND slot_start_time = '09:00:00';
