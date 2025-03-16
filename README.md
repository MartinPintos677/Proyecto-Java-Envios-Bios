# 📦 EnviosBios - Gestión de Envíos de Paquetes

EnviosBios es una plataforma de gestión de envíos de paquetes que permite a los empleados administrar los paquetes enviados por clientes, cambiar estados de rastreo y gestionar información relevante del sistema.

## 🚀 Características principales  

✅ **CRUD de Paquetes** - Los empleados pueden agregar, modificar y eliminar paquetes.  
✅ **Estados de Rastreo** - Se pueden actualizar los estados de los paquetes a medida que avanzan en el proceso de envío.  
✅ **Gestión de Clientes** - Los clientes pueden registrarse y realizar envíos.  
✅ **Administración de Empleados** - Los empleados son registrados y gestionados dentro del sistema.  
✅ **Filtros Avanzados** - Búsqueda de paquetes por cédula del cliente, fecha de registro o estado de rastreo.  
✅ **Paginación Configurable** - Para una mejor visualización de los paquetes.  

---

## 📌 Tecnologías utilizadas  

🔹 **Backend:** Java, Spring Boot, Spring MVC, JPA/Hibernate  
🔹 **Frontend:** Thymeleaf, HTML, CSS  
🔹 **Base de Datos:** MySQL  
🔹 **ORM:** JPA / Hibernate  
🔹 **Otros:** Maven, GitHub  


---

## ⚙️ Instalación y Configuración

### 📥 Clonar el repositorio
```bash
git clone https://github.com/tu-usuario/enviosbios.git
cd enviosbios
```

### 🛠️ Configurar Base de Datos
1. Crear una base de datos MySQL llamada `enviosbios`.
2. Configurar el archivo `application.properties` con tus credenciales de MySQL:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/enviosbios
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
```

### ▶️ Ejecutar el proyecto
```bash
mvn spring-boot:run
```

Acceder a la aplicación en: [http://localhost:8080](http://localhost:8080)

---

¡Gracias por visitar este repositorio! 🎉
