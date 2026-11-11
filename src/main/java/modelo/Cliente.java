package modelo;

public class Cliente extends Persona {
    private int inasistencias;

    public Cliente(String nombre, String apellido, String telefono, int dni) {
        super(nombre, apellido, telefono, dni);
        inasistencias = 0;
    }

    public void registrarInasistencia() {
        inasistencias++;
    }

    public int getInasistencias() {
        return inasistencias;
    }
    
    public String convertirALineaArchivo() {
        return getNombre() + ";" + getApellido() + ";" + getTelefono() + ";" + getDni() + ";" + getInasistencias();
    }
    
    public static Cliente crearDesdeLinea(String linea) {
    String[] datos = linea.split(";");
    String nombre = datos[0];
    String apellido = datos[1];
    String telefono = datos[2];
    int dni = Integer.parseInt(datos[3]);
    int inasistencias = 0;
    if (datos.length > 4) {
        inasistencias = Integer.parseInt(datos[4]);
    }

    Cliente c = new Cliente(nombre, apellido, telefono, dni);
    for (int i = 0; i < inasistencias; i++) {
        c.registrarInasistencia();
    }
    return c;
}
}