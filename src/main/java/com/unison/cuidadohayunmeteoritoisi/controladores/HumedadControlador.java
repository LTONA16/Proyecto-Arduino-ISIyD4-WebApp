package com.unison.cuidadohayunmeteoritoisi.controladores;


import com.unison.cuidadohayunmeteoritoisi.modelos.ArduinoHumedad;
import com.unison.cuidadohayunmeteoritoisi.repositorios.ArduinoHumedadRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;

@Controller
@RequestMapping("/humedad/*")
public class HumedadControlador {

    @Autowired
    private ArduinoHumedadRepositorio humedadRep;

    // devuelve los últimos 20 datos en formato JSON
    @GetMapping("/datos")
    @ResponseBody
    public List<ArduinoHumedad> obtenerDatosHumedad() {
        return humedadRep.findTop20ByOrderByFechaDesc().stream().toList(); // Devuelve los últimos 20 registros
    }


    // Muestra la página de humedad y temperatura con los datos cargados
    @RequestMapping("/*")
    public String mostrarDatosHumedad(Model model) {
        List<ArduinoHumedad> datosHumedad = obtenerDatosHumedad();
        model.addAttribute("datosHumedad", datosHumedad); // Obtén los datos más recientes
        return "humedad";
    }
}