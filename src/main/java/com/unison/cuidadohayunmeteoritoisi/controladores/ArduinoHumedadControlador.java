package com.unison.cuidadohayunmeteoritoisi.controladores;

import com.fazecast.jSerialComm.SerialPort;
import com.unison.cuidadohayunmeteoritoisi.repositorios.ArduinoHumedadRepositorio;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/arduinoHumedad/*")
public class ArduinoHumedadControlador {

    SerialPort arPort = SerialPort.getCommPort("COM7");
    private ArduinoHumedadRepositorio aHRepo;

    @RequestMapping("/*")
    public String mostrarDatosHumedad(){
        leerDatosHumedad();
        return "arduinoHumedad";
    }


    private void leerDatosHumedad(){
        // Encuentra y abre el puerto
        arPort.setComPortParameters(9600, 8, 1, SerialPort.NO_PARITY);
        arPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);

        if (arPort.openPort()) {
            System.out.println("Puerto abierto exitosamente.");
        } else {
            System.out.println("No se pudo abrir el puerto.");
            return;
        }

        // Lee datos del puerto
        try {
            while (true) {
                if (arPort.bytesAvailable() > 0) {
                    byte[] buffer = new byte[arPort.bytesAvailable()];
                    arPort.readBytes(buffer, buffer.length);
                    String data = new String(buffer);
                    System.out.println("Dato recibido: " + data.trim());
                }
                Thread.sleep(5000); // Lee datos cada 5s
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            arPort.closePort();
            System.out.println("Puerto cerrado.");
        }
    }
}
