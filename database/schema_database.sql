DROP DATABASE IF EXISTS libronova;

CREATE DATABASE libronova CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE libronova;

-- -----------------------------------------------------
-- Table: books
-- -----------------------------------------------------
CREATE TABLE books (
  isbn VARCHAR(13) NOT NULL,
  title VARCHAR(255) NOT NULL,
  author VARCHAR(255) NOT NULL,
  category VARCHAR(100) NULL,
  total_copies INT NOT NULL DEFAULT 0,
  available_copies INT NOT NULL DEFAULT 0,
  reference_price DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
  is_active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (isbn),
  INDEX idx_author (author),
  INDEX idx_category (category)
) ENGINE=InnoDB;


-- -----------------------------------------------------
-- Table: users
-- -----------------------------------------------------
CREATE TABLE users (
  id INT NOT NULL AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL,
  password VARCHAR(255) NOT NULL, 
  role ENUM('ADMIN', 'ASSISTANT') NOT NULL,
  is_active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE INDEX uq_username (username ASC)
) ENGINE=InnoDB;


-- -----------------------------------------------------
-- Table: members
-- -----------------------------------------------------
CREATE TABLE members (
  id INT NOT NULL AUTO_INCREMENT,
  first_name VARCHAR(100) NOT NULL,
  last_name VARCHAR(100) NOT NULL,
  email VARCHAR(255) NOT NULL,
  status ENUM('ACTIVE', 'INACTIVE') NOT NULL DEFAULT 'ACTIVE',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE INDEX uq_email (email ASC)
) ENGINE=InnoDB;


-- -----------------------------------------------------
-- Table: loans
-- -----------------------------------------------------
CREATE TABLE loans (
  id INT NOT NULL AUTO_INCREMENT,
  book_isbn VARCHAR(13) NOT NULL,
  member_id INT NOT NULL,
  loan_date DATE NOT NULL,
  due_date DATE NOT NULL,
  return_date DATE NULL,
  fine DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
  is_returned BOOLEAN NOT NULL DEFAULT FALSE,
  PRIMARY KEY (id),
  INDEX fk_loans_books_idx (book_isbn ASC),
  INDEX fk_loans_members_idx (member_id ASC),
  CONSTRAINT fk_loans_books
    FOREIGN KEY (book_isbn)
    REFERENCES books (isbn)
    ON DELETE RESTRICT 
    ON UPDATE CASCADE,
  CONSTRAINT fk_loans_members
    FOREIGN KEY (member_id)
    REFERENCES members (id)
    ON DELETE RESTRICT 
    ON UPDATE CASCADE
) ENGINE=InnoDB;