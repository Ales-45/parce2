Se debe realizar una aplicación web que permita la gestión de consultas médicas, con los roles de 
usuario: ADMINISTRADOR, MÉDICO y PACIENTE.
➢ Requerimientos de Usuario
ADMINISTRADOR: Este usuario tendrá permisos para Crear, editar, eliminar médicos y asignar
consultorios. Para registrar consultorio, se contará con un formulario donde se solicitarán los 
siguientes datos:
• Nombre del paciente: Campo alfanumérico que no debe superar los 40 caracteres.
• Motivo de la consulta: Campo textual. No debe superar los 100 caracteres.
• Número de consultorio: Campo numérico.
• Horario: Hora de inicio y hora de finalización.
• Médico: Campo de selección desplegable, cuya información se cargará desde la base de 
datos (tabla independiente).
MÉDICO: Este usuario solo tendrá permisos para visualizar la información registrada de las consultas 
asignadas a él (sin ver las consultas de otros médicos), sin posibilidad de realizar ninguna acción.
PACIENTE: Este usuario podrá visualizar la información y realizar actualizaciones de los horarios de 
las citas que SOLO tenga asignada.
➢ Requerimientos de Autenticación y Autorización
La aplicación deberá contar con un formulario de inicio de sesión personalizado, en el que el usuario 
ingresará sus credenciales. Según el rol asignado, se mostrarán las vistas correspondientes al perfil 
del usuario. Si el usuario no cuenta con los permisos requeridos para realizar alguna acción, se 
mostrará una vista informativa personalizada indicando la restricción.
➢ IMPLEMENTACIÓN

La aplicación se ejecuta con Spring Boot, Spring Security, JPA y una base H2 local.

Usuarios de demostración:

| Usuario | Contraseña | Rol |
|---|---|---|
| admin | admin123 | ADMINISTRADOR |
| medico | medico123 | MEDICO |
| paciente | paciente123 | PACIENTE |

Rutas:

- `http://localhost:8081/login`: formulario de inicio de sesión.
- `http://localhost:8081/dashboard`: panel autenticado.
- `http://localhost:8081/swagger-ui.html`: documentación y pruebas de los servicios.
- `http://localhost:8081/h2-console`: consola H2 (`jdbc:h2:file:./data/parce2`, usuario `sa`, contraseña vacía).

Servicios:

- `GET/POST/PUT/DELETE /api/medicos`: solo ADMINISTRADOR.
- `GET /api/consultas`: ADMINISTRADOR ve todas; MÉDICO ve solo las asignadas; PACIENTE ve solo las propias.
- `POST /api/consultas`: solo ADMINISTRADOR; asigna médico y paciente.
- `PATCH /api/consultas/{id}/horario`: solo PACIENTE y únicamente para su propia cita.

Cada endpoint documentado incluye respuestas `401` y `403`, además de su respuesta exitosa.

Ejecución:

```bash
sh ../spring_jwt/mvnw spring-boot:run
```

La aplicación usa el puerto `8081` para no interferir con el Tomcat existente en `8080`.