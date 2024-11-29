package com.unison.cuidadohayunmeteoritoisi.controladores;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.unison.cuidadohayunmeteoritoisi.modelos.ArduinoHumedad;
import com.unison.cuidadohayunmeteoritoisi.repositorios.ArduinoHumedadRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

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
    @GetMapping("/*")
    public String mostrarDatosHumedad(Model model) {
        List<ArduinoHumedad> datosHumedad = humedadRep.findTop20ByOrderByFechaDesc();
        model.addAttribute("datosHumedad", datosHumedad);
        return "humedad";
    }

    @GetMapping({"/filtrar/*", "/filtrar"})
    @ResponseBody
    public ResponseEntity<List<ArduinoHumedad>> filtrarDatosPorFecha(@RequestParam("fechaInicio") String fechaInicio, @RequestParam("fechaFin") String fechaFin) {
        try {
            // Debug: Verifica que la fecha recibida sea correcta
            System.out.println("Fecha de inicio recibida: " + fechaInicio);
            System.out.println("Fecha fin recibida: " + fechaFin);

            // Nos aseguramos que la fecha esté en el formato (yyyy-MM-dd)
            LocalDate fechaInicioParsed = LocalDate.parse(fechaInicio, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate fechaFinParsed = LocalDate.parse(fechaFin, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            // Obtenemos el inicio del día (00:00:00) y el final del día de la fecha seleccionada (23:59:59)
            LocalDateTime inicioDelDia = fechaInicioParsed.atStartOfDay();
            LocalDateTime finDelDia = fechaFinParsed.plusDays(1).atStartOfDay().minusNanos(1);

            // Aquí filtramos los registros para el día completo de la fecha seleccionada.
            List<ArduinoHumedad> datosFiltrados = humedadRep.findByFechaBetween(inicioDelDia, finDelDia);
            System.out.println(datosFiltrados);
            if (datosFiltrados.isEmpty()) {
                // Si no hay datos, respondemos con código 204 (No Content)
                return ResponseEntity.noContent().build();
            }
            // Si encontramos los datos, los devolvemos con código 200 (OK)
            return ResponseEntity.ok(datosFiltrados);
        } catch (Exception e) {
            e.printStackTrace(); // Registra cualquier excepción que ocurra
            // Responde con un error 400 si algo salió mal
            return ResponseEntity.badRequest().body(null);
        }
    }



}