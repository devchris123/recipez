-- All `id` columns are of type BIGINT and are autoincremented
-- No storage optimization is being considered for the `id`columns
-- This could later be added if useful for performance
-- All `public_id` are generated by the application
-- via a nanoid implementation if needed for usage outside of the application
-- This is to make the id less predictable in public usage.
USE recipedb;

CREATE TABLE IF NOT EXISTS recipe (
    id BIGINT ZEROFILL NOT NULL AUTO_INCREMENT,
    publicId VARCHAR(12) NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `recipe_public_id_idx` (`publicId`)
) ENGINE = InnoDB;

ALTER TABLE recipe ALTER description SET DEFAULT '';

-- Create a user for the atosapp using a password hash
-- The hash was retrieved by using `SELECT PASSWORD('yourpassword')` on a mariadb shell
CREATE USER IF NOT EXISTS recipeapp IDENTIFIED BY PASSWORD '*2470C0C06DEE42FD1618BB99005ADCA2EC9D1E19';

-- Grant privileges to the app user
