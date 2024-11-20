package com.unison.cuidadohayunmeteoritoisi.repositorios;

import com.unison.cuidadohayunmeteoritoisi.modelos.ArduinoAlarma;
import com.unison.cuidadohayunmeteoritoisi.modelos.ArduinoHumedad;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArduinoAlarmaRepositorio extends JpaRepository<ArduinoAlarma, Long> {
}
