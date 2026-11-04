#!/bin/bash
# ── ChazApp — Script de compilación y ejecución (Linux / macOS / Codespace) ──

set -e

echo "──────────────────────────────────────────"
echo "  ChazApp — UNAL"
echo "──────────────────────────────────────────"

# 1. Inicializar base de datos si no existe
if [ ! -f "database/chazapp.db" ]; then
    echo "[1/3] Inicializando base de datos..."
    python3 database/init_db.py
else
    echo "[1/3] Base de datos ya existe, omitiendo init."
fi

# 2. Compilar
echo "[2/3] Compilando..."
mkdir -p bin
javac -encoding UTF-8 -cp "sqlite-jdbc.jar" -d bin src/*.java
echo "      Compilación exitosa."

# 3. Ejecutar
echo "[3/3] Iniciando ChazApp..."
echo "──────────────────────────────────────────"
java -cp "bin:sqlite-jdbc.jar" ChazApp
