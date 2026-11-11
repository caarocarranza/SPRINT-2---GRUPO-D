
package vista;

import controlador.GestorReserva;
import modelo.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

public class Menu {
    private GestorReserva sistema;
    private Empleado empleadoActual;
    private Scanner teclado;

    public Menu(GestorReserva sistema, Empleado empleadoActual) {
        this.sistema = sistema;
        this.empleadoActual = empleadoActual;
        this.teclado = new Scanner(System.in);
    }

    public void iniciar() {
        int opcion = -1;

        while (opcion != 0) {
            mostrarMenu();

            try {
                opcion = Integer.parseInt(teclado.nextLine());

                switch (opcion) {
                    case 1: registrarCliente(); break;
                    case 2: listarClientes(); break;
                    case 3: registrarCancha(); break;
                    case 4: consultarDisponibilidad(); break;
                    case 5: registrarReserva(); break;
                    case 6: consultarReservas(); break;
                    case 7: cancelarReserva(); break;
                    case 8: registrarAsistencia(); break;
                    case 9: modificarPrecio(); break;
                    case 10: listarCanchas(); break;
                    case 0:
                        System.out.println("Saliendo del sistema...");
                        break;
                    default:
                        System.out.println("Opción incorrecta.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Error: ingresá un número válido.");
            } catch (Exception e) {
                System.out.println("No se pudo realizar la operación: " + e.getMessage());
            }
        }
    }

    private void mostrarMenu() {
        System.out.println("╔══════════════════════════════════╗");
        System.out.println("       TURNOS CLUB DE PADEL ");
        System.out.println("╠══════════════════════════════════╣");
        System.out.println("1. Registrar cliente");
        System.out.println("2. Listar clientes");
        System.out.println("3. Registrar cancha");
        System.out.println("4. Consultar disponibilidad");
        System.out.println("5. Registrar reserva");
        System.out.println("6. Consultar reservas de un cliente");
        System.out.println("7. Cancelar reserva");
        System.out.println("8. Registrar asistencia / ausencia");
        System.out.println("9. Modificar precio (encargado)");
        System.out.println("10. Listar canchas");
        System.out.println("0. Salir");
        System.out.println("╚══════════════════════════════════╝");
        System.out.print("Elegí una opción: ");
    }

    private void registrarCliente() {
        System.out.print("Nombre: ");
        String nombre = teclado.nextLine().trim();

        System.out.print("Apellido: ");
        String apellido = teclado.nextLine().trim();

        System.out.print("Teléfono: ");
        String telefono = teclado.nextLine().trim();

        System.out.print("DNI: ");
        int dni = Integer.parseInt(teclado.nextLine());

        if (nombre.isEmpty() || apellido.isEmpty()
                || telefono.isEmpty() || dni <= 0) {
            System.out.println("Datos incorrectos.");
            return;
        }

        Cliente cliente = sistema.registrarCliente(
                nombre, apellido, telefono, dni);

        if (cliente != null) {
            System.out.println("Cliente registrado correctamente.");
        }
    }

    private void listarClientes() {
        ArrayList<Cliente> clientes = sistema.getClientes();

        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados.");
            return;
        }

        for (Cliente c : clientes) {
            System.out.println(c.getNombreCompleto()
                    + " | DNI: " + c.getDni()
                    + " | Teléfono: " + c.getTelefono());
        }
    }

    private void registrarCancha() {
        System.out.print("Número de cancha: ");
        int numero = Integer.parseInt(teclado.nextLine());

        System.out.print("Tipo (1 PARED / 2 BLINDEX): ");
        int tipo = Integer.parseInt(teclado.nextLine());

        TipoCancha tipoCancha;

        if (tipo == 1) {
            tipoCancha = TipoCancha.PARED;
        } else if (tipo == 2) {
            tipoCancha = TipoCancha.BLINDEX;
        } else {
            System.out.println("Tipo incorrecto.");
            return;
        }

        Cancha cancha = sistema.registrarCancha(
                numero, tipoCancha, true);

        if (cancha != null) {
            System.out.println("Cancha registrada: " + cancha);
        }
    }

    private void consultarDisponibilidad() {
        System.out.print("Fecha (AAAA-MM-DD): ");
        LocalDate fecha = LocalDate.parse(teclado.nextLine());

        System.out.print("Hora (HH:MM): ");
        LocalTime hora = LocalTime.parse(teclado.nextLine());

        ArrayList<Cancha> disponibles =
                sistema.consultarDisponibilidad(fecha, hora);

        if (disponibles.isEmpty()) {
            System.out.println("No hay canchas disponibles.");
        } else {
            System.out.println("Canchas disponibles:");

            for (Cancha c : disponibles) {
                System.out.println(c);
            }
        }
    }

    private void registrarReserva() {
        System.out.print("DNI del cliente: ");
        int dni = Integer.parseInt(teclado.nextLine());

        Cliente cliente = sistema.buscarClientePorDni(dni);

        if (cliente == null) {
            System.out.println("No existe ese cliente. Registralo primero.");
            return;
        }

        System.out.print("Número de cancha: ");
        int numero = Integer.parseInt(teclado.nextLine());

        Cancha cancha = sistema.buscarCanchaPorNumero(numero);

        if (cancha == null) {
            System.out.println("No existe esa cancha.");
            return;
        }

        System.out.print("Fecha (AAAA-MM-DD): ");
        LocalDate fecha = LocalDate.parse(teclado.nextLine());

        System.out.print("Hora (HH:MM): ");
        LocalTime hora = LocalTime.parse(teclado.nextLine());

        MedioPago medio = pedirMedioPago();

        Reserva reserva = sistema.registrarReserva(
                cliente, cancha, fecha, hora, medio);

        if (reserva != null) {
            System.out.println("Reserva confirmada.");
            System.out.println("Número de reserva: " + reserva.getNumero());
        }
    }

    private void consultarReservas() {
        System.out.print("DNI del cliente: ");
        int dni = Integer.parseInt(teclado.nextLine());

        Cliente cliente = sistema.buscarClientePorDni(dni);

        if (cliente == null) {
            System.out.println("No existe ese cliente.");
            return;
        }

        System.out.println("Reservas de "
                + cliente.getNombreCompleto() + ":");

        sistema.consultarReservas(cliente);
    }

    private void cancelarReserva() {
        System.out.print("Número de reserva: ");
        int numero = Integer.parseInt(teclado.nextLine());

        Reserva reserva = sistema.buscarReservaPorNumero(numero);

        if (reserva == null) {
            System.out.println("No se encontró esa reserva.");
            return;
        }

        sistema.cancelarReserva(reserva, LocalDateTime.now());

        System.out.println("Estado actual: " + reserva.getEstado());
    }

    private void registrarAsistencia() {
        System.out.print("Número de reserva: ");
        int numero = Integer.parseInt(teclado.nextLine());

        Reserva reserva = sistema.buscarReservaPorNumero(numero);

        if (reserva == null) {
            System.out.println("No se encontró esa reserva.");
            return;
        }

        System.out.print("¿Asistió? (S/N): ");
        String respuesta = teclado.nextLine();

        if (!respuesta.equalsIgnoreCase("S")
                && !respuesta.equalsIgnoreCase("N")) {
            System.out.println("Ingresá S o N.");
            return;
        }

        boolean asistio = respuesta.equalsIgnoreCase("S");

        MedioPago medio = null;

        if (asistio) {
            medio = pedirMedioPago();
        }

        sistema.registrarAsistencia(reserva, asistio, medio);

        System.out.println("Estado actual: " + reserva.getEstado());
    }

    private void modificarPrecio() {
        if (empleadoActual == null
                || empleadoActual.getRol() != RolEmpleado.ENCARGADO) {
            System.out.println("Solo el encargado puede modificar precios.");
            return;
        }

        System.out.print("Tipo de cancha (1 PARED / 2 BLINDEX): ");
        int tipo = Integer.parseInt(teclado.nextLine());

        TipoCancha tipoCancha;

        if (tipo == 1) {
            tipoCancha = TipoCancha.PARED;
        } else if (tipo == 2) {
            tipoCancha = TipoCancha.BLINDEX;
        } else {
            System.out.println("Tipo incorrecto.");
            return;
        }

        System.out.print("Nuevo precio: ");
        double precio = Double.parseDouble(teclado.nextLine());

        if (precio <= 0) {
            System.out.println("El precio debe ser mayor a cero.");
            return;
        }

        sistema.modificarPrecio(empleadoActual, tipoCancha, precio);

        System.out.println("Nuevo precio: $" + tipoCancha.getPrecio());
    }

    private void listarCanchas() {
        ArrayList<Cancha> canchas = sistema.getCanchas();

        if (canchas.isEmpty()) {
            System.out.println("No hay canchas registradas.");
            return;
        }

        for (Cancha c : canchas) {
            System.out.println(c
                    + " | Precio: $" + c.getPrecio()
                    + " | Disponible: " + c.getDisponibilidad());
        }
    }

    private MedioPago pedirMedioPago() {
        System.out.println("Medio de pago:");
        System.out.println("1. EFECTIVO");
        System.out.println("2. TRANSFERENCIA");
        System.out.println("3. QR");

        int opcion = Integer.parseInt(teclado.nextLine());

        if (opcion == 1) return MedioPago.EFECTIVO;
        if (opcion == 2) return MedioPago.TRANSFERENCIA;
        if (opcion == 3) return MedioPago.QR;

        throw new IllegalArgumentException("Medio de pago incorrecto.");
    }
}