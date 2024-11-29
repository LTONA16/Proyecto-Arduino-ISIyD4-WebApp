package com.unison.cuidadohayunmeteoritoisi.repositorios;

import com.unison.cuidadohayunmeteoritoisi.modelos.ArduinoHumedad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface ArduinoHumedadRepositorio extends JpaRepository<ArduinoHumedad, Long> {
    List<ArduinoHumedad> findTop20ByOrderByFechaDesc();

    List<ArduinoHumedad> findByFechaBetween(LocalDateTime startDate, LocalDateTime endDate);
}

