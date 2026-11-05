-- ============================================================
--  ChazApp - Esquema de Base de Datos SQLite
--  Universidad Nacional de Colombia
-- ============================================================

PRAGMA foreign_keys = ON;

-- ------------------------------------------------------------
-- Tabla: chazas
-- Representa cada puesto de comida en la universidad
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS chazas (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre      TEXT    NOT NULL,
    descripcion TEXT,
    ubicacion   TEXT,
    imagen_url  TEXT,
    activa      INTEGER NOT NULL DEFAULT 1,   -- 1 = abierta, 0 = cerrada
    creado_en   TEXT    NOT NULL DEFAULT (datetime('now', 'localtime')),
    actualizado_en TEXT NOT NULL DEFAULT (datetime('now', 'localtime'))
);

-- ------------------------------------------------------------
-- Tabla: categorias
-- Categorías de producto dentro de cada chaza
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS categorias (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    chaza_id    INTEGER NOT NULL REFERENCES chazas(id) ON DELETE CASCADE,
    nombre      TEXT    NOT NULL,
    orden       INTEGER NOT NULL DEFAULT 0    -- para ordenar las categorías en la UI
);

-- ------------------------------------------------------------
-- Tabla: productos
-- Cada ítem del menú asociado a una chaza y categoría
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS productos (
    id              INTEGER PRIMARY KEY AUTOINCREMENT,
    chaza_id        INTEGER NOT NULL REFERENCES chazas(id) ON DELETE CASCADE,
    categoria_id    INTEGER          REFERENCES categorias(id) ON DELETE SET NULL,
    nombre          TEXT    NOT NULL,
    descripcion     TEXT,                      -- descripción editable fácilmente
    precio          REAL    NOT NULL DEFAULT 0,
    imagen_url      TEXT,
    disponible      INTEGER NOT NULL DEFAULT 1, -- 1 = disponible, 0 = agotado
    creado_en       TEXT    NOT NULL DEFAULT (datetime('now', 'localtime')),
    actualizado_en  TEXT    NOT NULL DEFAULT (datetime('now', 'localtime'))
);

-- ------------------------------------------------------------
-- Trigger: actualiza "actualizado_en" en chazas
-- ------------------------------------------------------------
CREATE TRIGGER IF NOT EXISTS trg_chazas_updated
AFTER UPDATE ON chazas
FOR EACH ROW
BEGIN
    UPDATE chazas SET actualizado_en = datetime('now', 'localtime')
    WHERE id = OLD.id;
END;

-- ------------------------------------------------------------
-- Trigger: actualiza "actualizado_en" en productos
-- ------------------------------------------------------------
CREATE TRIGGER IF NOT EXISTS trg_productos_updated
AFTER UPDATE ON productos
FOR EACH ROW
BEGIN
    UPDATE productos SET actualizado_en = datetime('now', 'localtime')
    WHERE id = OLD.id;
END;

-- ------------------------------------------------------------
-- Vista: v_productos_disponibles
-- Facilita consultar sólo los productos disponibles con info
-- de su chaza y categoría
-- ------------------------------------------------------------
CREATE VIEW IF NOT EXISTS v_productos_disponibles AS
SELECT
    p.id            AS producto_id,
    c.id            AS chaza_id,
    c.nombre        AS chaza,
    cat.nombre      AS categoria,
    p.nombre        AS producto,
    p.descripcion,
    p.precio,
    p.disponible,
    p.actualizado_en
FROM productos p
JOIN chazas    c   ON c.id  = p.chaza_id
LEFT JOIN categorias cat ON cat.id = p.categoria_id
WHERE c.activa = 1;
