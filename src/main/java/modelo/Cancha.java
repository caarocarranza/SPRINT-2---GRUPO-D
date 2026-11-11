package modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class Cancha {
    private int numero;
    private TipoCancha tipo;
    private boolean disponibilidad;

    public Cancha(int numero, TipoCancha tipo,boolean disponibilidad) {
        this.numero = numero;
        this.tipo = tipo;
        this.disponibilidad = disponibilidad;
    }

    public int getNumero() {
        return numero;
    }

    public boolean getDisponibilidad(){
        return this.disponibilidad;
    }

    public TipoCancha getTipo() {
        return tipo;
    }

    public double getPrecio() {
        return tipo.getPrecio();
    }

    public void cambiarDisponibilidad(){

        if(this.disponibilidad == true){
             this.disponibilidad = false;
        }
        else if (this.disponibilidad == false){
            this.disponibilidad = true;
        }
    }

    
    public boolean estaDisponible(LocalDate fecha, LocalTime hora, List<Reserva> reservas){
        if (!this.disponibilidad){
            return false;
        }

        LocalDateTime inicioSolicitado = LocalDateTime.of(fecha, hora);
        LocalDateTime finSolicitado = inicioSolicitado.plusMinutes(120);

        for (Reserva reserva : reservas){
            if (reserva.getCancha() != this){
                continue;
            }

            LocalDateTime inicioReserva = LocalDateTime.of(
                reserva.getFecha(),
                reserva.getHora()
            );
            LocalDateTime finReserva = inicioReserva.plusMinutes(120);

            if (inicioSolicitado.isBefore(finReserva) && finSolicitado.isAfter(inicioReserva)){
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
    return "Cancha " + numero + " (" + tipo + ")";
    }
    
}


