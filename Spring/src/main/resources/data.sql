-- ============================================================
-- ChazApp - Datos Iniciales (Seed Data)
-- Universidad Nacional de Colombia
-- ============================================================

-- ------------------------------------------------------------
-- Chazas
-- ------------------------------------------------------------
-- Chaza 1: Burritos UNAL (Menú publicado)
INSERT INTO chazas (id, nombre, descripcion, ubicacion, imagen_url, menu_publicado, activa) VALUES
(1, 'Burritos UNAL', 'Burritos mexicanos preparados al momento con ingredientes frescos, salsas de la casa y opciones personalizadas.', 'Parqueadero Antiguo Edificio Enfermería', '/img/burritos_banner.jpg', TRUE, TRUE);

-- Chaza 2: BOWLS UNAL (Menú publicado)
INSERT INTO chazas (id, nombre, descripcion, ubicacion, imagen_url, menu_publicado, activa) VALUES
(2, 'BOWLS UNAL', 'Bowls saludables y nutritivos, ideales para una alimentación balanceada y energética en el campus universitario.', 'Plaza Che - Frente a Sociología', '/img/bowls_banner.png', TRUE, TRUE);

-- Chaza 3: Salchipapas UNAL (Menú NO publicado - para probar caso de excepción CU-05)
INSERT INTO chazas (id, nombre, descripcion, ubicacion, imagen_url, menu_publicado, activa) VALUES
(3, 'Salchipapas UNAL', 'Salchipapas tradicionales y especiales con ingredientes de primera calidad y salsas artesanales.', 'Entrada Calle 26 - Canchas Deportivas', '/img/salchipapas_banner.png', FALSE, TRUE);


-- ------------------------------------------------------------
-- Productos
-- ------------------------------------------------------------

-- Productos Chaza 1: Burritos UNAL (7 productos, al menos 1 agotado)
INSERT INTO productos (id, chaza_id, nombre, descripcion, categoria, precio, imagen_url, disponible) VALUES
(1, 1, 'Burrito de Lomo', 'Tortilla de harina rellena con lomo fino salteado, papa ripio, pico de gallo, frijoles negros y arroz.', 'Burritos', 11000.0, '/img/OtrosBurritos.jpeg', TRUE),
(2, 1, 'Burrito de Carne', 'Tortilla de harina con carne molida a la mexicana, papa ripio, pico de gallo, frijoles negros y arroz.', 'Burritos', 11000.0, '/img/OtrosBurritos.jpeg', TRUE),
(3, 1, 'Burrito Mixto', 'Tortilla de harina con mezcla de lomo y carne molida, papa ripio, pico de gallo y frijoles negros.', 'Burritos', 12000.0, '/img/OtrosBurritos.jpeg', TRUE),
(4, 1, 'Burrito de Chorizo', 'Tortilla de harina con chorizo santarosano artesanal, queso mozzarella, papa ripio y frijoles.', 'Burritos', 11000.0, NULL, FALSE),
(5, 1, 'Burrito de Hamburguesa', 'Tortilla de harina con jugosa carne de hamburguesa, queso mozzarella, tocineta y salsas.', 'Burritos', 11000.0, NULL, TRUE),
(6, 1, 'Mazorcada Especial', 'Maíz tierno desgranado con pollo desmechado, tocineta crocante, queso gratinado y papas fósforo.', 'Especiales', 14000.0, '/img/Mazorcada.jpeg', TRUE),
(7, 1, 'Jugo Natural del Día', 'Variedad de jugos naturales preparados en agua o leche (Mora, Mango o Lulo).', 'Bebidas', 4000.0, NULL, TRUE);

-- Productos Chaza 2: BOWLS UNAL (6 productos, al menos 1 agotado)
INSERT INTO productos (id, chaza_id, nombre, descripcion, categoria, precio, imagen_url, disponible) VALUES
(8, 2, 'Bowl Criollo', 'Base de arroz blanco con fríjoles campesinos, carne molida, chicharrón crocante, tajadas y hogao casero.', 'Bowls', 12000.0, '/img/Arroz, frijoles y carne.jpeg', TRUE),
(9, 2, 'Bowl de Pollo Parrilla', 'Base de arroz integral con pechuga marinada en finas hierbas, maíz dulce, tomate cherry y vinagreta.', 'Bowls', 11000.0, NULL, TRUE),
(10, 2, 'Bowl de Carne Salteada', 'Base de arroz con lomo de res salteado con pimientos, cebolla morada y aderezo especial de la casa.', 'Bowls', 12000.0, NULL, TRUE),
(11, 2, 'Bowl Vegetariano', 'Base de quinua con garbanzos crocantes, espinaca fresca, aguacate, zanahoria y aderezo tahini.', 'Bowls', 11000.0, NULL, FALSE),
(12, 2, 'Bowl Mixto Proteico', 'Generosa porción de pechuga de pollo y lomo de res a la plancha con vegetales frescos y puré.', 'Bowls', 13500.0, NULL, TRUE),
(13, 2, 'Limonada Natural / Cerezada', 'Bebida refrescante recién preparada con limones seleccionados y hielo granizado.', 'Bebidas', 4000.0, NULL, TRUE);

-- Productos Chaza 3: Salchipapas UNAL (4 productos)
INSERT INTO productos (id, chaza_id, nombre, descripcion, categoria, precio, imagen_url, disponible) VALUES
(14, 3, 'Salchipapa Corriente', 'Papas a la francesa crujientes con rodajas de salchicha seleccionada, queso fundido y salsas.', 'Salchipapas', 12000.0, NULL, TRUE),
(15, 3, 'Salchipapa Especial', 'Papas crujientes con salchicha manguera, pollo desmechado, tocineta crocante y queso.', 'Salchipapas', 15000.0, NULL, TRUE),
(16, 3, 'Salchipapa Súper Mixta', 'Papas con salchicha, pollo y carne desmechada, tocineta, maíz dulce y huevo de codorniz.', 'Salchipapas', 18000.0, NULL, TRUE),
(17, 3, 'Gaseosa Postobón 400ml', 'Variedad de sabores fríos (Manzana, Colombiana, Pepsi) para acompañar tu orden.', 'Bebidas', 3500.0, NULL, TRUE);
