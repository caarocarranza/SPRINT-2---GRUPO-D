package modelo;

public class Empleado extends Persona {
    private RolEmpleado rol;

    public Empleado(String nombre, String apellido, String telefono, int dni, RolEmpleado rol) {
        super(nombre, apellido, telefono, dni);
        this.rol = rol;
    }

    public RolEmpleado getRol() {
        return rol;
    }
}

