package com.unison.cuidadohayunmeteoritoisi.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "temperatura")
public class ArduinoTemperatura {

    @Serial
    private static final long serialVersionUID = -7242444295706165729L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private double temperatura;

    @NotNull
    private LocalDateTime fecha;

    @NotNull
    public double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(@NotNull double temperatura) {
        this.temperatura = temperatura;
    }

    public @NotNull LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(@NotNull LocalDateTime fecha) {
        this.fecha = fecha;
    }

}
