# ChazApp 🌯
**Aplicación de escritorio para pedir en las chazas de la Universidad Nacional de Colombia.**

Proyecto del curso Ingeniería de Software I — UNAL.

---

## ¿Qué hace?

ChazApp permite a los estudiantes:
- Consultar las chazas disponibles en el campus
- Ver el menú de cada chaza organizado por categorías
- Verificar disponibilidad de productos
- Armar un pedido con carrito de compras

---

## Estructura del proyecto

```
ChazApp/
├── src/              → Código fuente Java
├── database/         → Base de datos SQLite + scripts de inicialización
├── img/              → Imágenes y banners de las chazas
├── sqlite-jdbc.jar   → Driver JDBC para SQLite (incluido)
├── run.bat           → Ejecutar en Windows
└── run.sh            → Ejecutar en Linux / macOS / Codespace
```

---

## Requisitos

| Herramienta | Versión mínima |
|-------------|---------------|
| Java JDK    | 11 o superior |
| Python      | 3.8 o superior |

> **En GitHub Codespaces** no necesitas instalar nada — el entorno se configura automáticamente al abrir el repositorio.

---

## Cómo ejecutar

### En GitHub Codespace ☁️ *(presentación del profesor)*

1. Abrir el repositorio en Codespaces (botón verde **Code → Codespaces → Create**)
2. Esperar a que el contenedor termine de configurarse (~1 min)
3. Cuando el escritorio virtual aparezca en el navegador (puerto 6080), abrir una terminal dentro de él y ejecutar:

```bash
./run.sh
```

4. La ventana de ChazApp aparecerá en el escritorio virtual del navegador.

> La contraseña del escritorio virtual es: `chazapp`

---

### En Linux / macOS 🐧🍎

```bash
# 1. Clonar el repositorio
git clone https://github.com/Danmunozbe/ChazApp.git
cd ChazApp

# 2. Inicializar la base de datos (solo la primera vez)
python3 database/init_db.py

# 3. Dar permisos y ejecutar
chmod +x run.sh
./run.sh
```

---

### En Windows 🪟

```bat
:: 1. Clonar el repositorio
git clone https://github.com/Danmunozbe/ChazApp.git
cd ChazApp

:: 2. Inicializar la base de datos (solo la primera vez)
python database\init_db.py

:: 3. Ejecutar
run.bat
```

---

## Base de datos

La base de datos se genera localmente y **no se sube al repositorio** (ver `.gitignore`).

Para reinicializarla desde cero:
```bash
python3 database/init_db.py --reset
```

---

## Equipo

| Usuario | Rol |
|---------|-----|
| Danmunozbe | Backend / Base de datos |
| santiagomoralesmanrique | Frontend |
| diego251307 | Frontend |
| LeonardoDelgado1905 | Base de datos |
