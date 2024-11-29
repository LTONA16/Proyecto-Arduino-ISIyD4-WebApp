package com.unison.cuidadohayunmeteoritoisi.controladores;

import com.fazecast.jSerialComm.SerialPort;
import com.unison.cuidadohayunmeteoritoisi.modelos.ArduinoAlarma;
import com.unison.cuidadohayunmeteoritoisi.modelos.ArduinoHumedad;
import com.unison.cuidadohayunmeteoritoisi.modelos.ArduinoTemperatura;
import com.unison.cuidadohayunmeteoritoisi.repositorios.ArduinoAlarmaRepositorio;
import com.unison.cuidadohayunmeteoritoisi.repositorios.ArduinoHumedadRepositorio;
import com.unison.cuidadohayunmeteoritoisi.repositorios.ArduinoTemperaturaRepositorio;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
public class ArduinoDataControlador {

    @Autowired
    private ArduinoAlarmaRepositorio arAlarmaRep;
    @Autowired
    private ArduinoHumedadRepositorio humedadRep;
    @Autowired
    private ArduinoTemperaturaRepositorio temperaturaRep;

    String puerto = "COM6";
    private SerialPort arPort;

    @PostConstruct
    public void configurarPuerto() {
        arPort = SerialPort.getCommPort(puerto);
        arPort.setComPortParameters(9600, 8, 1, SerialPort.NO_PARITY);
        arPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        if (arPort.openPort()) {
            System.out.println("Puerto serial abierto.");
        } else {
            System.err.println("Error al abrir el puerto serial. Puerto seleccionado: " + puerto);
        }
    }

    // Cierra el puerto serial al detener la aplicación
    @PreDestroy
    public void cerrarPuerto() {
        if (arPort != null && arPort.isOpen()) {
            arPort.closePort();
            System.out.println("Puerto serial cerrado.");
        }
    }

    // Métodos para guardar datos de Arduino
    @Scheduled(fixedRate = 5000)
    public void leerYGuardarDatos() {
        if (arPort == null || !arPort.isOpen()) {
            System.err.println("Puerto serial no disponible. Puerto seleccionado: " + puerto);
        } else if (arPort.openPort()) {
            System.out.println("Puerto serial abierto.");
            leerYGuardarDatosTodo();
        } else {
            System.err.println("Error al abrir el puerto serial. Puerto seleccionado: " + puerto);
        }

    }

    public void leerYGuardarDatosTodo() {
        try {
            // Si hay datos disponibles para leer, los procesamos
            if (arPort.bytesAvailable() > 0) {
                byte[] buffer = new byte[arPort.bytesAvailable()];
                int bytesRead = arPort.readBytes(buffer, buffer.length);

                if (bytesRead > 0) {
                    String data = new String(buffer).trim();
                    System.out.println("Datos recibidos: " + data);

                    // Verifica el formato de los datos
                    String[] valores = data.split(",");
                    if (valores.length >= 3) { // Asegura que haya al menos tres valores (uno para cada sensor)
                        // Procesar datos de cada sensor
                        double proximidad = 0, humedad = 0, temperatura = 0;
                        boolean alarmaActiva = false, alarmaSonando = false;

                        try {
                            // Procesar los datos de la alarma (proximidad)
                            if (valores.length >= 3 && !valores[2].equals("Error")) {
                                proximidad = Double.parseDouble(valores[2].trim());
                                alarmaActiva = proximidad > 0 && proximidad <= 30;
                                alarmaSonando = alarmaActiva;
                            }

                            // Procesar los datos de humedad
                            if (valores.length >= 2) {
                                humedad = Double.parseDouble(valores[1].trim());
                            }

                            // Procesar los datos de temperatura
                            if (valores.length >= 1) {
                                temperatura = Double.parseDouble(valores[0].trim());
                            }

                            // Crear y guardar las lecturas de cada sensor
                            // Guardar alarma
                            ArduinoAlarma nuevaAlarma = new ArduinoAlarma();
                            nuevaAlarma.setProximidad(proximidad);
                            nuevaAlarma.setActiva(alarmaActiva);
                            nuevaAlarma.setSonando(alarmaSonando);
                            nuevaAlarma.setFecha(LocalDateTime.now());
                            arAlarmaRep.save(nuevaAlarma);

                            // Guardar humedad
                            ArduinoHumedad nuevaHumedad = new ArduinoHumedad();
                            nuevaHumedad.setHumedad(humedad);
                            nuevaHumedad.setFecha(LocalDateTime.now());
                            humedadRep.save(nuevaHumedad);

                            // Guardar temperatura
                            ArduinoTemperatura nuevaTemperatura = new ArduinoTemperatura();
                            nuevaTemperatura.setTemperatura(temperatura);
                            nuevaTemperatura.setFecha(LocalDateTime.now());
                            temperaturaRep.save(nuevaTemperatura);

                            System.out.println("Datos de alarma, humedad y temperatura guardados en la base de datos.");
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

}
