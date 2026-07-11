create table store (
    id serial primary key,
    name varchar(255) not null,
    email varchar(255) not null unique,
    phone varchar(20) not null,
    active boolean,
    category varchar(255) NOT NULL,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp,
    
    CONSTRAINT fk_store_user
        FOREIGN KEY (owner) REFERENCES users(id)
);

CREATE TABLE store_products (
    store_id serial NOT NULL,
    product_id uuid NOT NULL,

    CONSTRAINT fk_store_products_store
        FOREIGN KEY (store_id) REFERENCES store(id),
    CONSTRAINT fk_store_products_product
        FOREIGN KEY (product_id) REFERENCES product(id)
);

alter table product remove column createdBy;
