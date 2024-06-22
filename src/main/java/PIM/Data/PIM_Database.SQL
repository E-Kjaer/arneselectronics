begin;
DROP TABLE IF EXISTS
    Specification,
    Product,
    Category,
    Brand;

CREATE TABLE Brand
(
    id   serial PRIMARY KEY NOT NULL,
    name varchar UNIQUE     NOT NULL
);

CREATE TABLE Category
(
    id          serial PRIMARY KEY NOT NULL,
    name        varchar(64) UNIQUE NOT NULL,
    description varchar(4096),
    subcategory int references Category (id) -- recursive relationship
);

CREATE TABLE Product
(
    id            serial PRIMARY KEY           NOT NULL,
    name          varchar(64) UNIQUE           NOT NULL,
    description   text,
    EAN           varchar(16) UNIQUE           NOT NULL,
    price         float                        NOT NULL,
    hidden_status boolean                      NOT NULL,
    category_id   int references Category (id) NOT NULL,
    brand_id      int references Brand (id)    NOT NULL
);

CREATE TABLE Specification
(
    id         serial PRIMARY KEY          NOT NULL,
    name       varchar(256)                NOT NULL,
    value      varchar(256)                NOT NULL,
    product_id int references Product (id) NOT NULL
);
commit;



