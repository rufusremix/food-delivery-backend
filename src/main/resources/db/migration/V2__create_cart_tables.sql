CREATE TABLE carts
(
  id binary(16) default(uuid_to_bin(uuid())) NOT NULL PRIMARY KEY,
  date_created date default(curdate()) NOT NULL
);

CREATE TABLE cart_items
(
  id BIGINT auto_increment PRIMARY KEY,
  cart_id binary(16) NOT NULL,
  product_id BIGINT NOT NULL,
  quantity INT default 1 NOT NULL,
  CONSTRAINT cart_items_cart_product_unique UNIQUE (cart_id, product_id),
  CONSTRAINT cart_items_carts_id_fk FOREIGN KEY (cart_id) REFERENCES carts (id) ON DELETE CASCADE,
  CONSTRAINT cart_items_products_id_fk FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE
);