package com.example.demo.test;

import com.example.demo.controller.UsuarioController;
import com.example.demo.models.Usuario;
import com.example.demo.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UsuarioTest {

	private UsuarioRepository usuarioRepository;
	private UsuarioController usuarioController;

	@BeforeEach
	public void setup() {
		usuarioRepository = new UsuarioRepositoryStub();
		usuarioController = new UsuarioController(usuarioRepository);
	}

	@Test
	public void testCreateUsuario() {
		// Configuración
		Usuario usuario = new Usuario();
		usuario.setNombre("John");
		usuario.setCorreo("john@example.com");
		usuario.setContrasena("Password123");
		usuario.setTelefonos("123456789");
		usuario.setCodigoCiudad("CDMX");
		usuario.setCodigoPais("MX");

		// Ejecución
		ResponseEntity<?> response = usuarioController.createUsuario(usuario);
		Usuario createdUsuario = (Usuario) response.getBody();

		// Aserciones
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(createdUsuario);
		assertNotNull(createdUsuario.getId());
		assertEquals("John", createdUsuario.getNombre());
		assertEquals("john@example.com", createdUsuario.getCorreo());
		assertEquals("Password123", createdUsuario.getContrasena());
		assertEquals("123456789", createdUsuario.getTelefonos());
		assertEquals("CDMX", createdUsuario.getCodigoCiudad());
		assertEquals("MX", createdUsuario.getCodigoPais());
		assertNotNull(createdUsuario.getCreated());
		assertNotNull(createdUsuario.getModified());
		assertNotNull(createdUsuario.getLastLogin());
		assertTrue(createdUsuario.getIsActive());

		// Verificar que el usuario se haya guardado en el repositorio
		List<Usuario> usuarios = usuarioRepository.findAll();
		assertEquals(1, usuarios.size());
		Usuario savedUsuario = usuarios.get(0);
		assertEquals(createdUsuario.getId(), savedUsuario.getId());
		assertEquals(createdUsuario.getNombre(), savedUsuario.getNombre());
		assertEquals(createdUsuario.getCorreo(), savedUsuario.getCorreo());
		assertEquals(createdUsuario.getContrasena(), savedUsuario.getContrasena());
		assertEquals(createdUsuario.getTelefonos(), savedUsuario.getTelefonos());
		assertEquals(createdUsuario.getCodigoCiudad(), savedUsuario.getCodigoCiudad());
		assertEquals(createdUsuario.getCodigoPais(), savedUsuario.getCodigoPais());
		assertEquals(createdUsuario.getCreated(), savedUsuario.getCreated());
		assertEquals(createdUsuario.getModified(), savedUsuario.getModified());
		assertEquals(createdUsuario.getLastLogin(), savedUsuario.getLastLogin());
		assertEquals(createdUsuario.getIsActive(), savedUsuario.getIsActive());
	}

	@Test
	public void testGetUsuarioById() {
		// Configuración
		Usuario usuario = new Usuario();
		UUID usuarioId = UUID.randomUUID();
		usuario.setId(usuarioId);
		usuario.setNombre("John");
		usuario.setCorreo("john@example.com");
		usuario.setContrasena("Password123");
		usuario.setTelefonos("123456789");
		usuario.setCodigoCiudad("CDMX");
		usuario.setCodigoPais("MX");
		usuario.setCreated(LocalDateTime.now());
		usuario.setModified(LocalDateTime.now());
		usuario.setLastLogin(LocalDateTime.now());
		usuario.setIsActive(true);

		usuarioRepository.save(usuario);

		// Ejecución
		ResponseEntity<Usuario> getResponse = usuarioController.getUsuarioById(usuarioId);
		Usuario retrievedUsuario = getResponse.getBody();

		// Aserciones
		assertEquals(HttpStatus.OK, getResponse.getStatusCode());
		assertNotNull(retrievedUsuario);
		assertEquals(usuario.getId(), retrievedUsuario.getId());
		assertEquals(usuario.getNombre(), retrievedUsuario.getNombre());
		assertEquals(usuario.getCorreo(), retrievedUsuario.getCorreo());
		assertEquals(usuario.getContrasena(), retrievedUsuario.getContrasena());
		assertEquals(usuario.getTelefonos(), retrievedUsuario.getTelefonos());
		assertEquals(usuario.getCodigoCiudad(), retrievedUsuario.getCodigoCiudad());
		assertEquals(usuario.getCodigoPais(), retrievedUsuario.getCodigoPais());
		assertEquals(usuario.getCreated(), retrievedUsuario.getCreated());
		assertEquals(usuario.getModified(), retrievedUsuario.getModified());
		assertEquals(usuario.getLastLogin(), retrievedUsuario.getLastLogin());
		assertEquals(usuario.getIsActive(), retrievedUsuario.getIsActive());
	}

	private static class UsuarioRepositoryStub implements UsuarioRepository {
		private List<Usuario> usuarios = new ArrayList<>();

		@Override
		public List<Usuario> findAll() {
			return usuarios;
		}

		@Override
		public Optional<Usuario> findById(UUID id) {
			return usuarios.stream().filter(u -> u.getId().equals(id)).findFirst();
		}

		@Override
		public Optional<Usuario> findByCorreo(String correo) {
			return usuarios.stream().filter(u -> u.getCorreo().equals(correo)).findFirst();
		}

		@Override
		public <S extends Usuario> S save(S usuario) {
			usuario.setId(UUID.randomUUID()); // Generar un nuevo ID
			usuarios.add(usuario);
			return (S) usuario;
		}

		// Implementa los demás métodos de UsuarioRepository si es necesario para tus
		// pruebas

		@Override
		public Usuario getReferenceById(UUID id) {
			return usuarios.stream().filter(u -> u.getId().equals(id)).findFirst().orElse(null);
		}

		@Override
		public List<Usuario> findAll(Sort sort) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<Usuario> findAllById(Iterable<UUID> ids) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public <S extends Usuario> List<S> saveAll(Iterable<S> entities) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void flush() {
			// TODO Auto-generated method stub

		}

		@Override
		public <S extends Usuario> S saveAndFlush(S entity) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public <S extends Usuario> List<S> saveAllAndFlush(Iterable<S> entities) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void deleteAllInBatch(Iterable<Usuario> entities) {
			// TODO Auto-generated method stub

		}

		@Override
		public void deleteAllByIdInBatch(Iterable<UUID> ids) {
			// TODO Auto-generated method stub

		}

		@Override
		public void deleteAllInBatch() {
			// TODO Auto-generated method stub

		}

		@Override
		public Usuario getOne(UUID id) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Usuario getById(UUID id) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public <S extends Usuario> List<S> findAll(Example<S> example) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public <S extends Usuario> List<S> findAll(Example<S> example, Sort sort) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Page<Usuario> findAll(Pageable pageable) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean existsById(UUID id) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public long count() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void deleteById(UUID id) {
			// TODO Auto-generated method stub

		}

		@Override
		public void delete(Usuario entity) {
			// TODO Auto-generated method stub

		}

		@Override
		public void deleteAllById(Iterable<? extends UUID> ids) {
			// TODO Auto-generated method stub

		}

		@Override
		public void deleteAll(Iterable<? extends Usuario> entities) {
			// TODO Auto-generated method stub

		}

		@Override
		public void deleteAll() {
			// TODO Auto-generated method stub

		}

		@Override
		public <S extends Usuario> Optional<S> findOne(Example<S> example) {
			// TODO Auto-generated method stub
			return Optional.empty();
		}

		@Override
		public <S extends Usuario> Page<S> findAll(Example<S> example, Pageable pageable) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public <S extends Usuario> long count(Example<S> example) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public <S extends Usuario> boolean exists(Example<S> example) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public <S extends Usuario, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
			// TODO Auto-generated method stub
			return null;
		}
	}

}
