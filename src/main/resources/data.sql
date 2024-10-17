CREATE PROCEDURE IF NOT EXISTS cargar_datos_iniciales()
BEGIN
    IF NOT EXISTS (
        SELECT * FROM logs
    ) THEN  

        -- Datos de prueba para la tabla sucursales
        INSERT INTO sucursal (numero, nombre) VALUES
        (1, 'Sucursal 1'),
        (2, 'Sucursal 2');

        -- Datos de prueba para la tabla usuarios
        INSERT INTO usuarios (nombre_usuario, clave_de_acceso, email) VALUES        
        ('cliente1', '$2a$10$e5yXhTjo9.dK/j38hOXyBe5Zh1rB7ay3QhxLk7PI2R7z.qy7olnCG', 'cliente1@gmail.com'),
        ('cliente2', '$2a$10$e5yXhTjo9.dK/j38hOXyBe5Zh1rB7ay3QhxLk7PI2R7z.qy7olnCG', 'cliente2@gmail.com'),
        ('empleado1', '$2a$10$yIOOCjM7R0iPBAmvDJ/GReFNTugPgE0a4LLDzE14l1GPFPqqYuHjC', 'empleado1@enviosbios.com'),
        ('empleado2', '$2a$10$OxiDDmDe3GmrODIylXYwz.FjkekrbwbRtPmXJwV6fuxj5Fbznf2jK', 'empleado2@gmail.com');

        -- Datos de prueba para la tabla clientes (sin claveDeAcceso y email)
        INSERT INTO clientes (nombre_usuario, cedula, domicilio, telefono, activo) VALUES
        ('cliente1', '12345678', 'Calle Falsa 123', '098765432', 1),
        ('cliente2', '87654321', 'Avenida Siempre Viva 742', '091234567', 1);

        -- Datos de prueba para la tabla empleados (sin claveDeAcceso y email)
        INSERT INTO empleados (nombre_usuario, sucursal_numero) VALUES
        ('empleado1', 1),
        ('empleado2', 2);

        --Datos de prueba para la tabla Roles
        INSERT INTO roles(nombre_rol) VALUES
        ('empleado'),
        ('cliente');

        INSERT INTO usuarios_roles(usuario_nombre_usuario, rol_nombre_rol) VALUES
        ('empleado1', 'empleado'),
        ('cliente1', 'cliente');   

        -- Datos de prueba para la tabla categorias
        INSERT INTO categorias (id_cat, nombre) VALUES
        (1, 'Documentos'),
        (2, 'Paquetes Grandes'),
        (3, 'Paquetes Pequeños'),
        (4, 'Paquetes Fragiles Pequeños'),
        (5, 'Paquetes Fragiles Grandes');


        -- Datos de prueba para la tabla estadoRastreo
        INSERT INTO estado_rastreo (id_rastreo, descripcion, activo) VALUES
        (1, 'a levantar', 1),
        (2, 'levantado', 1),
        (3, 'en sucursal', 1),
        (4, 'en reparto', 1),
        (5, 'entregado', 1),
        (6, 'devuelto', 1);

        -- Datos de prueba para la tabla paquetes
        INSERT INTO paquete (id_paquete, nombre_usuario, nombre_destinatario, telefono_destinatario, fecha_hora_registro, direccion_destinatario, cobro_destinatario, id_cat, id_rastreo) VALUES
        (1, 'cliente1', 'Juan Perez', '091234567', NOW(), 'Calle de la Paz 456', false, 2, 1),
        (2, 'cliente2', 'Maria Gomez', '098765432', NOW(), 'Avenida Libertador 123', true, 3, 1);  

        -- Insertar registro en logs
        INSERT INTO logs(fecha_hora, mensaje) VALUES (NOW(), 'Datos iniciales cargados.');
    END IF;
END^;

-- Llamar al procedimiento para cargar los datos iniciales
CALL cargar_datos_iniciales()^;
