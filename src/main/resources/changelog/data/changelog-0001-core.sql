-- CREATE TABLE public.activities (
--     id bigint NOT NULL,
--     created_at timestamp(6) without time zone NOT NULL,
--     deleted_at timestamp(6) without time zone,
--     updated_at timestamp(6) without time zone,
--     activity_description character varying(255),
--     activity_name character varying(255),
--     calories_burned double precision
-- );
insert into activities (id, created_at, deleted_at, updated_at, activity_description, activity_name, calories_burned) values
(1, '2023-10-01 00:00:00', null, '2023-10-01 00:00:00', 'Running', 'Morning Run', 300.0),
(2, '2023-10-02 00:00:00', null, '2023-10-02 00:00:00', 'Cycling', 'Evening Ride', 250.0),
(3, '2023-10-03 00:00:00', null, '2023-10-03 00:00:00', 'Swimming', 'Pool Session', 400.0);
