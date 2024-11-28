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
    private ArduinoHumedadRepositorio humedadRep;
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
            leerYGuardarDatosAlarma();
            leerYGuardarDatosHumedad();
            leerYGuardarDatosTemperatura();
        } else {
            System.err.println("Error al abrir el puerto serial. Puerto seleccionado: " + puerto);
        }

    }

    public void leerYGuardarDatosAlarma() {

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

    public void leerYGuardarDatosHumedad() {

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

    public void leerYGuardarDatosTemperatura() {

        try {
            // Si hay datos disponibles para leer, los procesamos
            if (arPort.bytesAvailable() > 0) {
                byte[] buffer = new byte[arPort.bytesAvailable()];
                int bytesRead = arPort.readBytes(buffer, buffer.length);

                if (bytesRead > 0) {
                    String data = new String(buffer).trim();
                    System.out.println("Datos recibidos (Temperatura): " + data);

                    // Verifica el formato de los datos (espera temperatura, humedad y un valor extra)
                    String[] valores = data.split(",");
                    if (valores.length >= 2) { // Asegura que haya al menos dos valores
                        try {
                            // Asignamos correctamente los valores:
                            // Primero la temperatura y luego la humedad
                            double temperatura = Double.parseDouble(valores[0]);
                            //double humedad = Double.parseDouble(valores[1]);

                            // Guardar en la base de datos
                            ArduinoTemperatura lectura = new ArduinoTemperatura();
                            //lectura.setHumedad(humedad);
                            lectura.setTemperatura(temperatura);
                            lectura.setFecha(LocalDateTime.now());
                            temperaturaRep.save(lectura);
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

}
