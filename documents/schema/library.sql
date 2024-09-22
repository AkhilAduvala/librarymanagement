select user();

SHOW VARIABLES WHERE Variable_name = 'hostname';

SELECT * FROM information_schema.SCHEMA_PRIVILEGES WHERE TABLE_SCHEMA = 'library';

SELECT user, host FROM mysql.user;

CREATE USER 'library_user'@'localhost' IDENTIFIED BY 'library_password';

GRANT ALL PRIVILEGES ON library.* TO 'library_user'@'localhost';

FLUSH PRIVILEGES;

create Table Authors(
	author_id int primary key auto_increment,
    author_name varchar(60)
);


CREATE TABLE Books (
    book_id INT PRIMARY KEY AUTO_INCREMENT,
    book_name VARCHAR(255),
    author_id int,
    FOREIGN KEY (author_id)
        REFERENCES Authors(author_id),
    published_year INT,
    total_qty INT,
    available_qty INT
);


CREATE TABLE students (
    student_id INT PRIMARY KEY,
    student_name VARCHAR(60)
);

CREATE TABLE history (
    transactonId INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT,
    FOREIGN KEY (student_id)
        REFERENCES students (student_id),
    book_id INT,
    FOREIGN KEY (book_id)
        REFERENCES books (book_id),
    assigned_on DATE,
    return_on DATE,
    status VARCHAR(15)
);

alter table books add status varchar(30) default 'Available';

ALTER TABLE books ADD CONSTRAINT chk_status CHECK (status IN ('Available', 'Archive'));

INSERT INTO authors (author_name) VALUES ('Hector');

select * from authors;

select * from books;

SELECT 
    CASE
        WHEN COUNT(*) > 0 THEN TRUE
        ELSE FALSE
    END
FROM
    books
WHERE
    book_name = 'a' AND authorId = 1
        AND status = 'Available';
