-- ============================================================
--  ChazApp - Datos Iniciales (Seed)
--  3 chazas de la Universidad Nacional de Colombia
-- ============================================================

PRAGMA foreign_keys = ON;

-- ============================================================
-- CHAZAS
-- ============================================================
INSERT INTO chazas (nombre, descripcion, ubicacion, activa) VALUES
    ('Burritos UNAL',   'Burritos mexicanos preparados al momento con ingredientes frescos, opciones picantes o no picantes.', 'Parqueadero Antiguo Edificio Enfermeria', 1),
    ('BOWLS UNAL',      'Bowls saludables y nutritivos, ideales para una alimentación balanceada en la UNAL.', 'Parqueadero Antiguo Edificio Enfermeria', 1),
    ('Salchipapas UNAL','Salchipapa en todas sus versiones, lista para satisfacer el antojo.', 'Parqueadero Antiguo Edificio Enfermeria', 1);


-- ============================================================
-- CATEGORÍAS
-- (cada chaza tiene sus propias categorías ordenadas)
-- ============================================================

-- Burritos UNAL (id=1)
INSERT INTO categorias (chaza_id, nombre, orden) VALUES
    (1, 'Burritos',  1),
    (1, 'Bebidas',   2);

-- BOWLS UNAL (id=2)
INSERT INTO categorias (chaza_id, nombre, orden) VALUES
    (2, 'Bowls',     1),
    (2, 'Bebidas',   2);

-- Salchipapas UNAL (id=3)
INSERT INTO categorias (chaza_id, nombre, orden) VALUES
    (3, 'Salchipapas', 1);


-- ============================================================
-- PRODUCTOS
-- ============================================================

-- ─────────────────────────────────────────────────────────────
-- Burritos UNAL  |  categoria_id 1 = Burritos, 2 = Bebidas
-- ─────────────────────────────────────────────────────────────
INSERT INTO productos (chaza_id, categoria_id, nombre, descripcion, precio, disponible) VALUES
    (1, 1, 'Burrito de Lomo',
        'Tortilla de harina rellena con lomo fino salteado, papa ripio, pico de gallo, frijoles negros, arroz.',
        11000, 1),

    (1, 1, 'Burrito de Carne',
        'Tortilla de harina con carne molida a la mexicana, papa ripio, pico de gallo, frijoles negros, arroz.',
        11000, 1),

    (1, 1, 'Burrito Mixto',
        'Tortilla de harina con mezcla de lomo y carne molida, papa ripio, pico de gallo, frijoles negros, arroz.',
        11000, 1),

    (1, 1, 'Burrito de Chorizo',
        'Tortilla de harina con un chorizo santarosano, papa ripio, pico de gallo, frijoles negros, arroz.',
        11000, 1),

    (1, 1, 'Burrito de Hamburguesa',
        'Tortilla de harina con carne de hamburguesa, papa ripio, pico de gallo, frijoles negros, arroz.',
        11000, 1),

    (1, 2, 'Bebida',
        'Variedad de jugos naturales, gaseosas y aguas. Consulta la disponibilidad del día en caja.',
        4000, 1);


-- ─────────────────────────────────────────────────────────────
-- BOWLS UNAL  |  categoria_id 3 = Bowls, 4 = Bebidas
-- ─────────────────────────────────────────────────────────────
INSERT INTO productos (chaza_id, categoria_id, nombre, descripcion, precio, disponible) VALUES
    (2, 3, 'Bowl de Carne',
        'Base de arroz integral con carne a la plancha, vegetales al vapor, aguacate y aderezo de la casa.',
        11000, 1),

    (2, 3, 'Bowl de Pollo',
        'Base de arroz con pollo a la parrilla marinado en especias, maíz, tomate cherry y vinagreta.',
        11000, 1),

    (2, 3, 'Bowl Vegetariano',
        'Base de quinua con garbanzos salteados, espinaca, zanahoria rallada, pepino y tahini.',
        11000, 1),

    (2, 3, 'Bowl Criollo',
        'Base de arroz blanco con bandeja criolla: fríjoles, chicharrón, tajadas y hogao casero.',
        11000, 1),

    (2, 4, 'Bebida',
        'Jugos de fruta natural del día, aguas aromáticas o gaseosas. Preguunta las opciones en caja.',
        4000, 1);


-- ─────────────────────────────────────────────────────────────
-- Salchipapas UNAL  |  categoria_id 5 = Salchipapas
-- ─────────────────────────────────────────────────────────────
INSERT INTO productos (chaza_id, categoria_id, nombre, descripcion, precio, disponible) VALUES
    (3, 5, 'Salchipapa Corriente',
        'Papas fritas crujientes con rodajas de salchicha americana, queso americano fundido, salsas de la casa',
        12000, 1),

    (3, 5, 'Salchipapa Especial',
        'Papas fritas con salchicha americana, pollo o carne desmechada, tocineta crocante, queso americano fundido, , mayonesa y mostaza.',
        14000, 1),

    (3, 5, 'Salchipapa Súper Especial',
        'Papas fritas con salchicha americana, queso americano fundido, pollo y carne desmechada, tocineta crocante, salsas de la casa',
        17000, 1),

    (3, 5, 'Salchipapa Ranchera',
        'Papas fritas con salchicha ranchera zenu, queso americano fundido, pollo y carne desmechada, tocineta crocante, maiz tierno, champiñones salteados y salsas a elección.',
        17000, 1);
