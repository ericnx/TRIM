DELETE FROM barber_services_mapping;
DELETE FROM appointment;
DELETE FROM slot;
DELETE FROM schedule;
DELETE FROM services;
DELETE FROM client;
DELETE FROM barber;
DELETE FROM person;

-- ---- People, Barbers, Clients ----
INSERT INTO person (first_name, last_name, phone, email) VALUES
    ('Andy',   'Nguyen',    '408-555-0001', 'andy@trim.com'),   
    ('Casi',   'Casillas',  '408-555-0002', 'casi@trim.com'),   
    ('Chamoy', 'Rivers',    '408-555-0003', 'chamoy@trim.com'), 
    ('Chepe',  'Lowks',     '408-555-0004', 'chepe@trim.com'),  
    ('Diamonte','Fernandez', '408-555-0005', 'diamonte@trim.com'),
    ('Steve',  'Bernal',    '408-555-0006', 'steve@trim.com'),  
    ('Alice',  'Johnson',   '408-555-1001', 'alice@email.com'), 
    ('Bob',    'Smith',     '408-555-1002', 'bob@email.com'),   
    ('Carol',  'Williams',  '408-555-1003', 'carol@email.com');

INSERT INTO barber (barber_id, license_no, role) VALUES
    (1, 'LIC-001', 'admin'), (2, 'LIC-002', 'staff'), (3, 'LIC-003', 'staff'),
    (4, 'LIC-004', 'staff'), (5, 'LIC-005', 'staff'), (6, 'LIC-006', 'staff');

INSERT INTO client (client_id) VALUES (7), (8), (9);

INSERT INTO services (type, price, duration) VALUES
    ('Regular Haircut', 40.00, 30),                       -- ID: 1
    ('Regular Haircut + Beard', 45.00, 35),               -- ID: 2
    ('Regular Haircut + Eyebrows', 45.00, 35),            -- ID: 3
    ('Regular Haircut + Beard + Eyebrows', 50.00, 40),    -- ID: 4
    ('Long Hair Scissor Haircut', 50.00, 45),             -- ID: 5
    ('Long Hair + Beard', 55.00, 50),                     -- ID: 6
    ('Long Hair + Eyebrows', 55.00, 50),                  -- ID: 7
    ('Long Hair + Beard + Eyebrows', 60.00, 55);          -- ID: 8

INSERT INTO barber_services_mapping (barber_id, service_id) VALUES
    -- Andy (Admin)
    (1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7), (1, 8),
    
    -- Casi
    (2, 1), (2, 2), (2, 3), (2, 4),

    -- Chamoy
    (3, 1), (3, 2), (3, 3), (3, 4),

    -- Chepe
    (4, 1), (4, 2), (4, 3), (4, 4), (4, 5), (4, 6), (4, 7), (4, 8),

    -- Diamonte
    (5, 1), (5, 2), (5, 3), (5, 4),

    -- Steve
    (6, 1), (6, 2), (6, 3), (6, 4), (6, 5), (6, 6), (6, 7), (6, 8);

-- ---- 1. REALISTIC SCHEDULES ----
INSERT INTO schedule (admin_id, staff_id, day_of_week, shift_start_time, shift_end_time) VALUES 
    -- Andy
    (1, 1, 'MON', '09:00:00', '17:00:00'), 
    (1, 1, 'TUE', '09:00:00', '17:00:00'),
    (1, 1, 'WED', '09:00:00', '17:00:00'),
    (1, 1, 'FRI', '09:00:00', '17:00:00'),
    (1, 1, 'SAT', '09:00:00', '15:00:00'),
    (1, 1, 'SUN', '10:00:00', '16:00:00'),

    -- Casi 
    (1, 2, 'MON', '09:00:00', '17:00:00'), 
    (1, 2, 'TUE', '09:00:00', '17:00:00'),
    (1, 2, 'WED', '09:00:00', '17:00:00'),
    (1, 2, 'THU', '09:00:00', '17:00:00'),

    -- Chamoy
    (1, 3, 'WED', '10:00:00', '19:00:00'), 
    (1, 3, 'THU', '10:00:00', '19:00:00'),
    (1, 3, 'FRI', '10:00:00', '19:00:00'),
    (1, 3, 'SAT', '09:00:00', '18:00:00'),

    -- Chepe 
    (1, 4, 'TUE', '09:00:00', '18:00:00'),
    (1, 4, 'THU', '09:00:00', '18:00:00'),
    (1, 4, 'FRI', '09:00:00', '18:00:00'), 
    (1, 4, 'SAT', '09:00:00', '18:00:00'),
    (1, 4, 'SUN', '10:00:00', '16:00:00'),

    -- Diamonte
    (1, 5, 'TUE', '11:00:00', '20:00:00'), 
    (1, 5, 'WED', '11:00:00', '20:00:00'),
    (1, 5, 'THU', '11:00:00', '20:00:00'),
    (1, 5, 'FRI', '11:00:00', '20:00:00'),

    -- Steve
    (1, 6, 'MON', '09:00:00', '15:00:00'),
    (1, 6, 'WED', '09:00:00', '15:00:00'), 
    (1, 6, 'FRI', '09:00:00', '15:00:00'),
    (1, 6, 'SAT', '09:00:00', '15:00:00'),
    (1, 6, 'SUN', '10:00:00', '14:00:00');

-- ---- 2. GENERATE SLOTS ----
INSERT INTO slot (schedule_id, slot_start_time, slot_end_time, status, date)
SELECT 
    s.schedule_id,
    t.start_time::time,
    (t.start_time + interval '30 minutes')::time,
    'AVAILABLE',
    d.day::date
FROM schedule s
CROSS JOIN generate_series('2026-05-01'::timestamp, '2026-05-31'::timestamp, '1 day'::interval) d(day)
CROSS JOIN generate_series('2026-05-01 09:00:00'::timestamp, '2026-05-01 20:00:00'::timestamp, '30 minutes'::interval) t(start_time)
WHERE 
    (
        (s.day_of_week = 'SUN' AND extract(DOW from d.day) = 0) OR
        (s.day_of_week = 'MON' AND extract(DOW from d.day) = 1) OR
        (s.day_of_week = 'TUE' AND extract(DOW from d.day) = 2) OR
        (s.day_of_week = 'WED' AND extract(DOW from d.day) = 3) OR
        (s.day_of_week = 'THU' AND extract(DOW from d.day) = 4) OR
        (s.day_of_week = 'FRI' AND extract(DOW from d.day) = 5) OR
        (s.day_of_week = 'SAT' AND extract(DOW from d.day) = 6)
    )
AND t.start_time::time >= s.shift_start_time 
AND t.start_time::time < s.shift_end_time
AND d.day::date >= CURRENT_DATE;

INSERT INTO appointment (client_id, service_id, schedule_id, slot_start_time, date, status, current_price)
SELECT 7, 1, schedule_id, slot_start_time, date, 'CONFIRMED', 40.00
FROM slot 
WHERE date = CURRENT_DATE AND slot_start_time = '10:00:00' LIMIT 1;

UPDATE slot SET status = 'BOOKED' 
WHERE date = CURRENT_DATE AND slot_start_time = '10:00:00';