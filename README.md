# ChazApp 🌯

**Plataforma para gestionar pedidos de comida en las chazas de la Universidad Nacional de Colombia.**

Proyecto desarrollado para el curso **Ingeniería de Software I — Universidad Nacional de Colombia (UNAL)**.

ChazApp busca facilitar la consulta de menús y la gestión de pedidos en las chazas universitarias, permitiendo a los estudiantes realizar pedidos y a los vendedores administrarlos.

---

## 📌 Estado del proyecto — Primer Incremento Funcional

Se ha completado la implementación del **Primer Incremento Funcional** enfocado en los casos de uso esenciales:

- **CU-05 — Consultar menú y disponibilidad por chaza (Rol Estudiante):**
  - Exploración de las chazas disponibles en el campus universitario con información de ubicación y banners institucionales.
  - Consulta del menú detallado de cada chaza con precios, imágenes y badges claros de estado en tiempo real (**Disponible** o **Agotado**).
  - Manejo de excepción cuando una chaza no tiene su menú publicado (`menu_publicado = false`), mostrando un mensaje explicativo al estudiante: *"No hay un menú disponible para esta chaza"*.
- **CU-04 — Actualizar disponibilidad de productos (Rol Vendedor):**
  - Panel de administración exclusivo por puesto de chaza (`/vendedor/chaza/{chazaId}`).
  - Botón de alternancia rápida (*toggle*) de disponibilidad por producto (`POST /vendedor/producto/{productoId}/toggle`) que actualiza la base de datos y se refleja inmediatamente en la vista del estudiante.

---

## 🚀 Rutas y Vistas de la Aplicación

| Ruta | Rol | Descripción |
|---|---|---|
| `/` | Público | Pantalla de bienvenida con selector rápido de rol (Estudiante o Vendedor por chaza) |
| `/estudiante/chazas` | Estudiante | Catálogo de puestos de chazas activas en la UNAL |
| `/estudiante/chazas/{id}` | Estudiante | Menú del día y disponibilidad de productos de la chaza |
| `/vendedor/chaza/{chazaId}` | Vendedor | Panel de gestión y control de stock de la chaza seleccionada |
| `POST /vendedor/producto/{id}/toggle` | Vendedor | Endpoint para alternar disponibilidad (Disponible ↔ Agotado) |
| `/h2-console` | Desarrollador | Consola web interactiva de la base de datos H2 |

---

## 📁 Estructura del proyecto

Arquitectura monolítica por capas dentro de `Spring/src/main/java/com/ChazApp/ChazApp/`:

```text
ChazApp/
├── oldFiles/                       # Prototipo anterior de escritorio (Java Swing)
│
├── Spring/                         # Aplicación web Spring Boot
│   ├── data/                       # Archivo local de base de datos H2 (ignorado en git)
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/ChazApp/ChazApp/
│   │   │   │   ├── controller/     # HomeController, EstudianteController, VendedorController
│   │   │   │   ├── exception/      # ChazaNotFoundException, ProductoNotFoundException, etc.
│   │   │   │   ├── model/          # Entidades JPA (Chaza, Producto)
│   │   │   │   ├── repository/     # Interfaces JpaRepository (ChazaRepository, ProductoRepository)
│   │   │   │   └── service/        # Lógica de negocio (ChazaService, ProductoService)
│   │   │   └── resources/
│   │   │       ├── schema.sql      # Esquema DDL versionado para H2
│   │   │       ├── data.sql        # Datos iniciales (Seed de 3 chazas y 17 productos)
│   │   │       ├── static/img/     # Logos, banners y fotografías de productos
│   │   │       └── templates/      # Vistas renderizadas con Thymeleaf y Bootstrap 5
│   │   └── test/                   # Pruebas unitarias JUnit 5 y Mockito (cobertura 100%)
│   ├── pom.xml                     # Dependencias y configuración Maven
│   ├── mvnw                        # Maven Wrapper para Linux / macOS
│   └── mvnw.cmd                    # Maven Wrapper para Windows
│
├── .gitignore
└── README.md
```

---

## 🛠️ Tecnologías

| Tecnología | Uso |
|---|---|
| Java 21 | Lenguaje de programación |
| Spring Boot | Framework principal (Spring MVC, Spring Data JPA) |
| Maven Wrapper (`./mvnw`) | Gestión de construcción y dependencias sin instalación previa |
| Thymeleaf | Renderizado de vistas HTML en el servidor |
| Bootstrap 5 + Bootstrap Icons | Diseño responsivo e identidad visual institucional |
| H2 Database | Base de datos relacional local en archivo persistente |
| JUnit 5 & Mockito | Suite de pruebas unitarias para la lógica de negocio |
| JaCoCo | Medición de cobertura de código automatizada (100% en servicios) |

---

## 💻 Requisitos

- **Java JDK 21** o superior.
- Git.

---

## ▶️ Cómo ejecutar la aplicación y correr las pruebas

Todas las instrucciones se ejecutan desde la carpeta `Spring/`:

```bash
cd Spring
```

### 1. Ejecutar la aplicación web

#### En Linux / macOS 🐧🍎:
```bash
# Dar permisos de ejecución al wrapper (solo la primera vez)
chmod +x mvnw

# Iniciar la aplicación
./mvnw spring-boot:run
```

#### En Windows 🪟:
```powershell
.\mvnw.cmd spring-boot:run
```

Una vez iniciada, abre tu navegador en:
👉 **http://localhost:8080/**

### 2. Ejecutar las pruebas unitarias y verificar cobertura

#### En Linux / macOS 🐧🍎:
```bash
./mvnw test
```

#### En Windows 🪟:
```powershell
.\mvnw.cmd test
```

Los reportes de cobertura generados por JaCoCo quedan disponibles en:
`Spring/target/site/jacoco/index.html`

### 3. Consola de base de datos H2

Durante el desarrollo se utiliza H2 como base de datos en archivo local (`jdbc:h2:file:./data/chazappdb`).
La consola está disponible en:

👉 **http://localhost:8080/h2-console**

- **JDBC URL:** `jdbc:h2:file:./data/chazappdb`
- **User Name:** `sa`
- **Password:** *(dejar vacío)*

---

## 🤖 Declaración de uso de Inteligencia Artificial

En cumplimiento de las buenas prácticas académicas y de transparencia, se declara que se utilizó la herramienta **Antigravity (Gemini)** como asistente de desarrollo para:
1. Diseñar y estructurar la arquitectura monolítica por capas en Spring Boot (`model`, `repository`, `service`, `controller`).
2. Adaptar el esquema relacional DDL (`schema.sql`) y datos iniciales de prueba (`data.sql`) para H2 Database.
3. Generar las plantillas Thymeleaf con maquetación en Bootstrap 5 e identidad visual de la Universidad Nacional de Colombia.
4. Redactar la suite de pruebas unitarias con JUnit 5 y Mockito para la capa de servicios, alcanzando el 100% de cobertura en la lógica de negocio de los casos de uso CU-04 y CU-05.

---

## 👥 Equipo de desarrollo

| Integrante | Rol / Responsabilidad |
|---|---|
| Danmunozbe | Backend / Base de datos |
| santiagomoralesmanrique | Frontend |
| diego251307 | Frontend |
| LeonardoDelgado1905 | Base de datos |

---

**Ingeniería de Software I — Universidad Nacional de Colombia**
