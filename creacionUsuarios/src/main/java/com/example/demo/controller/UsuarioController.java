package com.example.demo.controller;

import com.example.demo.models.Usuario;
import com.example.demo.repository.UsuarioRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/Usuario")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable UUID id) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            return ResponseEntity.ok(usuario);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> createUsuario(@Valid @RequestBody Usuario usuario) {
        // Verificar si el correo electrónico ya existe
        Optional<Usuario> optionalUsuario = usuarioRepository.findByCorreo(usuario.getCorreo());
        if (optionalUsuario.isPresent()) {
            // El correo electrónico ya existe en la base de datos, responder con código de
            // error 409 (conflicto)
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El correo electrónico ya está registrado.");
        }

        usuario.setCreated(LocalDateTime.now()); // Establecer la fecha de creación actual
        String token = generateToken(usuario); // Generar el token JWT
        usuario.setToken(token); // Establecer el token en el usuario creado
        usuario.setLastLogin(usuario.getCreated()); // Establecer el último inicio de sesión como la fecha de creación
        usuario.setIsActive(true); // Establecer el usuario como activo

        Usuario createdUsuario = usuarioRepository.save(usuario); // Guardar el usuario en la base de datos

        return ResponseEntity.status(HttpStatus.CREATED).body(createdUsuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable UUID id, @Valid @RequestBody Usuario usuarioDetails) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            usuario.setNombre(usuarioDetails.getNombre());
            usuario.setCorreo(usuarioDetails.getCorreo());
            usuario.setTelefonos(usuarioDetails.getTelefonos()); // Actualizar la lista de teléfonos
            usuario.setModified(LocalDateTime.now()); // Establecer la fecha de modificación actual
            Usuario updatedUsuario = usuarioRepository.save(usuario);
            return ResponseEntity.ok(updatedUsuario);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable UUID id) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
        if (optionalUsuario.isPresent()) {
            usuarioRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private String generateToken(Usuario usuario) {
        String secret = "MiClaveSuperSecreta"; // Clave secreta original
        String hashedSecret;
        try {
            hashedSecret = generateHash(secret); // Generar hash de la clave secreta
        } catch (NoSuchAlgorithmException e) {
            // Manejar la excepción adecuadamente
            return null;
        }

        return Jwts.builder().setSubject(usuario.getCorreo()).claim("id", usuario.getId().toString())
                .signWith(Keys.hmacShaKeyFor(hashedSecret.getBytes()), SignatureAlgorithm.HS256).compact();
    }

    private String generateHash(String input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte hashByte : hashBytes) {
            String hex = Integer.toHexString(0xff & hashByte);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
