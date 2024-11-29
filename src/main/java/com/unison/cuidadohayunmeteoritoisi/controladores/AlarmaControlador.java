package com.unison.cuidadohayunmeteoritoisi.controladores;


import com.unison.cuidadohayunmeteoritoisi.modelos.ArduinoAlarma;
import com.unison.cuidadohayunmeteoritoisi.repositorios.ArduinoAlarmaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
@RequestMapping("/alarma/*")
public class AlarmaControlador {

    @Autowired
    private ArduinoAlarmaRepositorio arAlarmaRep;

    // devuelve el último dato
    @GetMapping("/estado")
    @ResponseBody
    public ResponseEntity<ArduinoAlarma> obtenerEstadoAlarma(Model modelo) {
        ArduinoAlarma alarma = arAlarmaRep.findTop1ByOrderByFechaDesc();
        modelo.addAttribute("alarma", alarma);
        return ResponseEntity.ok(alarma);
    }

}