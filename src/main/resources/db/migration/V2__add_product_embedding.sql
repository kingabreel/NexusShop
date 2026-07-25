CREATE TABLE users (
    id uuid PRIMARY KEY,
    created_at timestamp(6),
    updated_at timestamp(6),
    active boolean,

    username varchar(255) NOT NULL UNIQUE,
    email varchar(255) NOT NULL UNIQUE,
    password varchar(255),

    provider varchar(255)
);

CREATE TABLE user_roles (
    user_id uuid NOT NULL,
    roles varchar(255) NOT NULL,

    CONSTRAINT fk_user_roles_user
        FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE product (
    id uuid PRIMARY KEY,
    created_at timestamp(6),
    updated_at timestamp(6),
    active boolean,

    name varchar(255) NOT NULL,
    description varchar(255),

    price numeric(38,2) NOT NULL,
    stock integer NOT NULL,

    category varchar(255) NOT NULL,
    highlight boolean NOT NULL DEFAULT false,

    created_by uuid,

    embedding vector(1536),

    CONSTRAINT fk_product_user
        FOREIGN KEY (created_by) REFERENCES users(id)
);

CREATE TABLE product_tags (
    product_id uuid NOT NULL,
    tags varchar(255) NOT NULL,

    CONSTRAINT fk_product_tags_product
        FOREIGN KEY (product_id) REFERENCES product(id)
);

CREATE TABLE coupon (
    id uuid PRIMARY KEY,
    created_at timestamp(6),
    updated_at timestamp(6),
    active boolean,

    code varchar(255) NOT NULL UNIQUE,
    value numeric(38,2) NOT NULL,
    type varchar(255) NOT NULL,
    expiration_date timestamp(6) NOT NULL
);

CREATE TABLE cart (
    id uuid PRIMARY KEY,
    created_at timestamp(6),
    updated_at timestamp(6),
    active boolean,

    user_id uuid NOT NULL,
    status varchar(255) NOT NULL,

    subtotal numeric(38,2),
    discount numeric(38,2),
    total numeric(38,2),

    coupon_id uuid,

    CONSTRAINT fk_cart_user
        FOREIGN KEY (user_id) REFERENCES users(id),

    CONSTRAINT fk_cart_coupon
        FOREIGN KEY (coupon_id) REFERENCES coupon(id)
);

CREATE TABLE cart_item (
    id uuid PRIMARY KEY,
    created_at timestamp(6),
    updated_at timestamp(6),
    active boolean,

    cart_id uuid NOT NULL,
    product_id uuid NOT NULL,

    quantity integer NOT NULL,
    price_at_time numeric(38,2) NOT NULL,
    total numeric(38,2) NOT NULL,

    CONSTRAINT fk_cart_item_cart
        FOREIGN KEY (cart_id) REFERENCES cart(id),

    CONSTRAINT fk_cart_item_product
        FOREIGN KEY (product_id) REFERENCES product(id)
);

CREATE TABLE notification (
    id uuid PRIMARY KEY,
    created_at timestamp(6),
    updated_at timestamp(6),
    active boolean,

    message varchar(255),
    user_id uuid NOT NULL,
    status varchar(255) NOT NULL,
    redirect_link varchar(255),

    CONSTRAINT fk_notification_user
        FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE ratings (
    id uuid PRIMARY KEY,
    created_at timestamp(6),
    updated_at timestamp(6),
    active boolean,

    rating double precision NOT NULL,
    comment varchar(255),
    anonymous boolean NOT NULL DEFAULT false,

    user_id uuid,
    product_id uuid,

    CONSTRAINT fk_rating_user
        FOREIGN KEY (user_id) REFERENCES users(id),

    CONSTRAINT fk_rating_product
        FOREIGN KEY (product_id) REFERENCES product(id)
);

CREATE TABLE product_analytic (
    id uuid PRIMARY KEY,
    created_at timestamp(6),
    updated_at timestamp(6),
    active boolean,

    product_id uuid NOT NULL UNIQUE,
    view_count bigint DEFAULT 0,

    CONSTRAINT fk_product_analytic_product
        FOREIGN KEY (product_id) REFERENCES product(id)
);

CREATE TABLE user_history (
    id uuid PRIMARY KEY,
    created_at timestamp(6),
    updated_at timestamp(6),
    active boolean,

    user_id uuid NOT NULL,
    product_id uuid NOT NULL,

    viewed_at timestamp(6),

    CONSTRAINT fk_user_history_user
        FOREIGN KEY (user_id) REFERENCES users(id),

    CONSTRAINT fk_user_history_product
        FOREIGN KEY (product_id) REFERENCES product(id)
);

CREATE INDEX product_embedding_idx
ON product
USING hnsw (embedding vector_cosine_ops);

CREATE INDEX idx_product_category ON product(category);
CREATE INDEX idx_product_highlight ON product(highlight);

CREATE INDEX idx_cart_user ON cart(user_id);
CREATE INDEX idx_cart_item_cart ON cart_item(cart_id);
CREATE INDEX idx_cart_item_product ON cart_item(product_id);

CREATE INDEX idx_rating_product ON ratings(product_id);
CREATE INDEX idx_rating_user ON ratings(user_id);

CREATE INDEX idx_notification_user ON notification(user_id);
CREATE INDEX idx_user_history_user ON user_history(user_id);
CREATE INDEX idx_user_history_product ON user_history(product_id);
