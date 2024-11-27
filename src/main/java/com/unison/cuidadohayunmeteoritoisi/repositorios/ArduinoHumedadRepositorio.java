package com.unison.cuidadohayunmeteoritoisi.repositorios;

import com.unison.cuidadohayunmeteoritoisi.modelos.ArduinoHumedad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface ArduinoHumedadRepositorio extends JpaRepository<ArduinoHumedad, Long> {
    List<ArduinoHumedad> findTop20ByOrderByFechaDesc();

}

