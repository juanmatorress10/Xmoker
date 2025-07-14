# Xmoker - Aplicación de Apoyo para Dejar de Fumar

Xmoker es una aplicación integral diseñada para ofrecer apoyo y herramientas a las personas que desean dejar de fumar. Combina seguimiento personal, gamificación, una comunidad de apoyo y consejos diarios para motivar a los usuarios en su proceso.

---

## ✨ Características Principales

El proyecto está estructurado en varios módulos que ofrecen un conjunto completo de funcionalidades:

* **Autenticación y Seguridad**: Sistema de registro e inicio de sesión seguro para usuarios utilizando JSON Web Tokens (JWT).
* **Gestión de Usuarios**: Perfiles de usuario personalizables, con roles para usuarios, profesionales y administradores.
* **Seguimiento del Progreso (`Progreso` y `Métricas`)**:
    * Cálculo de métricas clave como el tiempo sin fumar, el dinero ahorrado y los cigarrillos no consumidos.
    * Registro de recaídas para un seguimiento honesto del proceso.
* **Comunidad de Apoyo (`Comunidad`)**:
    * Creación y gestión de **Grupos de Apoyo**.
    * Publicaciones, comentarios y reacciones dentro de los grupos para fomentar la interacción.
    * **Retos grupales** para motivar a los usuarios a alcanzar metas comunes.
* **Gamificación (`Logros` y `Niveles`)**:
    * Sistema de **niveles** basado en la experiencia (XP) ganada por el tiempo sin fumar.
    * Desbloqueo de **logros** por alcanzar hitos importantes (e.g., 24 horas sin fumar, 100€ ahorrados).
* **Contenido Motivacional**:
    * **Consejos diarios** para mantener la motivación.
    * Un **diario personal** para que los usuarios registren sus pensamientos y sentimientos.
* **Cuestionarios**: Evaluación inicial y de seguimiento para personalizar la experiencia del usuario.

---

## 🛠️ Stack Tecnológico

El backend del proyecto está construido con tecnologías modernas, robustas y escalables:

* **Lenguaje**: Java 17
* **Framework**: Spring Boot 3
* **Base de Datos**: PostgreSQL
* **Autenticación**: Spring Security y JWT
* **ORM**: Spring Data JPA (Hibernate)
* **Contenerización**: Docker y Docker Compose

---

## 🚀 Cómo Poner en Marcha el Proyecto

Gracias a Docker, el entorno de desarrollo se puede levantar con un único comando.

### Prerrequisitos

* Tener instalado [Docker](https://www.docker.com/get-started) en tu sistema.

### Pasos para la Ejecución

1.  **Clona el repositorio** (si aún no lo has hecho):
    ```bash
    git clone [https://github.com/juanmatorress10/xmoker.git](https://github.com/juanmatorress10/xmoker.git)
    cd Xmoker-main
    ```

2.  **Construye la imagen de la aplicación**:
    Dentro del directorio `xmoker-backend/xmoker-backend`, ejecuta el siguiente comando de Maven para generar el archivo JAR.
    ```bash
    ./mvnw clean package
    ```
    *Nota: Si no tienes Maven instalado, el Maven Wrapper (`mvnw`) lo gestionará por ti.*

3.  **Levanta los servicios con Docker Compose**:
    Desde el directorio raíz del proyecto (`Xmoker-main`), ejecuta:
    ```bash
    docker-compose up --build
    ```
    Este comando hará lo siguiente:
    * Construirá la imagen de Docker para la aplicación de Spring Boot (`xmoker-backend`).
    * Levantará un contenedor para la base de datos PostgreSQL.
    * Iniciará la aplicación y la conectará con la base de datos.

La aplicación estará disponible en `http://localhost:8080`.

---

## 📂 Estructura del Proyecto

El backend sigue una arquitectura modular y organizada para facilitar su mantenimiento y escalabilidad:
xmoker-backend/
└── src/main/java/com/xmoker/
├── auth/         # Lógica de autenticación, JWT y seguridad.
├── common/       # Clases comunes (ej. GlobalExceptionHandler).
├── comunidad/    # Módulo de grupos, posts, retos y comunidad.
├── config/       # Configuración de Spring (Seguridad, Web, etc.).
├── consejo/      # Módulo para los consejos diarios.
├── diario/       # Módulo para el diario personal.
├── logro/        # Módulo de logros y sistema de gamificación.
└── user/         # Módulo de gestión de usuarios, progreso y métricas.


---

## 📝 Gestión de Archivos con .gitignore

El archivo `.gitignore` es fundamental para evitar que archivos innecesarios o sensibles (como fotos, archivos de configuración locales, o artefactos de compilación) sean rastreados por Git y subidos al repositorio.

Para excluir archivos, simplemente añade los patrones correspondientes en tu archivo `.gitignore`. Por ejemplo, para ignorar imágenes:

```gitignore
# Ignorar archivos de imagen
*.jpg
*.jpeg
*.png
*.gif
*.svg
*.bmp

# O ignorar una carpeta completa
/uploads/images/
