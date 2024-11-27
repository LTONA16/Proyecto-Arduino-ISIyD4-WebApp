package com.unison.cuidadohayunmeteoritoisi.controladores;

import com.fazecast.jSerialComm.SerialPort;
import com.unison.cuidadohayunmeteoritoisi.modelos.ArduinoHumedad;
import com.unison.cuidadohayunmeteoritoisi.repositorios.ArduinoHumedadRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/humedad/*")
public class HumedadControlador {

    @Autowired
    private ArduinoHumedadRepositorio humedadRep;

    /*private SerialPort arPort;

    // Configura el puerto serial al iniciar la aplicación

    @PostConstruct
    public void configurarPuerto() {
        arPort = SerialPort.getCommPort("COM6"); // Cambiar "COM6" al puerto correcto
        arPort.setComPortParameters(9600, 8, 1, SerialPort.NO_PARITY);
        arPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
    }

    // Cierra el puerto serial al detener la aplicación
    @PreDestroy
    public void cerrarPuerto() {
        if (arPort != null && arPort.isOpen()) {
            arPort.closePort();
            System.out.println("Puerto serial cerrado.");
        }
    }



    @Scheduled(fixedRate = 5000)
    public void guardarDatosArduino(){
        onDataReceived();
    }


    @Override
    public void onDataReceived(String data) {
        System.out.println("Datos recibidos (Humedad): " + data);

        // Verifica el formato de los datos (espera temperatura, humedad y un valor extra)
        String[] valores = data.split(",");
        if (valores.length >= 2) { // Asegura que haya al menos dos valores
            try {
                // Asignamos correctamente los valores:
                // Primero la temperatura y luego la humedad
                double temperatura = Double.parseDouble(valores[1]);  // Humedad -> temperatura
                double humedad = Double.parseDouble(valores[0]);  // Temperatura -> humedad

                // Guardar en la base de datos
                ArduinoHumedad lectura = new ArduinoHumedad();
                lectura.setHumedad(humedad);
                lectura.setTemperatura(temperatura);
                lectura.setFecha(LocalDateTime.now());
                arduinoHumedadRepositorio.save(lectura);
                System.out.println("Datos guardados en la base de datos");
            } catch (NumberFormatException e) {
                System.err.println("Error al parsear los valores: " + e.getMessage());
            }
        } else {
            System.err.println("Formato inválido: " + data);
        }
    }


    // Método programado que se ejecuta cada 5 segundos para leer datos del puerto serial
    @Scheduled(fixedRate = 5000)
    public void leerYGuardarDatos() {
        // Verifica si el puerto está abierto antes de leer
        if (!arPort.isOpen()) {
            if (!arPort.openPort()) {
                System.err.println("No se pudo abrir el puerto COM6.");
                return;
            }
        }

        try {
            // Si hay datos disponibles para leer, los procesamos
            if (arPort.bytesAvailable() > 0) {
                byte[] buffer = new byte[arPort.bytesAvailable()];
                int bytesRead = arPort.readBytes(buffer, buffer.length);

                if (bytesRead > 0) {
                    String data = new String(buffer).trim();
                    System.out.println("Datos recibidos (Humedad): " + data);

                    // Verifica el formato de los datos (espera temperatura, humedad y un valor extra)
                    String[] valores = data.split(",");
                    if (valores.length >= 2) { // Asegura que haya al menos dos valores
                        try {
                            // Asignamos correctamente los valores:
                            // Primero la temperatura y luego la humedad
                            //double temperatura = Double.parseDouble(valores[0]);
                            double humedad = Double.parseDouble(valores[1]);

                            // Guardar en la base de datos
                            ArduinoHumedad lectura = new ArduinoHumedad();
                            lectura.setHumedad(humedad);
                            //lectura.setTemperatura(temperatura);
                            lectura.setFecha(LocalDateTime.now());
                            humedadRep.save(lectura);
                            System.out.println("Datos guardados en la base de datos");
                        } catch (NumberFormatException e) {
                            System.err.println("Error al parsear los valores: " + e.getMessage());
                        }
                    } else {
                        System.err.println("Formato inválido: " + data);
                    }
                }
            } else {
                System.out.println("No hay datos disponibles en el puerto.");
            }
        } catch (Exception e) {
            System.err.println("Error al leer el puerto serial: " + e.getMessage());
            e.printStackTrace();
        }
    }
*/

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