package modelo;

public class Pago {
    private double monto;
    private String tipo;
    private MedioPago medio;
    private String estado;

    public Pago(double monto, String tipo, MedioPago medio) {
        this.monto = monto;
        this.tipo = tipo;
        this.medio = medio;
        estado = "PAGADO";
    }

    public Pago(double monto, String tipo, MedioPago medio, String estado) {
        this.monto = monto; this.tipo = tipo; this.medio = medio; this.estado = estado;
    }

    public double getMonto() { return monto; }
    public String getTipo() { return tipo; }
    public MedioPago getMedio() { return medio; }
    public String getEstado() { return estado; }

    public void reembolsar() {
        estado = "REEMBOLSADO";
    }

    public void retener() {
        estado = "RETENIDO";
    }

    @Override
    public String toString() {
        return tipo + ": $" + monto + " - " + medio + " - " + estado;
    }
}

