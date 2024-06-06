-- All `id` columns are of type BIGINT and are autoincremented
-- No storage optimization is being considered for the `id`columns
-- This could later be added if useful for performance
-- All `public_id` are generated by the application
-- via a nanoid implementation if needed for usage outside of the application
-- This is to make the id less predictable in public usage.
USE recipedb;

CREATE TABLE IF NOT EXISTS Recipe (
    id BIGINT ZEROFILL NOT NULL AUTO_INCREMENT,
    publicId VARCHAR(12) NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `recipe_public_id_idx` (`publicId`)
) ENGINE = InnoDB;

ALTER TABLE
    Recipe ALTER description
SET
    DEFAULT '';

CREATE TABLE IF NOT EXISTS Ingredient (
    id BIGINT ZEROFILL NOT NULL AUTO_INCREMENT,
    publicId VARCHAR(12) NULL,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `ingredient_public_id_idx` (`publicId`)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS Unit (
    id BIGINT ZEROFILL NOT NULL AUTO_INCREMENT,
    publicId VARCHAR(12) NULL,
    unit VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `unit_public_id_idx` (`publicId`)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS IngredientQuantity (
    id BIGINT ZEROFILL NOT NULL AUTO_INCREMENT,
    publicId VARCHAR(12) NULL,
    ingredient BIGINT ZEROFILL NOT NULL,
    quantity DECIMAL(10, 1) NOT NULL,
    unit BIGINT ZEROFILL NOT NULL,
    recipe BIGINT ZEROFILL NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `ingredientquantity_public_id_idx` (`publicId`),
    CONSTRAINT `ingredientquantity_fk_ingredient` FOREIGN KEY (ingredient) REFERENCES Ingredient (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT `ingredientquantity_fk_unit` FOREIGN KEY (unit) REFERENCES Unit (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT `ingredientquantity_fk_recipe` FOREIGN KEY (recipe) REFERENCES Recipe (id) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS RecipeInstruction (
    id BIGINT ZEROFILL NOT NULL AUTO_INCREMENT,
    publicId VARCHAR(12) NULL,
    description TEXT NOT NULL,
    recipe BIGINT ZEROFILL NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `recipeinstruction_public_id_idx` (`publicId`),
    CONSTRAINT `recipeinstruction_fk_recipe` FOREIGN KEY (recipe) REFERENCES Recipe (id) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB;

-- Create a user for the atosapp using a password hash
-- The hash was retrieved by using `SELECT PASSWORD('yourpassword')` on a mariadb shell
CREATE USER IF NOT EXISTS recipeapp IDENTIFIED BY PASSWORD '*70DE2E6669C2D19872A345393C50554F73FE025C';

-- Grant privileges to the app user
GRANT
SELECT
,
INSERT
,
UPDATE
,
    DELETE ON TABLE Recipe TO recipeapp;

GRANT
SELECT
,
INSERT
,
UPDATE
,
    DELETE ON TABLE Ingredient TO recipeapp;

GRANT
SELECT
,
INSERT
,
UPDATE
,
    DELETE ON TABLE Unit TO recipeapp;

GRANT
SELECT
,
INSERT
,
UPDATE
,
    DELETE ON TABLE IngredientQuantity TO recipeapp;

GRANT
SELECT
,
INSERT
,
UPDATE
,
    DELETE ON TABLE RecipeInstruction TO recipeapp;

-- Insert ingredient data
INSERT
    IGNORE INTO Ingredient (id, publicId, name)
VALUES
    (1, "14s97q52s9z3", "blue cheese"),
    (2, "xk2i06ae4g3t", "sugar"),
    (3, "0615bca579x9", "pheasants"),
    (4, "vw0dbiropxp5", "cream cheese"),
    (5, "ec1g7isi399f", "trout"),
    (6, "nnff6llqge70", "celery seeds"),
    (7, "karrf0m8j05f", "melons"),
    (9, "p0nfl33qbefm", "white chocolate"),
    (10, "3055dx9gcdt0", "summer squash"),
    (11, "k3lynpwzghhj", "water chestnuts");

INSERT
    IGNORE INTO Unit (id, publicId, unit)
VALUES
    (1, "dij28n93yeuw", "ml"),
    (2, "ed2x1rlkzvm8", "l"),
    (3, "jfbdz2dyhbjk", "dl"),
    (4, "i87i9cc064gz", "tsp"),
    (5, "ha46aznxc8tz", "tbsp"),
    (6, "rsk9ey2uh409", "oz"),
    (7, "zsdt81pytby7", "pt"),
    (8, "zqmo8nzf72w5", "qt"),
    (9, "3pn3hi9n94jk", "gal"),
    (10, "8sybq33lr3kj", "mg"),
    (11, "io0r94ot3jz6", "g"),
    (12, "ym9y3y2i6i1m", "kg"),
    (13, "yvyh8n8uu0ds", "lb");