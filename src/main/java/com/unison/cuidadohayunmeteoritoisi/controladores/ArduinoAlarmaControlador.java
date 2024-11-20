package com.unison.cuidadohayunmeteoritoisi.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/arduinoAlarma/*")
public class ArduinoAlarmaControlador {

    @RequestMapping("/*")
    public String mostrarAlarma(){
        return "arduinoAlarma";
    }


}
