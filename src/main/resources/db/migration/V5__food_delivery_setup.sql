-- =========================================================================
-- Food delivery setup (combines former V5-V8)
--   1. restaurants table
--   2. link products -> restaurants
--   3. product food fields (image, availability, veg)
--   4. order delivery status + delivery address
-- =========================================================================

-- 1. Restaurants ----------------------------------------------------------
CREATE TABLE restaurants
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    name         VARCHAR(255)   NOT NULL,
    description  VARCHAR(500) NULL,
    cuisine      VARCHAR(200) NULL,
    address      VARCHAR(255) NULL,
    image_url    VARCHAR(500) NULL,
    is_open      BOOLEAN        NOT NULL DEFAULT TRUE,
    delivery_fee DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

-- 2. Link products -> restaurants -----------------------------------------
ALTER TABLE products
    ADD COLUMN restaurant_id BIGINT NULL;

ALTER TABLE products
    ADD CONSTRAINT fk_product_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurants (id) ON DELETE NO ACTION;

CREATE INDEX fk_product_restaurant ON products (restaurant_id);

-- 3. Product food fields --------------------------------------------------
ALTER TABLE products
    ADD COLUMN image_url VARCHAR(500) NULL;

ALTER TABLE products
    ADD COLUMN is_available BOOLEAN NOT NULL DEFAULT TRUE;

ALTER TABLE products
    ADD COLUMN is_veg BOOLEAN NULL;

-- 4. Order delivery status + delivery address -----------------------------
ALTER TABLE orders
    ADD COLUMN delivery_status VARCHAR(20) NOT NULL DEFAULT 'CONFIRMED';

ALTER TABLE orders
    ADD COLUMN delivery_address_id BIGINT NULL;

ALTER TABLE orders
    ADD CONSTRAINT fk_order_delivery_address FOREIGN KEY (delivery_address_id) REFERENCES addresses (id) ON DELETE NO ACTION;

CREATE INDEX fk_order_delivery_address ON orders (delivery_address_id);
