package modelo;

import java.time.LocalDate;

public class Cliente {
    private String dniCuit;
    private String nombre;
    private String telefono;
    private String email;
    private String direccion;
    private EstadoCliente estado;
    private LocalDate fechaRegistro;

    public Cliente(String dniCuit, String nombre, String telefono, 
                   String email, String direccion) {
        this.dniCuit = dniCuit;
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
        this.estado = EstadoCliente.ACTIVO;
        this.fechaRegistro = LocalDate.now();
    }

    public String getDniCuit() {
        return dniCuit;
    }

    public void setDniCuit(String dniCuit) {
        this.dniCuit = dniCuit;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public EstadoCliente getEstado() {
        return estado;
    }

    public void setEstado(EstadoCliente estado) {
        this.estado = estado;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "dniCuit='" + dniCuit + '\'' +
                ", nombre='" + nombre + '\'' +
                ", estado=" + estado +
                '}';
    }
}