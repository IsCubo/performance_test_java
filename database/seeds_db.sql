-- -----------------------------------------------------
-- Example to inject data to database
-- -----------------------------------------------------

-- Books
INSERT INTO books (isbn, title, author, category, total_copies, available_copies, reference_price, is_active, created_at) VALUES
('9788437604947', 'Cien años de soledad', 'Gabriel García Márquez', 'Novela', 10, 8, 22.50, TRUE, NOW()),
('9788499890974', 'Elantris', 'Brandon Sanderson', 'Fantasía', 5, 5, 25.00, TRUE, NOW()),
('9780307474278', 'Ready Player One', 'Ernest Cline', 'Ciencia Ficción', 7, 6, 18.75, TRUE, NOW()),
('9788420412038', 'El nombre del viento', 'Patrick Rothfuss', 'Fantasía', 4, 3, 21.90, TRUE, NOW()),
('9788498382679', 'Los juegos del hambre', 'Suzanne Collins', 'Ciencia Ficción', 12, 12, 15.00, FALSE, NOW());

-- Users
INSERT INTO users (username, password, role, is_active, created_at) VALUES
('admin', 'admin', 'ADMIN', TRUE, NOW()),
('asistente', 'asistente', 'ASSISTANT', TRUE, NOW()),
('user_inactive', 'test', 'ASSISTANT', FALSE, NOW());


-- Members
INSERT INTO members (first_name, last_name, email, status, created_at) VALUES
('Ana', 'García', 'ana.garcia@email.com', 'ACTIVE', NOW()),
('Carlos', 'Rodriguez', 'carlos.r@email.com', 'ACTIVE', NOW()),
('Laura', 'Martinez', 'laura.martinez@email.com', 'INACTIVE', NOW());

-- Loans
INSERT INTO loans (book_isbn, member_id, loan_date, due_date, return_date, fine, is_returned) VALUES
('9788437604947', 1, '2025-09-01', '2025-09-08', NULL, 0.00, FALSE), -- Préstamo activo
('9780307474278', 2, '2025-09-05', '2025-09-12', NULL, 0.00, FALSE), -- Préstamo activo
('9788420412038', 1, '2025-08-15', '2025-08-22', '2025-08-23', 1.50, TRUE), -- Devuelto con multa
('9788437604947', 2, '2025-07-10', '2025-07-17', NULL, 0.00, FALSE); -- Préstamo VENCIDO
