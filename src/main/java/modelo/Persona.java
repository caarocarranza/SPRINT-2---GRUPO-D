package modelo;

public class Persona {
    private String nombre;
    private String apellido;
    private String telefono;
    private int dni;

    public Persona(String nombre, String apellido, String telefono, int dni) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }   

    public String getApellido() {
        return apellido;
    }

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }
    
    public int getDni() {
        return dni;
    }

    public String getTelefono() {
        return telefono;
    }
}
