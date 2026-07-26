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
    store_id UUID NOT NULL,
    tag VARCHAR(255) NOT NULL,

    CONSTRAINT fk_store_tags_store
        FOREIGN KEY (store_id) REFERENCES store(id)
        ON DELETE CASCADE
);

CREATE TABLE store_products (
    store_id uuid NOT NULL,
    product_id uuid NOT NULL,

    CONSTRAINT fk_store_products_store
        FOREIGN KEY (store_id) REFERENCES store(id),
    CONSTRAINT fk_store_products_product
        FOREIGN KEY (product_id) REFERENCES product(id)
);

alter table product drop column created_by;

alter table product add column store_id uuid;

alter table product add constraint fk_product_store foreign key (store_id) references store(id);