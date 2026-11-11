package modelo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.Duration;

public class Reserva {
    private int numero;
    private Cliente cliente;
    private Cancha cancha;
    private LocalDate fecha;
    private LocalTime hora;
    private double monto;
    private String estado;
    private Pago sena;
    private Pago saldo;

    public Reserva(int numero, Cliente cliente, Cancha cancha, LocalDate fecha, LocalTime hora, MedioPago medio) {
        this.numero = numero;
        this.cliente = cliente;
        this.cancha = cancha;
        this.fecha = fecha;
        this.hora = hora;
        this.monto = cancha.getPrecio();
        this.estado = "CONFIRMADA";
        this.sena = new Pago(monto * 0.5, "SEÑA", medio);
        this.saldo = null;
    }

    public Reserva(int numero, Cliente cliente, Cancha cancha, LocalDate fecha, LocalTime hora, double monto, String estado, MedioPago medioSena, String estadoSena, MedioPago medioSaldo, String estadoSaldo) {
        this.numero = numero; this.cliente = cliente; this.cancha = cancha;
        this.fecha = fecha; this.hora = hora; this.monto = monto; this.estado = estado;
        this.sena = new Pago(monto * 0.5, "SEÑA", medioSena, estadoSena);
        this.saldo = medioSaldo == null ? null : new Pago(monto * 0.5, "SALDO", medioSaldo, estadoSaldo);
    }

    public boolean seSuperpone(LocalDate otraFecha, LocalTime otraHora) {
        if (!fecha.equals(otraFecha) || estado.equals("CANCELADA") || estado.equals("AUSENTE") || estado.equals("COMPLETADA")) {
            return false;
        }

        int inicio = hora.toSecondOfDay() / 60;
        int otroInicio = otraHora.toSecondOfDay() / 60;
        return Math.abs(inicio - otroInicio) < 120;
    }

    public void cancelar(LocalDateTime ahora) {
        if (!estado.equals("CONFIRMADA")) {
            System.out.println("La reserva no se puede cancelar.");
            return;
        }

        LocalDateTime inicioTurno = LocalDateTime.of(fecha, hora);
        long horas = Duration.between(ahora, inicioTurno).toHours();

        if (horas >= 24) {
            sena.reembolsar();
        } else {
            sena.retener();
        }
        estado = "CANCELADA";
    }

    public void registrarAsistencia(boolean asistio, MedioPago medioSaldo) {
        if (!estado.equals("CONFIRMADA")) {
            System.out.println("La reserva ya fue cerrada.");
            return;
        }

        if (asistio) {
            saldo = new Pago(monto * 0.5, "SALDO", medioSaldo);
            estado = "COMPLETADA";
        } else {
            sena.retener();
            cliente.registrarInasistencia();
            estado = "AUSENTE";
        }
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Cancha getCancha() {
        return cancha;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public int getNumero() {
    return numero;
    }

    public String getEstado() { return estado; }
    public double getMonto() { return monto; }
    public Pago getSena() { return sena; }
    public Pago getSaldo() { return saldo; }

    public String convertirALineaArchivo() {
        return numero + ";" + cliente.getDni() + ";" + cancha.getNumero() + ";" + fecha + ";" + hora + ";" + estado;
    }
    
    @Override
    public String toString() {
        return "Reserva " + numero + " | " + cancha + " | " + fecha + " " + hora + " | " + cliente.getNombreCompleto() + " | $" + monto + " | " + estado;
    }
}
