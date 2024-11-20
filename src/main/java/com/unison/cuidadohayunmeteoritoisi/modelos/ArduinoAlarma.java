package com.unison.cuidadohayunmeteoritoisi.modelos;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "alarma")
public class ArduinoAlarma implements Serializable {

    @Serial
    private static final long serialVersionUID = 3660617525846421889L;

    @Id
    private Long id;

    @NotNull
    private boolean activa;

    @NotNull
    private boolean sonando;

    @NotNull
    private double proximidad;

    @NotNull
    private LocalDateTime fecha;
}
