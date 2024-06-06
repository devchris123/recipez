-- Insert ingredient data
INSERT
    IGNORE INTO Ingredient (id, publicId, name)
VALUES
    (1, '14s97q52s9z3', 'blue cheese'),
    (2, 'xk2i06ae4g3t', 'sugar'),
    (3, '0615bca579x9', 'pheasants'),
    (4, 'vw0dbiropxp5', 'cream cheese'),
    (5, 'ec1g7isi399f', 'trout'),
    (6, 'nnff6llqge70', 'celery seeds'),
    (7, 'karrf0m8j05f', 'melons'),
    (9, 'p0nfl33qbefm', 'white chocolate'),
    (10, '3055dx9gcdt0', 'summer squash'),
    (11, 'k3lynpwzghhj', 'water chestnuts');

INSERT
    IGNORE INTO Unit (id, publicId, unit)
VALUES
    (1, 'dij28n93yeuw', 'ml'),
    (2, 'ed2x1rlkzvm8', 'l'),
    (3, 'jfbdz2dyhbjk', 'dl'),
    (4, 'i87i9cc064gz', 'tsp'),
    (5, 'ha46aznxc8tz', 'tbsp'),
    (6, 'rsk9ey2uh409', 'oz'),
    (7, 'zsdt81pytby7', 'pt'),
    (8, 'zqmo8nzf72w5', 'qt'),
    (9, '3pn3hi9n94jk', 'gal'),
    (10, '8sybq33lr3kj', 'mg'),
    (11, 'io0r94ot3jz6', 'g'),
    (12, 'ym9y3y2i6i1m', 'kg'),
    (13, 'yvyh8n8uu0ds', 'lb');