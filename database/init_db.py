"""
ChazApp - Inicializador de base de datos SQLite
    python database/init_db.py          # crea/reinicia la DB
    python database/init_db.py --reset  # borra y recrea todo
"""

import sqlite3
import argparse
from pathlib import Path

DB_PATH     = Path(__file__).parent / "chazapp.db"
SCHEMA_PATH = Path(__file__).parent / "schema.sql"
SEED_PATH   = Path(__file__).parent / "seed.sql"


def init_db(reset: bool = False) -> None:
    if reset and DB_PATH.exists():
        DB_PATH.unlink()
        print(f"[OK] Base de datos eliminada: {DB_PATH}")

    conn = sqlite3.connect(DB_PATH)
    conn.execute("PRAGMA foreign_keys = ON")

    with open(SCHEMA_PATH, encoding="utf-8") as f:
        conn.executescript(f.read())
    print("[OK] Esquema aplicado.")

    with open(SEED_PATH, encoding="utf-8") as f:
        conn.executescript(f.read())
    print("[OK] Datos iniciales cargados.")

    conn.commit()
    conn.close()
    print(f"[LISTO] Base de datos lista en: {DB_PATH.resolve()}")


def show_summary(conn: sqlite3.Connection) -> None:
    """resumen de los datos"""
    cur = conn.cursor()
    print("\n-- Resumen -------------------------------------------")
    for chaza in cur.execute("SELECT id, nombre FROM chazas").fetchall():
        chaza_id, nombre = chaza
        total = cur.execute(
            "SELECT COUNT(*) FROM productos WHERE chaza_id = ?", (chaza_id,)
        ).fetchone()[0]
        print(f"  - {nombre}: {total} producto(s)")
    print("------------------------------------------------------\n")


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Inicializa la DB de ChazApp")
    parser.add_argument("--reset", action="store_true",
                        help="Elimina y recrea la base de datos")
    args = parser.parse_args()

    init_db(reset=args.reset)

    conn = sqlite3.connect(DB_PATH)
    show_summary(conn)
    conn.close()
