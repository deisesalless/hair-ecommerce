CREATE TABLE stock (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_variation_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    minimum_quantity INT DEFAULT 0,
    updated_at TIMESTAMP NOT NULL,
    expiration_date DATE,

    CONSTRAINT fk_stock_variation FOREIGN KEY (product_variation_id) REFERENCES product_variation(id)
);