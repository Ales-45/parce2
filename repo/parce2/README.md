Usuarios y contraseña

admin : admin123
medico: medico123
paciente: paciente123


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