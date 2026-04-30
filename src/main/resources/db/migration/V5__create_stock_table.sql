CREATE TABLE stock (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    product_variation_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    minimum_quantity INT DEFAULT 0,
    updated_at TIMESTAMP NOT NULL,
    expiration_date DATE,

    CONSTRAINT fk_stock_variation FOREIGN KEY (product_variation_id) REFERENCES product_variation(id)
);