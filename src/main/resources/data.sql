CREATE PROCEDURE IF NOT EXISTS cargar_datos_iniciales()
BEGIN
    IF NOT EXISTS (
        SELECT *
        FROM logs
    ) THEN  
        INSERT INTO categorias(idCat, nombre)
        VALUES
            (1, 'Categoria1'),
            (2, 'Categoria2'),
            (3, 'Categoria3'),
            (4, 'Categoria4');

        INSERT INTO estadoRastreo(idRastreo, descripcion)
        VALUES
            (1, 'EstadoRastreo1'),
            (2, 'EstadoRastreo2'),
            (3, 'EstadoRastreo3'),
            (4, 'EstadoRastreo4');     
        
        
       
        INSERT INTO logs(fecha_hora, mensaje)
        VALUES (NOW(), 'Datos iniciales cargados.');
    END IF;
END^;

CALL cargar_datos_iniciales()^;