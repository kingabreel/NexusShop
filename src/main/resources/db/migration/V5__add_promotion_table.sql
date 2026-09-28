CREATE TABLE promotion (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    percentage DOUBLE PRECISION NOT NULL,
    store_id UUID NOT NULL,
    FOREIGN KEY (store_id) REFERENCES store(id)
);

CREATE TABLE product_promotion (
    promotion_id UUID NOT NULL,
    product_id UUID NOT NULL,

    PRIMARY KEY (promotion_id, product_id),

    FOREIGN KEY (promotion_id)
        REFERENCES promotion(id)
        ON DELETE CASCADE,

    FOREIGN KEY (product_id)
        REFERENCES product(id)
        ON DELETE CASCADE
);