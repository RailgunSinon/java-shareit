delete from bookings;
delete from items;
delete from users;
delete from comments;

ALTER TABLE comments ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE bookings ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE users ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE items ALTER COLUMN ID RESTART WITH 1;