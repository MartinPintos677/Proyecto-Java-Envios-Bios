CREATE PROCEDURE IF NOT EXISTS cargar_datos_iniciales()
BEGIN
    IF NOT EXISTS (
        SELECT * FROM logs
    ) THEN  

        -- Datos de prueba para la tabla sucursales
        INSERT INTO sucursal (numero, nombre) VALUES
        (1, 'Sucursal 1'),
        (2, 'Sucursal 2'),
        (3, 'Sucursal 3'),
        (4, 'Sucursal 4');

        -- Datos de prueba para la tabla usuarios
        INSERT INTO usuarios (nombre_usuario, clave_de_acceso, email) VALUES        

        -- Clave clave123
        ('cliente1', '$2a$10$QJu21AwLWBpyOGoRV3zH7ecZIM4.piwNLuQjnLtcoCUDzoyKLXnAG', 'cliente1@gmail.com'),

        -- Clave clave456
        ('cliente2', '$2a$10$pjnPm1rV49t0qcxsaDjJN.HUZ8i2l521t8pOkTX52O3IbI1XSVVsa', 'cliente2@gmail.com'),

        -- Clave empleado123
        ('empleado1', '$2a$10$DiSj/4qyqVBNBkBWVVFHNO1ND5mZ4xXt1BmtCcFjJODlJzWeTs6Dq', 'empleado1@enviosbios.com'),

        -- Clave empleado456
        ('empleado2', '$2a$10$Il1qsPOCz9fQ0ri1S9Z2Gu.rPDyigosoMBJhtz03ZbwTLFxA5JsCa', 'empleado2@gmail.com');


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
        ('empleado2', 'empleado'),  
        ('cliente1', 'cliente'),
        ('cliente2', 'cliente'); 


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
        (1, 'cliente1', 'Juan Perez', '091234567', '2024-10-15 22:35:36.000000', 'Calle de la Paz 456', false, 2, 1),
        (2, 'cliente2', 'Maria Gomez', '098765432', '2024-10-16 18:12:21.000000', 'Avenida Libertador 123', true, 3, 1),
        (3, 'cliente1', 'Mario Martínez', '098423654', '2024-10-16 20:35:36.000000', 'Calle Colonia 3541', false, 2, 4),
        (4, 'cliente2', 'Camila Sánchez', '091654987', '2024-10-16 22:40:11.000000', 'Avenida 18 de Julio 321', true, 3, 1),
        (5, 'cliente1', 'Arturo González', '096369741', '2024-10-17 10:15:05.000000', 'Calle Columbia 1456', false, 2, 1),
        (6, 'cliente2', 'Marina García', '093852147', '2024-10-17 21:22:12.000000', 'Avenida 8 de Octubre 3695', true, 3, 4),
        (7, 'cliente1', 'Juan Rodríguez', '091596321', '2024-10-18 12:35:36.000000', 'Calle Yaguarón 4321', false, 2, 1),
        (8, 'cliente2', 'Maria Gomez', '097569874', '2024-10-19 15:23:06.000000', 'Avenida 18 de Julio 123', true, 3, 2),
        (9, 'cliente1', 'Juan Suarez', '099412521', '2024-10-20 22:35:36.000000', 'Calle Rio Branco 1385', false, 2, 1),
        (10, 'cliente2', 'Mariana Gimenez', '098258777','2024-10-21 16:18:36.000000', 'Avenida Libertador 6589', true, 3, 4);  

        -- Insertar registro en logs
        INSERT INTO logs(fecha_hora, mensaje) VALUES (NOW(), 'Datos iniciales cargados.');
    END IF;
END^;

-- Llamar al procedimiento para cargar los datos iniciales
CALL cargar_datos_iniciales()^;
