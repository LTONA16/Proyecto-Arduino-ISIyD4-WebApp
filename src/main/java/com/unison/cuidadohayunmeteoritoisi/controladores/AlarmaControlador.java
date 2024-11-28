package com.unison.cuidadohayunmeteoritoisi.controladores;

import com.fazecast.jSerialComm.SerialPort;
import com.unison.cuidadohayunmeteoritoisi.modelos.ArduinoAlarma;
import com.unison.cuidadohayunmeteoritoisi.repositorios.ArduinoAlarmaRepositorio;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/alarma/*")
public class AlarmaControlador {

    @Autowired
    private ArduinoAlarmaRepositorio arAlarmaRep;
    //private SerialPort arPort;

/*
    // Configura el puerto serial al iniciar la aplicación

    //@PostConstruct
    public void configurarPuerto() {
        arPort = SerialPort.getCommPort("COM6");
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



    /*
    @Override
    public void onDataReceived(String data) {
        // Lógica adicional para procesar los datos
        System.out.println("Datos recibidos (Alarma) : " + data);

        // Verifica el formato de los datos (Array[2]: proximidad)
        String[] valores = data.split(",");
        if (valores.length >= 2) {
            try {
                // Guardar valor de proximidad y crear variables de activa y sonando
                double proximidad = 0;
                boolean activa = false;
                boolean sonando = false;
                ArduinoAlarma nuevaLectura = new ArduinoAlarma();

                if(valores[2].equals("Error")){
                    System.err.println("Error en el sensor de proximidad");
                    return;
                } else {
                    proximidad = Double.parseDouble(valores[2].trim());
                    System.out.println("Sensor de proximidad activo, su valor es: " + proximidad);
                    if (proximidad > 0 && proximidad <= 60) {
                        System.out.println("Alerta: La alarma sera activada...");
                        // Asignar valores a las variables
                        activa = true;
                        sonando = true;
                        nuevaLectura.setActiva(activa);
                        nuevaLectura.setSonando(sonando);
                        nuevaLectura.setProximidad(proximidad);
                        nuevaLectura.setFecha(LocalDateTime.now());

                    }else if(proximidad > 60){
                        System.out.println("La alarma esta desactivada");
                        // Asignar valores a las variables
                        activa = false;
                        sonando = false;
                        //nuevaLectura = new ArduinoAlarma(null, activa, sonando, proximidad, LocalDateTime.now());
                        nuevaLectura.setActiva(activa);
                        nuevaLectura.setSonando(sonando);
                        nuevaLectura.setProximidad(proximidad);
                        nuevaLectura.setFecha(LocalDateTime.now());
                    }
                }
                // Guardar en la base de datos
                arAlarmaRep.save(nuevaLectura);
                System.out.println("Datos de alarma guardados en la base de datos");
            } catch (NumberFormatException e) {
                System.err.println("Error al parsear los valores: " + e.getMessage());
            }
        } else {
            System.err.println("Formato inválido: " + data);
        }
    }



    // Metodo programado que se ejecuta cada 5 segundos para leer datos del puerto serial
    //@Scheduled(fixedRate = 5000)
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
                    System.out.println("Datos recibidos (Alarma) : " + data);

                    // Verifica el formato de los datos (Array[2]: proximidad)
                    String[] valores = data.split(",");
                    if (valores.length >= 2) {
                        try {
                            // Guardar valor de proximidad y crear variables de activa y sonando
                            double proximidad = 0;
                            boolean activa = false;
                            boolean sonando = false;
                            ArduinoAlarma nuevaLectura = new ArduinoAlarma();

                            if(valores[2].equals("Error")){
                                System.err.println("Error en el sensor de proximidad");
                                return;
                            } else {
                                proximidad = Double.parseDouble(valores[2].trim());
                                System.out.println("Sensor de proximidad activo, su valor es: " + proximidad);
                                if (proximidad > 0 && proximidad <= 60) {
                                    System.out.println("Alerta: La alarma sera activada...");
                                    // Asignar valores a las variables
                                    activa = true;
                                    sonando = true;
                                    nuevaLectura.setActiva(activa);
                                    nuevaLectura.setSonando(sonando);
                                    nuevaLectura.setProximidad(proximidad);
                                    nuevaLectura.setFecha(LocalDateTime.now());

                                }else if(proximidad > 60){
                                    System.out.println("La alarma esta desactivada");
                                    // Asignar valores a las variables
                                    activa = false;
                                    sonando = false;
                                    //nuevaLectura = new ArduinoAlarma(null, activa, sonando, proximidad, LocalDateTime.now());
                                    nuevaLectura.setActiva(activa);
                                    nuevaLectura.setSonando(sonando);
                                    nuevaLectura.setProximidad(proximidad);
                                    nuevaLectura.setFecha(LocalDateTime.now());
                                }
                            }
                            // Guardar en la base de datos
                            arAlarmaRep.save(nuevaLectura);
                            System.out.println("Datos de alarma guardados en la base de datos");
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
    // devuelve el último dato
    @GetMapping("/estado")
    @ResponseBody
    public ResponseEntity<ArduinoAlarma> obtenerEstadoAlarma(Model modelo) {
        ArduinoAlarma alarma = arAlarmaRep.findTop1ByOrderByFechaDesc();
        modelo.addAttribute("alarma", alarma);
        return ResponseEntity.ok(alarma);
    }

}