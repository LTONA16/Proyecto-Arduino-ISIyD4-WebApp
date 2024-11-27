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
    private double humedad;

    @NotNull
    private double temperatura;

    @NotNull
    private LocalDateTime fecha;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    public double getHumedad() {
        return humedad;
    }

    public void setHumedad(@NotNull long humedad) {
        this.humedad = humedad;
    }

    @NotNull
    public double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(@NotNull long temperatura) {
        this.temperatura = temperatura;
    }

    public @NotNull LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(@NotNull LocalDateTime fecha) {
        this.fecha = fecha;
    }
}
