-- Ensure the 'users' table exists
CREATE TABLE IF NOT EXISTS "users" (
    id UUID PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

-- Insert admin user
INSERT INTO "users" (id, email, password, role)
SELECT '223e4567-e89b-12d3-a456-426614174006', 'testuser@test.com',
       '$2b$12$7hoRZfJrRKD2nIm2vHLs7OBETy.LWenXXMLKf99W8M4PUwO6KB7fu', 'ADMIN'
WHERE NOT EXISTS (
    SELECT 1 FROM "users" WHERE id = '223e4567-e89b-12d3-a456-426614174006' OR email = 'testuser@test.com'
);

-- Insert seed client user
INSERT INTO "users" (id, email, password, role)
SELECT '11111111-1111-1111-1111-111111111111', 'client@test.com',
       '$2b$12$7hoRZfJrRKD2nIm2vHLs7OBETy.LWenXXMLKf99W8M4PUwO6KB7fu', 'ROLE_CLIENT'
WHERE NOT EXISTS (
    SELECT 1 FROM "users" WHERE id = '11111111-1111-1111-1111-111111111111' OR email = 'client@test.com'
);

-- Insert seed developer user
INSERT INTO "users" (id, email, password, role)
SELECT '22222222-2222-2222-2222-222222222222', 'developer@test.com',
       '$2b$12$7hoRZfJrRKD2nIm2vHLs7OBETy.LWenXXMLKf99W8M4PUwO6KB7fu', 'ROLE_DEVELOPER'
WHERE NOT EXISTS (
    SELECT 1 FROM "users" WHERE id = '22222222-2222-2222-2222-222222222222' OR email = 'developer@test.com'
);
