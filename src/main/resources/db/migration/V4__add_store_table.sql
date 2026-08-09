CREATE TABLE store (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    active BOOLEAN,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    owner_id UUID NOT NULL UNIQUE,

    CONSTRAINT fk_store_user
        FOREIGN KEY (owner_id) REFERENCES users(id)
);

CREATE TABLE store_tags (
    store_id uuid NOT NULL,
    tag varchar(255) NOT NULL,

    CONSTRAINT fk_store_tags_store
        FOREIGN KEY (store_id) REFERENCES store(id)
);

ALTER TABLE product 
ADD COLUMN store_id uuid;

ALTER TABLE product 
ADD CONSTRAINT fk_product_store 
FOREIGN KEY (store_id) REFERENCES store(id);

CREATE INDEX idx_stores_owner ON store(owner_id);
CREATE INDEX idx_store_tags_store ON store_tags(store_id);
CREATE INDEX idx_product_store ON product(store_id);