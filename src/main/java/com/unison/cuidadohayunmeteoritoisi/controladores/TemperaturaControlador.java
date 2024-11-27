package com.unison.cuidadohayunmeteoritoisi.controladores;

import com.unison.cuidadohayunmeteoritoisi.modelos.ArduinoTemperatura;
import com.unison.cuidadohayunmeteoritoisi.repositorios.ArduinoTemperaturaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/temperatura/*")
public class TemperaturaControlador {

    @Autowired
    private ArduinoTemperaturaRepositorio temperaturaRep;

    @GetMapping("/datos")
    @ResponseBody
    public List<ArduinoTemperatura> obtenerDatosTemperatura(){
        return temperaturaRep.findTop20ByOrderByFechaDesc();
    }

    @RequestMapping("/*")
    public String mostrarDatosTemperatura(Model modelo){
        List<ArduinoTemperatura> temperaturas = obtenerDatosTemperatura();
        modelo.addAttribute("datosTemperatura", temperaturas);
        return "temperatura";
    }
}
