DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS items CASCADE;
DROP TABLE IF EXISTS bookings CASCADE;
DROP TABLE IF EXISTS comments CASCADE;
DROP TABLE IF EXISTS requests CASCADE;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(512) NOT NULL,
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS requests (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
  description VARCHAR(4000) NOT NULL,
  requester_id BIGINT REFERENCES users (id) ON DELETE CASCADE,
  created TIMESTAMP WITHOUT TIME ZONE DEFAULT now()
);

CREATE INDEX IF NOT EXISTS fki_fk_requests_requester_id ON requests (requester_id ASC);

CREATE TABLE IF NOT EXISTS items (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(1000) NOT NULL,
    available Boolean NOT NULL,
    user_id BIGINT REFERENCES users (id) ON DELETE CASCADE NOT NULL,
    request_id BIGINT REFERENCES requests (id) ON DELETE CASCADE
);

DROP INDEX IF EXISTS fki_fk_items_user_id;
DROP INDEX IF EXISTS fki_fk_items_request_id;
CREATE INDEX IF NOT EXISTS fki_fk_items_user_id ON items (user_id ASC NULLS LAST);
CREATE INDEX IF NOT EXISTS fki_fk_items_request_id ON items (request_id ASC NULLS LAST);

CREATE TABLE IF NOT EXISTS bookings (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    item_id BIGINT REFERENCES items (id) ON DELETE CASCADE,
    booker_id BIGINT REFERENCES users (id) ON DELETE CASCADE,
    status VARCHAR,
    start_booking TIMESTAMP WITHOUT TIME ZONE,
    end_booking TIMESTAMP WITHOUT TIME ZONE
);

DROP INDEX IF EXISTS fki_fk_bookings_item_id;
DROP INDEX IF EXISTS fki_fk_bookings_booker_id;
CREATE INDEX IF NOT EXISTS fki_fk_bookings_item_id ON bookings (item_id ASC NULLS LAST);
CREATE INDEX IF NOT EXISTS fki_fk_bookings_booker_id ON bookings (booker_id ASC NULLS LAST);

CREATE TABLE IF NOT EXISTS comments (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    text VARCHAR(4000) NOT NULL,
    item_id BIGINT REFERENCES items (id) ON DELETE CASCADE,
    author_id BIGINT REFERENCES users (id) ON DELETE CASCADE,
    created TIMESTAMP WITHOUT TIME ZONE DEFAULT now()
);

DROP INDEX IF EXISTS fki_fk_comments_item_id;
DROP INDEX IF EXISTS fki_fk_comments_author_id;
CREATE INDEX IF NOT EXISTS fki_fk_comments_item_id ON comments (item_id ASC NULLS LAST);
CREATE INDEX IF NOT EXISTS fki_fk_comments_author_id ON comments (author_id ASC NULLS LAST);
