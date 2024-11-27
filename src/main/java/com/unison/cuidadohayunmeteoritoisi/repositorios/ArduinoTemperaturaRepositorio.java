package com.unison.cuidadohayunmeteoritoisi.repositorios;

import com.unison.cuidadohayunmeteoritoisi.modelos.ArduinoTemperatura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArduinoTemperaturaRepositorio extends JpaRepository<ArduinoTemperatura, Long> {
    List<ArduinoTemperatura> findTop20ByOrderByFechaDesc();
}
