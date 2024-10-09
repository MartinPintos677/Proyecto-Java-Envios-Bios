package com.example.envios_bios.servicios;



import org.springframework.data.domain.Page;

import com.example.envios_bios.dominio.Categoria;

public interface IServicioPaginacion {
    
    Page<Categoria> listarP();
    Page<Categoria> buscarP(String criterio);
}
