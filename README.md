# ChazApp 🌯

**Plataforma para gestionar pedidos de comida en las chazas de la Universidad Nacional de Colombia.**

Proyecto desarrollado para el curso **Ingeniería de Software I — Universidad Nacional de Colombia (UNAL)**.

ChazApp busca facilitar la consulta de menús y la gestión de pedidos en las chazas universitarias, permitiendo a los estudiantes realizar pedidos y a los vendedores administrarlos.

---

## 📌 Estado del proyecto

Actualmente, el repositorio contiene dos versiones:

- **Aplicación web (en desarrollo):** nueva versión construida con Spring Boot, Java y Thymeleaf.
- **Prototipo de escritorio:** versión anterior desarrollada en Java Swing, conservada en `oldFiles/` como referencia del desarrollo inicial.

---

## 🚀 Funcionalidades

La aplicación está planteada para permitir:

- Consultar las chazas disponibles en el campus.
- Visualizar los menús y productos disponibles.
- Consultar la disponibilidad y los precios de los productos.
- Realizar pedidos con cantidades específicas.
- Generar un PIN para identificar y reclamar los pedidos.
- Gestionar los pedidos desde el perfil del vendedor.
- Actualizar el estado de los pedidos.

*Estas funcionalidades se irán implementando progresivamente en la nueva aplicación web.*

---

## 📁 Estructura del proyecto

```text
ChazApp/
├── .devcontainer/          # Configuración de GitHub Codespaces
│
├── oldFiles/               # Prototipo anterior de escritorio
│
├── Spring/                 # Nueva aplicación web
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/        # Código fuente Java
│   │   │   └── resources/
│   │   │       ├── static/  # CSS, JavaScript e imágenes
│   │   │       └── templates/ # Plantillas HTML con Thymeleaf
│   │   └── test/            # Pruebas automatizadas
│   ├── pom.xml              # Dependencias y configuración Maven
│   ├── mvnw                 # Maven Wrapper para Linux / macOS
│   └── mvnw.cmd             # Maven Wrapper para Windows
│
├── .gitignore
└── README.md
```

---

## 🛠️ Tecnologías

### Aplicación web (Spring Boot)

| Tecnología | Uso |
|---|---|
| Java 21 | Lenguaje de programación |
| Spring Boot | Framework principal |
| Maven | Gestión de dependencias y construcción |
| Spring MVC | Manejo de solicitudes web |
| Thymeleaf | Renderizado de vistas HTML |
| Spring Data JPA | Persistencia y acceso a datos |
| Hibernate | Mapeo objeto-relacional |
| H2 | Base de datos durante el desarrollo |



## 💻 Requisitos

Para ejecutar la aplicación web:

- Java JDK 21 o superior.
- Git (para clonar el repositorio).

---

## ▶️ Cómo ejecutar la aplicación web

La versión actual en desarrollo se encuentra dentro de la carpeta `Spring/`.

### Windows 🪟

```powershell
# 1. Clonar el repositorio
git clone https://github.com/Danmunozbe/ChazApp.git

# 2. Entrar al proyecto Spring
cd ChazApp\Spring

# 3. Ejecutar la aplicación
.\mvnw.cmd spring-boot:run
```

### Linux / macOS 🐧🍎

```bash
# 1. Clonar el repositorio
git clone https://github.com/Danmunozbe/ChazApp.git

# 2. Entrar al proyecto Spring
cd ChazApp/Spring

# 3. Dar permisos de ejecución al Maven Wrapper
chmod +x mvnw

# 4. Ejecutar la aplicación
./mvnw spring-boot:run
```

Una vez iniciada, acceder desde el navegador a:

**http://localhost:8080/**

> La aplicación web se encuentra en desarrollo. Algunas rutas y funcionalidades todavía están pendientes de implementación.

### Consola de base de datos H2

Durante el desarrollo se utiliza H2 como base de datos.

La consola está disponible en:

**http://localhost:8080/h2-console**

Los datos de conexión se encuentran en `Spring/src/main/resources/application.properties` y, si se utiliza una base de datos en memoria, los datos se reinician al detener la aplicación.

---



## Equipo de desarrollo

| Usuario | Responsabilidad |
|---|---|
| Danmunozbe | Backend / Base de datos |
| santiagomoralesmanrique | Frontend |
| diego251307 | Frontend |
| LeonardoDelgado1905 | Base de datos |

---

**Ingeniería de Software I — Universidad Nacional de Colombia**