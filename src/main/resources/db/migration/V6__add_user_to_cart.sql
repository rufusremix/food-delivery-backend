ALTER TABLE carts
    ADD COLUMN user_id BIGINT NOT NULL;

ALTER TABLE carts
    ADD CONSTRAINT carts_users_id_fk FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE carts
    ADD CONSTRAINT carts_user_id_unique UNIQUE (user_id);

ALTER TABLE products
    MODIFY COLUMN restaurant_id BIGINT NOT NULL;