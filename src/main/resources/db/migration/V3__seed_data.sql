INSERT INTO users (
    id,
    created_at,
    updated_at,
    active,
    username,
    email,
    password,
    provider
)
VALUES (
    gen_random_uuid(),
    now(),
    now(),
    true,
    'admin',
    'admin@nexus.com',
    '$2b$10$YxW6od6e9jLsWknm6pvX5OpF1wIL/fNOlLg2DfNfQ7Ny.Ytt3Ps2q',
    NULL
);

INSERT INTO product (
    id,
    created_at,
    updated_at,
    active,
    name,
    description,
    price,
    stock,
    category,
    highlight,
    created_by,
    embedding
)
SELECT
    gen_random_uuid(),
    now(),
    now(),
    true,
    v.name,
    v.description,
    v.price,
    v.stock,
    v.category,
    v.highlight,
    u.id,
    NULL
FROM users u
CROSS JOIN (
    VALUES
        ('Smartphone X','High-end smartphone',2999.99,50,'ELETRONICOS',true),
        ('Laptop Pro','Powerful laptop',5999.90,20,'ELETRONICOS',true),
        ('Wireless Headphones','Noise-cancelling over-ear headphones',899.99,35,'ELETRONICOS',true),
        ('Office Chair','Ergonomic chair for office use',649.50,15,'MOVEIS',true),
        ('Gaming Mouse','High precision RGB gaming mouse',199.90,80,'ELETRONICOS',true),
        ('Mechanical Keyboard','RGB mechanical keyboard with blue switches',349.90,60,'ELETRONICOS',true),
        ('Running Shoes','Comfortable running shoes',249.99,40,'ROUPAS',true),
        ('Backpack','Durable backpack for travel and work',159.90,70,'ACESSORIOS',true),
        ('Coffee Maker','Automatic drip coffee maker',299.00,25,'ELETRODOMESTICOS',false),
        ('Desk Lamp','LED desk lamp with adjustable brightness',89.90,55,'MOVEIS',false)
) AS v(name, description, price, stock, category, highlight)
WHERE u.email = 'admin@nexus.com';

INSERT INTO product (id, created_at, updated_at, active, name, description, price, stock, category, highlight, created_by, embedding)
VALUES (gen_random_uuid(), now(), now(), true, 'Tablet Plus', '10-inch tablet with high resolution screen', 1899.99, 30, 'ELETRONICOS', false, (SELECT id FROM users WHERE email = 'admin@nexus.com'), NULL);

INSERT INTO product (id, created_at, updated_at, active, name, description, price, stock, category, highlight, created_by, embedding)
VALUES (gen_random_uuid(), now(), now(), true, 'Smartwatch Fit', 'Fitness smartwatch with heart rate monitor', 799.90, 45, 'ELETRONICOS', false, (SELECT id FROM users WHERE email = 'admin@nexus.com'), NULL);

INSERT INTO product (id, created_at, updated_at, active, name, description, price, stock, category, highlight, created_by, embedding)
VALUES (gen_random_uuid(), now(), now(), true, 'Bluetooth Speaker', 'Portable speaker with deep bass', 299.99, 65, 'ELETRONICOS', false, (SELECT id FROM users WHERE email = 'admin@nexus.com'), NULL);

INSERT INTO product (id, created_at, updated_at, active, name, description, price, stock, category, highlight, created_by, embedding)
VALUES (gen_random_uuid(), now(), now(), true, '4K Monitor', '27-inch 4K UHD monitor', 1799.00, 18, 'ELETRONICOS', false, (SELECT id FROM users WHERE email = 'admin@nexus.com'), NULL);

INSERT INTO product (id, created_at, updated_at, active, name, description, price, stock, category, highlight, created_by, embedding)
VALUES (gen_random_uuid(), now(), now(), true, 'USB-C Hub', 'Multiport adapter with HDMI and USB 3.0', 149.90, 90, 'ACESSORIOS', false, (SELECT id FROM users WHERE email = 'admin@nexus.com'), NULL);

INSERT INTO product (id, created_at, updated_at, active, name, description, price, stock, category, highlight, created_by, embedding)
VALUES (gen_random_uuid(), now(), now(), true, 'External SSD 1TB', 'High-speed portable SSD storage', 699.90, 25, 'ELETRONICOS', false, (SELECT id FROM users WHERE email = 'admin@nexus.com'), NULL);

INSERT INTO product (id, created_at, updated_at, active, name, description, price, stock, category, highlight, created_by, embedding)
VALUES (gen_random_uuid(), now(), now(), true, 'Office Desk', 'Spacious wooden office desk', 899.00, 12, 'MOVEIS', false, (SELECT id FROM users WHERE email = 'admin@nexus.com'), NULL);

INSERT INTO product (id, created_at, updated_at, active, name, description, price, stock, category, highlight, created_by, embedding)
VALUES (gen_random_uuid(), now(), now(), true, 'Bookshelf', '5-tier wooden bookshelf', 499.90, 20, 'MOVEIS', false, (SELECT id FROM users WHERE email = 'admin@nexus.com'), NULL);

INSERT INTO product (id, created_at, updated_at, active, name, description, price, stock, category, highlight, created_by, embedding)
VALUES (gen_random_uuid(), now(), now(), true, 'Floor Lamp', 'Modern floor lamp with warm lighting', 229.90, 28, 'MOVEIS', false, (SELECT id FROM users WHERE email = 'admin@nexus.com'), NULL);

INSERT INTO product (id, created_at, updated_at, active, name, description, price, stock, category, highlight, created_by, embedding)
VALUES (gen_random_uuid(), now(), now(), true, 'Air Fryer', 'Oil-free air fryer with digital panel', 399.99, 33, 'ELETRODOMESTICOS', false, (SELECT id FROM users WHERE email = 'admin@nexus.com'), NULL);

INSERT INTO product (id, created_at, updated_at, active, name, description, price, stock, category, highlight, created_by, embedding)
VALUES (gen_random_uuid(), now(), now(), true, 'Blender Pro', 'High-power blender with glass jar', 249.90, 40, 'ELETRODOMESTICOS', false, (SELECT id FROM users WHERE email = 'admin@nexus.com'), NULL);

INSERT INTO product (id, created_at, updated_at, active, name, description, price, stock, category, highlight, created_by, embedding)
VALUES (gen_random_uuid(), now(), now(), true, 'Vacuum Cleaner', 'Bagless vacuum cleaner with strong suction', 599.00, 22, 'ELETRODOMESTICOS', false, (SELECT id FROM users WHERE email = 'admin@nexus.com'), NULL);

INSERT INTO product (id, created_at, updated_at, active, name, description, price, stock, category, highlight, created_by, embedding)
VALUES (gen_random_uuid(), now(), now(), true, 'T-shirt Basic', 'Cotton basic t-shirt', 49.90, 100, 'ROUPAS', false, (SELECT id FROM users WHERE email = 'admin@nexus.com'), NULL);

INSERT INTO product (id, created_at, updated_at, active, name, description, price, stock, category, highlight, created_by, embedding)
VALUES (gen_random_uuid(), now(), now(), true, 'Jeans Slim Fit', 'Comfortable slim fit jeans', 129.90, 55, 'ROUPAS', false, (SELECT id FROM users WHERE email = 'admin@nexus.com'), NULL);

INSERT INTO product (id, created_at, updated_at, active, name, description, price, stock, category, highlight, created_by, embedding)
VALUES (gen_random_uuid(), now(), now(), true, 'Jacket Winter', 'Warm winter jacket', 299.90, 35, 'ROUPAS', false, (SELECT id FROM users WHERE email = 'admin@nexus.com'), NULL);

INSERT INTO product (id, created_at, updated_at, active, name, description, price, stock, category, highlight, created_by, embedding)
VALUES (gen_random_uuid(), now(), now(), true, 'Sneakers Casual', 'Stylish casual sneakers', 199.90, 60, 'ROUPAS', false, (SELECT id FROM users WHERE email = 'admin@nexus.com'), NULL);

INSERT INTO product (id, created_at, updated_at, active, name, description, price, stock, category, highlight, created_by, embedding)
VALUES (gen_random_uuid(), now(), now(), true, 'Sunglasses', 'UV protection sunglasses', 89.90, 75, 'ACESSORIOS', false, (SELECT id FROM users WHERE email = 'admin@nexus.com'), NULL);

INSERT INTO product (id, created_at, updated_at, active, name, description, price, stock, category, highlight, created_by, embedding)
VALUES (gen_random_uuid(), now(), now(), true, 'Wallet Leather', 'Genuine leather wallet', 119.90, 50, 'ACESSORIOS', false, (SELECT id FROM users WHERE email = 'admin@nexus.com'), NULL);

INSERT INTO product (id, created_at, updated_at, active, name, description, price, stock, category, highlight, created_by, embedding)
VALUES (gen_random_uuid(), now(), now(), true, 'Travel Suitcase', 'Medium size durable suitcase', 349.90, 27, 'ACESSORIOS', false, (SELECT id FROM users WHERE email = 'admin@nexus.com'), NULL);

INSERT INTO product (id, created_at, updated_at, active, name, description, price, stock, category, highlight, created_by, embedding)
VALUES (gen_random_uuid(), now(), now(), true, 'Electric Kettle', 'Fast boiling electric kettle', 159.90, 38, 'ELETRODOMESTICOS', false, (SELECT id FROM users WHERE email = 'admin@nexus.com'), NULL);

INSERT INTO product (id, created_at, updated_at, active, name, description, price, stock, category, highlight, created_by, embedding)
VALUES (gen_random_uuid(), now(), now(), true, 'Gaming Chair', 'Ergonomic gaming chair with lumbar support', 1299.90, 14, 'MOVEIS', false, (SELECT id FROM users WHERE email = 'admin@nexus.com'), NULL);

INSERT INTO product (id, created_at, updated_at, active, name, description, price, stock, category, highlight, created_by, embedding)
VALUES (gen_random_uuid(), now(), now(), true, 'Router Wi-Fi 6', 'High-speed dual band Wi-Fi 6 router', 499.90, 32, 'ELETRONICOS', false, (SELECT id FROM users WHERE email = 'admin@nexus.com'), NULL);

INSERT INTO product (id, created_at, updated_at, active, name, description, price, stock, category, highlight, created_by, embedding)
VALUES (gen_random_uuid(), now(), now(), true, 'Webcam HD', '1080p webcam with built-in microphone', 219.90, 46, 'ELETRONICOS', false, (SELECT id FROM users WHERE email = 'admin@nexus.com'), NULL);

INSERT INTO product (id, created_at, updated_at, active, name, description, price, stock, category, highlight, created_by, embedding)
VALUES (gen_random_uuid(), now(), now(), true, 'Microphone USB', 'Studio quality USB microphone', 349.90, 29, 'ELETRONICOS', false, (SELECT id FROM users WHERE email = 'admin@nexus.com'), NULL);

INSERT INTO product (id, created_at, updated_at, active, name, description, price, stock, category, highlight, created_by, embedding)
VALUES (gen_random_uuid(), now(), now(), true, 'Power Bank 20000mAh', 'Portable fast-charging power bank', 179.90, 70, 'ACESSORIOS', false, (SELECT id FROM users WHERE email = 'admin@nexus.com'), NULL);

INSERT INTO product (id, created_at, updated_at, active, name, description, price, stock, category, highlight, created_by, embedding)
VALUES (gen_random_uuid(), now(), now(), true, 'Smart TV 50"', '50-inch 4K Smart TV with HDR', 2799.00, 10, 'ELETRONICOS', false, (SELECT id FROM users WHERE email = 'admin@nexus.com'), NULL);

INSERT INTO product (id, created_at, updated_at, active, name, description, price, stock, category, highlight, created_by, embedding)
VALUES (gen_random_uuid(), now(), now(), true, 'Soundbar', 'Home theater soundbar system', 899.90, 21, 'ELETRONICOS', false, (SELECT id FROM users WHERE email = 'admin@nexus.com'), NULL);

INSERT INTO product (id, created_at, updated_at, active, name, description, price, stock, category, highlight, created_by, embedding)
VALUES (gen_random_uuid(), now(), now(), true, 'Hair Dryer', 'Compact hair dryer with multiple settings', 129.90, 58, 'ELETRODOMESTICOS', false, (SELECT id FROM users WHERE email = 'admin@nexus.com'), NULL);

INSERT INTO product (id, created_at, updated_at, active, name, description, price, stock, category, highlight, created_by, embedding)
VALUES (gen_random_uuid(), now(), now(), true, 'Electric Iron', 'Steam electric iron', 149.90, 44, 'ELETRODOMESTICOS', false, (SELECT id FROM users WHERE email = 'admin@nexus.com'), NULL);

INSERT INTO product (id, created_at, updated_at, active, name, description, price, stock, category, highlight, created_by, embedding)
VALUES (gen_random_uuid(), now(), now(), true, 'Mixer', 'Kitchen mixer with multiple speeds', 299.90, 26, 'ELETRODOMESTICOS', false, (SELECT id FROM users WHERE email = 'admin@nexus.com'), NULL);

INSERT INTO product (id, created_at, updated_at, active, name, description, price, stock, category, highlight, created_by, embedding)
VALUES (gen_random_uuid(), now(), now(), true, 'Dress Casual', 'Light casual dress for everyday wear', 139.90, 48, 'ROUPAS', false, (SELECT id FROM users WHERE email = 'admin@nexus.com'), NULL);

INSERT INTO product (id, created_at, updated_at, active, name, description, price, stock, category, highlight, created_by, embedding)
VALUES (gen_random_uuid(), now(), now(), true, 'Shorts Sport', 'Breathable sports shorts', 79.90, 67, 'ROUPAS', false, (SELECT id FROM users WHERE email = 'admin@nexus.com'), NULL);

INSERT INTO product (id, created_at, updated_at, active, name, description, price, stock, category, highlight, created_by, embedding)
VALUES (gen_random_uuid(), now(), now(), true, 'Cap Adjustable', 'Adjustable cap with curved brim', 59.90, 85, 'ACESSORIOS', false, (SELECT id FROM users WHERE email = 'admin@nexus.com'), NULL);

INSERT INTO product (id, created_at, updated_at, active, name, description, price, stock, category, highlight, created_by, embedding)
VALUES (gen_random_uuid(), now(), now(), true, 'Notebook 15"', '15-inch notebook for daily tasks', 3499.00, 16, 'ELETRONICOS', false, (SELECT id FROM users WHERE email = 'admin@nexus.com'), NULL);

INSERT INTO product (id, created_at, updated_at, active, name, description, price, stock, category, highlight, created_by, embedding)
VALUES (gen_random_uuid(), now(), now(), true, 'Mouse Pad XL', 'Extended mouse pad for desk setup', 89.90, 73, 'ACESSORIOS', false, (SELECT id FROM users WHERE email = 'admin@nexus.com'), NULL);

INSERT INTO product (id, created_at, updated_at, active, name, description, price, stock, category, highlight, created_by, embedding)
VALUES (gen_random_uuid(), now(), now(), true, 'LED Strip Lights', 'RGB LED strip with remote control', 119.90, 52, 'ACESSORIOS', false, (SELECT id FROM users WHERE email = 'admin@nexus.com'), NULL);

INSERT INTO product (id, created_at, updated_at, active, name, description, price, stock, category, highlight, created_by, embedding)
VALUES (gen_random_uuid(), now(), now(), true, 'Bedside Table', 'Compact bedside table with drawer', 279.90, 19, 'MOVEIS', false, (SELECT id FROM users WHERE email = 'admin@nexus.com'), NULL);

INSERT INTO product (id, created_at, updated_at, active, name, description, price, stock, category, highlight, created_by, embedding)
VALUES (gen_random_uuid(), now(), now(), true, 'Wardrobe 3 Doors', 'Spacious wardrobe with 3 doors', 1599.00, 8, 'MOVEIS', false, (SELECT id FROM users WHERE email = 'admin@nexus.com'), NULL);

INSERT INTO product (id, created_at, updated_at, active, name, description, price, stock, category, highlight, created_by, embedding)
VALUES (gen_random_uuid(), now(), now(), true, 'Dish Rack', 'Stainless steel dish drying rack', 99.90, 61, 'ELETRODOMESTICOS', false, (SELECT id FROM users WHERE email = 'admin@nexus.com'), NULL);

INSERT INTO product (id, created_at, updated_at, active, name, description, price, stock, category, highlight, created_by, embedding)
VALUES (gen_random_uuid(), now(), now(), true, 'Toaster', '2-slice toaster with browning control', 139.90, 37, 'ELETRODOMESTICOS', false, (SELECT id FROM users WHERE email = 'admin@nexus.com'), NULL);