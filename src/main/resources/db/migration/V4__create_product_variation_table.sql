CREATE TABLE product_variation (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    product_id BIGINT NOT NULL,
    size VARCHAR(50) NOT NULL,
    sale_price DECIMAL(10,2) NOT NULL,
    cost_price DECIMAL(10,2),
    barcode VARCHAR(100) UNIQUE,
    active BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT fk_variation_product FOREIGN KEY (product_id) REFERENCES product(id)
);