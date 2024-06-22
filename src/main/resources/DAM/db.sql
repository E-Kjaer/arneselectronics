
CREATE TABLE images (
    id SERIAL PRIMARY KEY,
    path VARCHAR(100) UNIQUE,
    original_ref INT references images(id)
);

/*ALTER TABLE images
    ADD original_ref INT,
    ADD CONSTRAINT fk_original_ref FOREIGN KEY (original_ref) REFERENCES images(id);*/


CREATE TABLE tags (
    id SERIAL PRIMARY KEY,
    key VARCHAR(100),
    value VARCHAR(100),
    UNIQUE(key, value)
);


CREATE TABLE products (
    id serial PRIMARY KEY,
    name VARCHAR(100)
);


CREATE TABLE image_tags (
    image_id INT REFERENCES images(id),
    tag_id INT REFERENCES tags(id),
    PRIMARY KEY (image_id, tag_id)
);


CREATE TABLE image_products (
    product_id int REFERENCES products(id),
    image_id INT REFERENCES images(id),
    PRIMARY KEY (image_id, product_id)
);




INSERT INTO tags (key, value) VALUES
('Color', 'Blue'), ('Color', 'Yellow'),
('Color', 'Green'), ('Color', 'Black'),
('Color', 'White'), ('Color', 'Red'),
('Color', 'Purple'), ('Color', 'Orange'),
('Color', 'Pink'), ('Color', 'Brown'),
('Brand', 'HP'), ('Brand', 'Acer'),
('Brand', 'Samsung'), ('Brand', 'Asus'),
('Brand', 'Apple'), ('Brand', 'Google'),
('Brand', 'Oneplus'), ('Brand', 'Logitech'),
('Brand', 'Nos'), ('Brand', 'Corsair'),
('Brand', 'Lg'), ('Brand', 'Jbl'),
('Brand', 'Hyperx'), ('Brand', 'Piranha'),
('Brand', 'Arozzi'), ('Brand', 'Acezone'),
('Brand', 'The shrimp'),('Category', 'Laptop'),
('Category', 'Monitor'), ('Size', 'Lille'),
('Category', 'Phone'), ('Category', 'Mouse'),
('Category', 'Webcam'), ('Category', 'Tv'),
('Category', 'Headset'), ('Category', 'Chair'),
('Category', 'keyboard'), ('Size', 'Stor'),
('Logo', 'Logo');




INSERT INTO images (path) VALUES
('src\main\resources\DAM\produktBilleder\acer-aspire-lille.png'), ('src\main\resources\DAM\produktBilleder\acer-aspire-stor.png'),
('src/main/resources/DAM/produktBilleder/acezone-a-lille.png'), ('src\main\resources\DAM\produktBilleder\acezone-a-stor.png'),
('src\main\resources\DAM\produktBilleder\arozzi-arena-lille.png'), ('src\main\resources\DAM\produktBilleder\arozzi-arena-stor.png'),
('src\main\resources\DAM\produktBilleder\arozzi-vernazza-lille.png'), ('src\main\resources\DAM\produktBilleder\arozzi-vernazza-stor.png'),
('src\main\resources\DAM\produktBilleder\asus-vg27aq-lille.png'), ('src\main\resources\DAM\produktBilleder\asus-vg27aq-stor.png'),
('src\main\resources\DAM\produktBilleder\corsair-k55-lille.png'), ('src\main\resources\DAM\produktBilleder\corsair-k55-stor.png'),
('src\main\resources\DAM\produktBilleder\google-pixel-blå-lille.png'), ('src\main\resources\DAM\produktBilleder\google-pixel-blå-stor.png'),
('src\main\resources\DAM\produktBilleder\google-pixel-lille.png'), ('src\main\resources\DAM\produktBilleder\google-pixel-stor.png'),
('src\main\resources\DAM\produktBilleder\hp-laptop-i3-lille.png'), ('src\main\resources\DAM\produktBilleder\hp-laptop-i3-stor.png'),
('src\main\resources\DAM\produktBilleder\hp-laptop-ryzen-lille.png'), ('src\main\resources\DAM\produktBilleder\hp-laptop-ryzen-stor.png'),
('src\main\resources\DAM\produktBilleder\hyperx-cloud-lille.png'), ('src\main\resources\DAM\produktBilleder\hyperx-cloud-stor.png'),
('src\main\resources\DAM\produktBilleder\iphone-15-lille.png'), ('src\main\resources\DAM\produktBilleder\iphone-15-stor.png'),
('src\main\resources\DAM\produktBilleder\iphone-15-pro-lille.png'), ('src\main\resources\DAM\produktBilleder\iphone-15-pro-stor.png'),
('src\main\resources\DAM\produktBilleder\jbl-quantum-lille.png'), ('src\main\resources\DAM\produktBilleder\jbl-quantum-stor.png'),
('src\main\resources\DAM\produktBilleder\lg-55-lille.png'), ('src\main\resources\DAM\produktBilleder\lg-55-stor.png'),
('src\main\resources\DAM\produktBilleder\logitech-brio-hvid-lille.png'), ('src\main\resources\DAM\produktBilleder\logitech-brio-hvid-stor.png'),
('src\main\resources\DAM\produktBilleder\logitech-brio-lille.png'), ('src\main\resources\DAM\produktBilleder\logitech-brio-stor.png'),
('src\main\resources\DAM\produktBilleder\logitech-brio-pink-ille.png'), ('src\main\resources\DAM\produktBilleder\logitech-brio-pink-stor.png'),
('src\main\resources\DAM\produktBilleder\logitech-g413-lille.png'), ('src\main\resources\DAM\produktBilleder\logitech-g413-stor.png'),
('src\main\resources\DAM\produktBilleder\logitech-g502-lille.png'), ('src\main\resources\DAM\produktBilleder\logitech-g502-stor.png'),
('src\main\resources\DAM\produktBilleder\logitech-mx-lille.png'), ('src\main\resources\DAM\produktBilleder\logitech-mx-stor.png'),
('src\main\resources\DAM\produktBilleder\nos-m-650-lille.png'), ('src\main\resources\DAM\produktBilleder\nos-m-650-stor.png'),
('src\main\resources\DAM\produktBilleder\oneplus-nord-lille.png'), ('src\main\resources\DAM\produktBilleder\oneplus-nord-stor.png'),
('src\main\resources\DAM\produktBilleder\piranha-rush-lille.png'), ('src\main\resources\DAM\produktBilleder\piranha-rush-stor.png'),
('src\main\resources\DAM\produktBilleder\samsung-55-lille.png'), ('src\main\resources\DAM\produktBilleder\samsung-55-stor.png'),
('src\main\resources\DAM\produktBilleder\samsung-65-lille.png'), ('src\main\resources\DAM\produktBilleder\samsung-65-stor.png'),
('src\main\resources\DAM\produktBilleder\samsung-galaxy-s22-lille.png'), ('src\main\resources\DAM\produktBilleder\samsung-galaxy-s22-stor.png'),
('src\main\resources\DAM\produktBilleder\samsung-odyssey-lille.png'), ('src\main\resources\DAM\produktBilleder\samsung-odyssey-stor.png'),
('src\main\resources\DAM\produktBilleder\samsung-odyssey-q8-lille.png'), ('src\main\resources\DAM\produktBilleder\samsung-odyssey-q8-stor.png'),
('src\main\resources\DAM\produktBilleder\the-shrimp-lille.png'), ('src/main/resources\DAM\produktBilleder\the-shrimp-stor.png'),
('src/main/java/SHOP/data/dummies/DAM/images/arne.png');

INSERT INTO images (path) VALUES
                              ('src/main/resources/DAM/produktBilleder/acer-aspire-lille.png'), ('src/main/resources/DAM/produktBilleder/acer-aspire-stor.png'),
                              ('src/main/resources/DAM/produktBilleder/acezone-a-lille.png'), ('src/main/resources/DAM/produktBilleder/acezone-a-stor.png'),
                              ('src/main/resources/DAM/produktBilleder/arozzi-arena-lille.png'), ('src/main/resources/DAM/produktBilleder/arozzi-arena-stor.png'),
                              ('src/main/resources/DAM/produktBilleder/arozzi-vernazza-lille.png'), ('src/main/resources/DAM/produktBilleder/arozzi-vernazza-stor.png'),
                              ('src/main/resources/DAM/produktBilleder/asus-vg27aq-lille.png'), ('src/main/resources/DAM/produktBilleder/asus-vg27aq-stor.png'),
                              ('src/main/resources/DAM/produktBilleder/corsair-k55-lille.png'), ('src/main/resources/DAM/produktBilleder/corsair-k55-stor.png'),
                              ('src/main/resources/DAM/produktBilleder/google-pixel-blå-lille.png'), ('src/main/resources/DAM/produktBilleder/google-pixel-blå-stor.png'),
                              ('src/main/resources/DAM/produktBilleder/google-pixel-lille.png'), ('src/main/resources/DAM/produktBilleder/google-pixel-stor.png'),
                              ('src/main/resources/DAM/produktBilleder/hp-laptop-i3-lille.png'), ('src/main/resources/DAM/produktBilleder/hp-laptop-i3-stor.png'),
                              ('src/main/resources/DAM/produktBilleder/hp-laptop-ryzen-lille.png'), ('src/main/resources/DAM/produktBilleder/hp-laptop-ryzen-stor.png'),
                              ('src/main/resources/DAM/produktBilleder/hyperx-cloud-lille.png'), ('src/main/resources/DAM/produktBilleder/hyperx-cloud-stor.png'),
                              ('src/main/resources/DAM/produktBilleder/iphone-15-lille.png'), ('src/main/resources/DAM/produktBilleder/iphone-15-stor.png'),
                              ('src/main/resources/DAM/produktBilleder/iphone-15-pro-lille.png'), ('src/main/resources/DAM/produktBilleder/iphone-15-pro-stor.png'),
                              ('src/main/resources/DAM/produktBilleder/jbl-quantum-lille.png'), ('src/main/resources/DAM/produktBilleder/jbl-quantum-stor.png'),
                              ('src/main/resources/DAM/produktBilleder/lg-55-lille.png'), ('src/main/resources/DAM/produktBilleder/lg-55-stor.png'),
                              ('src/main/resources/DAM/produktBilleder/logitech-brio-hvid-lille.png'), ('src/main/resources/DAM/produktBilleder/logitech-brio-hvid-stor.png'),
                              ('src/main/resources/DAM/produktBilleder/logitech-brio-lille.png'), ('src/main/resources/DAM/produktBilleder/logitech-brio-stor.png'),
                              ('src/main/resources/DAM/produktBilleder/logitech-brio-pink-ille.png'), ('src/main/resources/DAM/produktBilleder/logitech-brio-pink-stor.png'),
                              ('src/main/resources/DAM/produktBilleder/logitech-g413-lille.png'), ('src/main/resources/DAM/produktBilleder/logitech-g413-stor.png'),
                              ('src/main/resources/DAM/produktBilleder/logitech-g502-lille.png'), ('src/main/resources/DAM/produktBilleder/logitech-g502-stor.png'),
                              ('src/main/resources/DAM/produktBilleder/logitech-mx-lille.png'), ('src/main/resources/DAM/produktBilleder/logitech-mx-stor.png'),
                              ('src/main/resources/DAM/produktBilleder/nos-m-650-lille.png'), ('src/main/resources/DAM/produktBilleder/nos-m-650-stor.png'),
                              ('src/main/resources/DAM/produktBilleder/oneplus-nord-lille.png'), ('src/main/resources/DAM/produktBilleder/oneplus-nord-stor.png'),
                              ('src/main/resources/DAM/produktBilleder/piranha-rush-lille.png'), ('src/main/resources/DAM/produktBilleder/piranha-rush-stor.png'),
                              ('src/main/resources/DAM/produktBilleder/samsung-55-lille.png'), ('src/main/resources/DAM/produktBilleder/samsung-55-stor.png'),
                              ('src/main/resources/DAM/produktBilleder/samsung-65-lille.png'), ('src/main/resources/DAM/produktBilleder/samsung-65-stor.png'),
                              ('src/main/resources/DAM/produktBilleder/samsung-galaxy-s22-lille.png'), ('src/main/resources/DAM/produktBilleder/samsung-galaxy-s22-stor.png'),
                              ('src/main/resources/DAM/produktBilleder/samsung-odyssey-lille.png'), ('src/main/resources/DAM/produktBilleder/samsung-odyssey-stor.png'),
                              ('src/main/resources/DAM/produktBilleder/samsung-odyssey-q8-lille.png'), ('src/main/resources/DAM/produktBilleder/samsung-odyssey-q8-stor.png'),
                              ('src/main/resources/DAM/produktBilleder/the-shrimp-lille.png'), ('src/main/resources/DAM/produktBilleder/the-shrimp-stor.png'),
                              ('src/main/java/SHOP/data/dummies/DAM/images/arne.png');


INSERT INTO products (id, name) VALUES
(3, 'Acer Aspire'),
(27, 'Acezone A'),
(30, 'Arozzi Arena'),
(29, 'Arozzi Vernazza'),
(5, 'ASUS VG27AQ'),
(16, 'Corsair K55'),
(10, 'Google Pixel'),
(11, 'Google Pixel Blå'),
(1, 'HP Laptop i3'),
(2, 'HP Laptop Ryzen'),
(25, 'HyperX Cloud'),
(9, 'iPhone 15'),
(7, 'iPhone 15 Pro'),
(26, 'JBL Quantum'),
(23, 'LG 55'),
(19, 'Logitech Brio Hvid'),
(21, 'Logitech Brio'),
(20, 'Logitech Brio Pink'),
(17, 'Logitech G413'),
(13, 'Logitech G502'),
(15, 'Logitech MX'),
(14, 'NOS M-650'),
(12, 'OnePlus Nord'),
(28, 'Piranha Rush'),
(22, 'Samsung 55'),
(24, 'Samsung 65'),
(8, 'Samsung Galaxy S22'),
(4, 'Samsung Odyssey'),
(6, 'Samsung Odyssey Q8'),
(18, 'The Shrimp');

INSERT INTO image_tags (image_id, tag_id) VALUES
    (61, 39);

INSERT INTO image_products (product_id, image_id) VALUES
(3, 1),
(3, 2),
(27, 3),
(27, 4),
(30, 5),
(30, 6),
(29, 7),
(29, 8),
(5, 9),
(5, 10),
(16, 11),
(16, 12),
(10, 13),
(10, 14),
(11, 15),
(11, 16),
(1, 17),
(1, 18),
(2, 19),
(2, 20),
(25, 21),
(25, 22),
(9, 23),
(9, 24),
(7, 25),
(7, 26),
(26, 27),
(26, 28),
(23, 29),
(23, 30),
(19, 31),
(19, 32),
(21, 33),
(21, 34),
(20, 35),
(20, 36),
(17, 37),
(17, 38),
(13, 39),
(13, 40),
(15, 41),
(15, 42),
(14, 43),
(14, 44),
(12, 45),
(12, 46),
(28, 47),
(28, 48),
(22, 49),
(22, 50),
(24, 51),
(24, 52),
(8, 53),
(8, 54),
(4, 55),
(4, 56),
(6, 57),
(6, 58),
(18, 59),
(18, 60);

INSERT INTO image_tags (image_id, tag_id) VALUES
(61, 39), /* Logo tag*/
(6, 69),
(6, 85),
(6, 95),
(7, 69),
(7, 85),
(7, 94),

(8, 83),
(8, 91),
(8, 95),
(9, 83),
(9, 91),
(9, 94),

(10, 82),
(10, 92),
(10, 95),
(11, 82),
(11, 92),
(11, 94),

(12, 82),
(12, 92),
(12, 95),
(13, 82),
(13, 92),
(13, 94),

(14, 71),
(14, 86),
(14, 95),
(15, 71),
(15, 86),
(15, 94),

(16, 77),
(16, 93),
(16, 95),
(17, 77),
(17, 93),
(17, 94),

(18, 73),
(18, 87),
(18, 7),
(18, 95),
(19, 73),
(19, 87),
(19, 7),
(19, 94),
(20, 73),
(20, 87),
(20, 95),
(21, 73),
(21, 87),
(21, 94),

(22, 68),
(22, 85),
(22, 95),
(23, 68),
(23, 85),
(23, 94),

(24, 68),
(24, 85),
(24, 95),
(25, 68),
(25, 85),
(25, 94),

(26, 80),
(26, 91),
(26, 95),
(27, 80),
(27, 91),
(27, 94),

(28, 72),
(28, 87),
(28, 95),
(29, 72),
(29, 87),
(29, 94),

(30, 72),
(30, 87),
(30, 95),
(31, 72),
(31, 87),
(31, 94),

(32, 79),
(32, 91),
(32, 95),
(33, 79),
(33, 91),
(33, 94),

(34, 78),
(34, 90),
(34, 95),
(35, 78),
(35, 90),
(35, 94),

(36, 75),
(36, 89),
(36, 11),
(36, 95),
(37, 75),
(37, 89),
(37, 11),
(37, 94),
(38, 75),
(38, 89),
(38, 95),
(39, 75),
(39, 89),
(39, 94),
(40, 75),
(40, 89),
(40, 15),
(40, 95),
(41, 75),
(41, 89),
(41, 15),
(41, 94),

(42, 75),
(42, 93),
(42, 95),
(43, 75),
(43, 93),
(43, 94),

(44, 75),
(44, 88),
(44, 95),
(45, 75),
(45, 88),
(45, 94),

(46, 75),
(46, 88),
(46, 95),
(47, 75),
(47, 88),
(47, 94),

(48, 76),
(48, 88),
(48, 95),
(49, 76),
(49, 88),
(49, 94),

(50, 74),
(50, 87),
(50, 95),
(51, 74),
(51, 87),
(51, 94),

(52, 81),
(52, 91),
(52, 95),
(53, 81),
(53, 91),
(53, 94),

(54, 70),
(54, 90),
(54, 95),
(55, 70),
(55, 90),
(55, 94),

(56, 70),
(56, 90),
(56, 95),
(57, 70),
(57, 90),
(57, 94),

(58, 70),
(58, 87),
(58, 95),
(59, 70),
(59, 87),
(59, 94),

(60, 70),
(60, 86),
(60, 95),
(61, 70),
(61, 86),
(61, 94),

(62, 70),
(62, 86),
(62, 95),
(63, 70),
(63, 86),
(63, 94),

(64, 84),
(64, 92),
(64, 95),
(65, 84),
(65, 92),
(65, 94);










CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50),
    password VARCHAR(50)
);


INSERT INTO users (name, password) VALUES
('admin', 'password'), ('admin', 'password2');


CREATE ROLE "Marius-PC" LOGIN PASSWORD 'aloevera';

CREATE ROLE "Nicolai-PC" LOGIN PASSWORD 'aloevera';


SELECT images.path, t1.value, t2.value 
FROM images
JOIN image_tags AS it1 ON images.id = it1.image_id
JOIN tags AS t1 ON t1.id = it1.tag_id --AND t1.value = ''
JOIN image_tags AS it2 ON images.id = it2.image_id
JOIN tags AS t2 ON it2.tag_id = t2.id AND t2.value = 'lille';




--Nice display af produkter og paths--
SELECT 
    products.name AS product_name,
    images.path AS image_path,
    ARRAY_AGG(DISTINCT tags.key || ': ' || tags.value) AS tags
FROM 
    products
JOIN 
    image_products ON products.name = image_products.product_name
JOIN 
    images ON image_products.image_id = images.id
LEFT JOIN 
    image_tags ON images.id = image_tags.image_id
LEFT JOIN 
    tags ON image_tags.tag_id = tags.id
GROUP BY 
    products.name, images.path
ORDER BY 
    products.name, images.path
WHERE
    products.name = 'Samsung Galaxy S22';


SELECT 
    products.name AS product_name,
    images.path AS image_path,
    ARRAY_AGG(DISTINCT tags.key || ': ' || tags.value) AS tags
FROM 
    products
JOIN 
    image_products ON products.name = image_products.product_name
JOIN 
    images ON image_products.image_id = images.id
LEFT JOIN 
    image_tags ON images.id = image_tags.image_id
LEFT JOIN 
    tags ON image_tags.tag_id = tags.id
WHERE
    products.name = 'Samsung Galaxy S22'
GROUP BY 
    products.name, images.path
ORDER BY 
    products.name, images.path;


-- test database data --

INSERT INTO users (name, password) VALUES
('admin', 'password'), ('admin', 'password2');


--UPDATE FOR LOGIN ENCRYPTION FUNCTION--
--INSERT THE FOLLOWING TO FACILITATE ENCRYPTION for username: admin, password: pw--
alter table users alter column password TYPE varchar(100);
insert into users (name,password) values ('admin','30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4')

