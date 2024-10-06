package com.example.envios_bios.controladores;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ControladorErrores implements ErrorController{

    @GetMapping("/mierror")
    public String mostrarError(HttpServletRequest request, Model model){
        Integer estado = (Integer)request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE); //Codigo entero del numero de estado de la peticion
        if(estado != null){//Si hay estado
            if(estado == HttpStatus.NOT_FOUND.value()){//Con esto verficamos el error 404 no encontrado
                model.addAttribute("status", estado);
                model.addAttribute("error", HttpStatus.NOT_FOUND.getReasonPhrase()); //Obtenemos la descripcion del error y la guardamos en el model
                return "errores/not-found";
            }
            if(estado == HttpStatus.INTERNAL_SERVER_ERROR.value()){//Con esto verficamos el error 500 error en el servidor
                model.addAttribute("status", estado);
                model.addAttribute("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()); //Obtenemos la descripcion del error y la guardamos en el model
                return "errores/server-error";
            }
        }
        return "errores/mi-error";//Retornamos una pagina con error general
        
    }
    
}
