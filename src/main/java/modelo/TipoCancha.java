package modelo;

public enum TipoCancha {
    PARED(35000), BLINDEX(45000);

    private double precio;

    TipoCancha(double precio) {
        this.precio = precio;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        if (precio <= 0) {
            System.out.println("Error: El precio debe ser mayor a cero.");
            return;
        }
        this.precio = precio;
    }
}