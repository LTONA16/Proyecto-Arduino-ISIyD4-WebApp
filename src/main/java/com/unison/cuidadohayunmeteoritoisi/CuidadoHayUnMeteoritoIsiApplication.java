package com.unison.cuidadohayunmeteoritoisi;

import com.unison.cuidadohayunmeteoritoisi.controladores.ArduinoAlarmaControlador;
import com.unison.cuidadohayunmeteoritoisi.controladores.ArduinoControlador;
import com.unison.cuidadohayunmeteoritoisi.controladores.ArduinoHumedadControlador;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CuidadoHayUnMeteoritoIsiApplication {
    public static void main(String[] args) {
        SpringApplication.run(CuidadoHayUnMeteoritoIsiApplication.class, args);
        /*
        ArduinoControlador controller = new ArduinoControlador("COM6");
        if (controller.openPort()) {
            System.out.println("Puerto serie abierto exitosamente.");

            // Lectores de datos
            controller.addListener(new ArduinoAlarmaControlador());
            controller.addListener(new ArduinoHumedadControlador());

            // Iniciar la lectura de datos
            controller.startReading();
        } else {
            System.out.println("No se pudo abrir el puerto.");
        }
        */
    }
}