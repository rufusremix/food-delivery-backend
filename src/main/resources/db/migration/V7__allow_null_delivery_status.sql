ALTER TABLE orders
    ALTER COLUMN delivery_status DROP DEFAULT;

ALTER TABLE orders
    MODIFY COLUMN delivery_status VARCHAR(20) NULL;
