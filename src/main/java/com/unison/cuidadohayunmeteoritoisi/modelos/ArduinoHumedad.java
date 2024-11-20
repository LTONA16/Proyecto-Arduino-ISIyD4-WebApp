package com.unison.cuidadohayunmeteoritoisi.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "humedad")
public class ArduinoHumedad implements Serializable {

    @Serial
    private static final long serialVersionUID = 7169631338699191661L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private long humedad;

    @NotNull
    private long temperatura;

    @NotNull
    private LocalDateTime fecha;
}
