package com.example.demo.models;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Length(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    private String nombre;

    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "El correo debe tener el formato correcto (aaaaaaa@dominio.cl)")
    @Column(unique = true, nullable = false)
    private String correo;

    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d.*\\d).{6,}$", message = "La contraseña debe contener al menos una mayúscula, letras minúsculas y dos números")
    @Column(nullable = false)
    private String contrasena;

    @Column(name = "telefonos", nullable = false)
    @Size(max = 9)
    private String telefonos;

    @Column(length = 50, nullable = false)
    private String codigoCiudad;

    @Column(length = 50, nullable = false)
    private String codigoPais;

    private LocalDateTime created;

    private LocalDateTime modified;
    private LocalDateTime lastLogin;

    @Column(nullable = false)
    private boolean isActive;

    @Column(nullable = false)
    private String token;

    public Usuario() {
        // Constructor vacío requerido por JPA
    }

    public Usuario(UUID id, String nombre, String correo, String contrasena, String telefonos, String codigoCiudad,
                   String codigoPais, LocalDateTime created, LocalDateTime modified, LocalDateTime lastLogin,
                   boolean isActive) {
        this.id = id != null ? id : UUID.randomUUID();
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
        this.telefonos = telefonos;
        this.codigoCiudad = codigoCiudad;
        this.codigoPais = codigoPais;
        this.created = created != null ? created : LocalDateTime.now();
        this.modified = modified;
        this.lastLogin = lastLogin;
        this.isActive = isActive;
    }

    // Getters y setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getTelefonos() {
        return telefonos;
    }

    public void setTelefonos(String telefonos) {
        this.telefonos = telefonos;
    }

    public String getCodigoCiudad() {
        return codigoCiudad;
    }

    public void setCodigoCiudad(String codigoCiudad) {
        this.codigoCiudad = codigoCiudad;
    }

    public String getCodigoPais() {
        return codigoPais;
    }

    public void setCodigoPais(String codigoPais) {
        this.codigoPais = codigoPais;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getModified() {
        return modified;
    }

    public void setModified(LocalDateTime modified) {
        this.modified = modified;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

