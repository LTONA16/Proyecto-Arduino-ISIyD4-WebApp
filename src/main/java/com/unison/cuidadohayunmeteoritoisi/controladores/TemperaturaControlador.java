package com.unison.cuidadohayunmeteoritoisi.controladores;

import com.unison.cuidadohayunmeteoritoisi.modelos.ArduinoHumedad;
import com.unison.cuidadohayunmeteoritoisi.modelos.ArduinoTemperatura;
import com.unison.cuidadohayunmeteoritoisi.repositorios.ArduinoTemperaturaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/temperatura/*")
public class TemperaturaControlador {

    @Autowired
    private ArduinoTemperaturaRepositorio temperaturaRep;

    @GetMapping("/datos")
    @ResponseBody
    public List<ArduinoTemperatura> obtenerDatosTemperatura(){
        return temperaturaRep.findTop20ByOrderByFechaDesc().stream().toList();
    }

    @RequestMapping("/*")
    public String mostrarDatosTemperatura(Model model){
        List<ArduinoTemperatura> datosTemperatura = temperaturaRep.findTop20ByOrderByFechaDesc();
        model.addAttribute("datosTemperatura", datosTemperatura);
        return "temperatura";
    }

    @GetMapping({"/filtrar/*", "/filtrar"})
    @ResponseBody
    public ResponseEntity<List<ArduinoTemperatura>> filtrarDatosPorFecha(@RequestParam("fechaInicio") String fechaInicio, @RequestParam("fechaFin") String fechaFin) {
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
            List<ArduinoTemperatura> datosFiltrados = temperaturaRep.findByFechaBetween(inicioDelDia, finDelDia);
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
