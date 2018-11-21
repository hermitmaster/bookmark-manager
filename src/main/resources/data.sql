-- Init default user (Username: admin, Password: password) and category

-- Users
INSERT INTO users (password, registration_date, username, enabled)
    SELECT * FROM (SELECT '$2a$10$VUbNDU1YVuXPOlizhc8K6u0FZFDipJaIIGSSbpMNeGDyQ2IEcaily', NOW(), 'admin', true) as tmp
WHERE NOT EXISTS (
    SELECT username FROM users WHERE username = 'admin'
) LIMIT 1;

-- Roles
INSERT INTO authorities (username, authority)
    SELECT * FROM (SELECT 'admin' as username, 'ROLE_ADMIN' as authority) as tmp1
WHERE NOT EXISTS(
  SELECT username FROM authorities WHERE username = 'admin' and authority = 'ROLE_ADMIN'
) LIMIT 1;

-- Bookmark Categories
INSERT INTO bookmark_category (created_by_username, date_created, date_modified, name, parent_id)
    SELECT * FROM (SELECT 'admin', NOW() as created, NOW() as modified, 'None', null as parent)as tmp2
WHERE NOT EXISTS(
  SELECT name FROM bookmark_category WHERE name = 'None'
) LIMIT 1;
