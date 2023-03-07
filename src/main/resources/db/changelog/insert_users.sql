INSERT INTO users (id, name, surname, email, password, role, is_activated)
VALUES (1, 'Mark', 'Ivanov', 'mark.ivanov@gmail.com', '$2a$10$wnXXYMX2IUTMtC7ajTIeM.dMLEZcpMjorqfdvAFhdOeYiJW3WXewG',
        'CLIENT', true),
       (2, 'John', 'Smith', 'johnsmith@gmail.com', '$2a$10$wnXXYMX2IUTMtC7ajTIeM.dMLEZcpMjorqfdvAFhdOeYiJW3WXewG',
        'CLIENT', true),
       (3, 'Mike', 'Jordan', 'mikejordan@gmail.com', '$2a$10$wnXXYMX2IUTMtC7ajTIeM.dMLEZcpMjorqfdvAFhdOeYiJW3WXewG',
        'CLIENT', false),
       (4, 'Ivan', 'Petrov', 'ivan.petrov@gmail.com', '$2a$10$wnXXYMX2IUTMtC7ajTIeM.dMLEZcpMjorqfdvAFhdOeYiJW3WXewG',
        'EMPLOYEE', true);