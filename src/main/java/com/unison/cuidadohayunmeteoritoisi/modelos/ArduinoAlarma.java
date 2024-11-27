package com.unison.cuidadohayunmeteoritoisi.modelos;

import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private boolean activa;

    @NotNull
    private boolean sonando;

    @NotNull
    private double proximidad;

    @NotNull
    private LocalDateTime fecha;

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setSonando(boolean sonando) {
        this.sonando = sonando;
    }

    public boolean isSonando() {
        return activa;
    }

    // Getter y Setter para 'proximidad'
    public double getProximidad() {
        return proximidad;
    }

    public void setProximidad(double proximidad) {
        this.proximidad = proximidad;
    }

    // Getter y Setter para 'fecha'
    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}
