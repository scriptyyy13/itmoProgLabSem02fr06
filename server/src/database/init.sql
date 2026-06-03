-- Роли пользователей
-- Админ может и серверами в балансере управлять и делать любые операции со всеми объектами коллекциями
-- Простой пользователь может управлять только своими объектами коллекции
-- Перенесено в код

-- Таблица пользователей
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    login VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(56) NOT NULL, -- SHA-224 (HEX)
    role roles DEFAULT 'user'
);

-- Таблица драконов (вложенные поля храним в одной строке, чтобы не плодить потом join'ы)
CREATE TABLE IF NOT EXISTS dragons (
      id SERIAL PRIMARY KEY,
      creator_id INT NOT NULL,
      name TEXT NOT NULL,

    -- Coordinates
      coord_x REAL NOT NULL,
      coord_y DOUBLE PRECISION NOT NULL,

      creation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
      age BIGINT NOT NULL,
      weight INT,
      speaking BOOLEAN NOT NULL,
      color VARCHAR(20),

    -- Person (Killer)
      killer_name TEXT,
      killer_birthday DATE,
      killer_passport_id TEXT,
      killer_nationality VARCHAR(20),

    -- Location (внутри Killer)
      killer_loc_x INT,
      killer_loc_y INT,
      killer_loc_z INT,
      killer_loc_name TEXT,

      FOREIGN KEY (creator_id) REFERENCES users(id) ON DELETE CASCADE
);