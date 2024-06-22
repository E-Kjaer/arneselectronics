create table customer(
     id serial PRIMARY KEY,
     name VARCHAR(50) NOT NULL,
     phoneNumber VARCHAR(8) UNIQUE NOT NULL,
     email VARCHAR(355) UNIQUE NOT NULL,
     createdOn TIMESTAMP not null
);

create table paymentOrder(
     id serial primary key,
     customerID INT REFERENCES customer(id) ON DELETE CASCADE,
     ownerName VARCHAR(50) NOT NULL,
     cardNumber VARCHAR(16) NOT NULL ,
     cardMonth CHAR(2) NOT NULL,
     cardYear CHAR(4) NOT NULL,
     cardCVV CHAR(3) NOT NULL
);

CREATE TABLE stock(
    id INTEGER PRIMARY KEY NOT NULL,
    name VARCHAR(255) NOT NULL,
    stock_price DECIMAL(10,2) NOT NULL,
    amount INTEGER NOT NULL
);

/*
    It makes okay sense that the customer_id, payment_id is not a reference, since if it was and customer got deleted it would also
    Still need to look at it
*/
CREATE TABLE orders(
    id SERIAL PRIMARY KEY NOT NULL,
    customer_id INTEGER NOT NULL,
    payment_id INTEGER NOT NULL,
    address VARCHAR(255)  NOT NULL,
    unique_product_amount INTEGER NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,
    date TIMESTAMP not null,
    status VARCHAR(255) NOT NULL
);

/*
    It makes okay sense that the product_id is not a reference, since if it was and the product got deleted it would also
    Still need to look at it
*/
CREATE TABLE orderLines(
    id SERIAL PRIMARY KEY NOT NULL,
    order_id REFERENCES orders(id) INTEGER NOT NULL ON DELETE CASCADE,
    product_id INTEGER NOT NULL,
    amount INTEGER NOT NULL,
    buy_price DECIMAL(10,2) NOT NULL
)


/*
    No need for this anymore
*/
create table paymentSave(
        id serial primary key,
        customerID INT UNIQUE REFERENCES customer(id) ON DELETE CASCADE,
        ownerName VARCHAR(50) NOT NULL,
        cardNumber VARCHAR(16) not null unique ,
        cardMonth CHAR(2) not null,
        cardYear CHAR(4) not null,
        cardCVV CHAR(3) not null,
        createdOn TIMESTAMP not null
);


