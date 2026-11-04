-- ============================================================
--  ChazApp - Datos Iniciales (Seed)
--  3 chazas de la Universidad Nacional de Colombia
-- ============================================================

PRAGMA foreign_keys = ON;

-- ============================================================
-- CHAZAS
-- ============================================================
INSERT INTO chazas (nombre, descripcion, ubicacion, activa) VALUES
    ('Burritos UNAL',   'Auténticos burritos mexicanos preparados al momento con ingredientes frescos.', 'Edificio de Ingeniería - Pasillo Central', 1),
    ('BOWLS UNAL',      'Bowls saludables y nutritivos, ideales para una alimentación balanceada en el campus.', 'Cafetería Central - Local 3', 1),
    ('Salchipapas UNAL','La clásica salchipapa colombiana en todas sus versiones, lista para satisfacer el antojo.', 'Entrada Principal - Kiosco 2', 1);


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
        'Tortilla de harina rellena con lomo fino salteado, guacamole, frijoles y pico de gallo.',
        12000, 1),

    (1, 1, 'Burrito de Carne',
        'Tortilla de harina con carne molida a la mexicana, queso cheddar, lechuga y crema ácida.',
        11000, 1),

    (1, 1, 'Burrito Mixto',
        'Tortilla de harina con mezcla de lomo y carne molida, pimientos asados, jalapeños y salsa roja.',
        13000, 1),

    (1, 1, 'Burrito de Chorizo',
        'Tortilla de harina con chorizo artesanal, huevo revuelto, queso y salsa verde tatemada.',
        11500, 1),

    (1, 1, 'Burrito de Hamburguesa',
        'Tortilla de harina con carne de hamburguesa, cheddar derretido, pepinillos y salsa secreta de la casa.',
        12500, 1),

    (1, 2, 'Bebida',
        'Variedad de jugos naturales, gaseosas y aguas. Consulta la disponibilidad del día en caja.',
        3000, 1);


-- ─────────────────────────────────────────────────────────────
-- BOWLS UNAL  |  categoria_id 3 = Bowls, 4 = Bebidas
-- ─────────────────────────────────────────────────────────────
INSERT INTO productos (chaza_id, categoria_id, nombre, descripcion, precio, disponible) VALUES
    (2, 3, 'Bowl de Carne',
        'Base de arroz integral con carne a la plancha, vegetales al vapor, aguacate y aderezo de la casa.',
        13000, 1),

    (2, 3, 'Bowl de Pollo',
        'Base de arroz con pollo a la parrilla marinado en especias, maíz, tomate cherry y vinagreta.',
        12500, 1),

    (2, 3, 'Bowl Vegetariano',
        'Base de quinua con garbanzos salteados, espinaca, zanahoria rallada, pepino y tahini.',
        11500, 1),

    (2, 3, 'Bowl Criollo',
        'Base de arroz blanco con bandeja criolla: fríjoles, chicharrón, tajadas y hogao casero.',
        13500, 1),

    (2, 4, 'Bebida',
        'Jugos de fruta natural del día, aguas aromáticas o gaseosas. Preguunta las opciones en caja.',
        3000, 1);


-- ─────────────────────────────────────────────────────────────
-- Salchipapas UNAL  |  categoria_id 5 = Salchipapas
-- ─────────────────────────────────────────────────────────────
INSERT INTO productos (chaza_id, categoria_id, nombre, descripcion, precio, disponible) VALUES
    (3, 5, 'Salchipapa Corriente',
        'Papas fritas crujientes con rodajas de salchicha, kétchup y mostaza.',
        6000, 1),

    (3, 5, 'Salchipapa Especial',
        'Papas fritas con salchicha, queso mozarela fundido, kétchup, mayonesa y mostaza.',
        8000, 1),

    (3, 5, 'Salchipapa Súper Especial',
        'Papas fritas con doble salchicha, queso, tocineta crocante, champiñones salteados y salsas a elección.',
        10000, 1),

    (3, 5, 'Salchipapa Ranchera',
        'Papas fritas con salchicha, hogao, suero costeño, cilantro picado y ají casero.',
        9000, 1);
