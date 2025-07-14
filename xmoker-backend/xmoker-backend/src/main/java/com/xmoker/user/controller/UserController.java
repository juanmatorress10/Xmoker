package com.xmoker.user.controller;

import com.xmoker.user.dto.NivelUsuarioDTO;
import com.xmoker.user.dto.UsuarioDTO;
import com.xmoker.user.entity.Usuario;
import com.xmoker.user.repository.UserRepository;
import com.xmoker.user.security.JwtService;
import com.xmoker.user.service.NivelService;
import com.xmoker.user.service.UserService;
import com.xmoker.user.dto.UsuarioUpdateDTO;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UserService userService;
    private final NivelService nivelService;

    public UserController(UserRepository userRepository, JwtService jwtService, UserService userService,NivelService nivelService ) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.userService = userService;
        this.nivelService = nivelService;
    }

    // 🔐 Obtener usuario autenticado
    @GetMapping("/me")
    public ResponseEntity<UsuarioDTO> obtenerUsuarioActual(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().build();
        }

        String token = authorizationHeader.substring(7);
        String email = jwtService.extractUsername(token);

        Usuario usuario = userRepository.findByEmail(email).orElse(null);
        if (usuario != null) {
            int nivel = nivelService.calcularNivel(usuario.getExperiencia());
            double porcentaje = nivelService.porcentajeProgreso(usuario);

            UsuarioDTO dto = new UsuarioDTO(
                    usuario.getId(),
                    usuario.getNombre(),
                    usuario.getEmail(),
                    usuario.getEstiloFumador(),
                    usuario.getDescripcionEstiloFumador(),
                    usuario.getFotoPerfilUrl(),
                    nivel,
                    usuario.getExperiencia(),
                    porcentaje
            );

            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }




    // ✅ Registro de nuevo usuario
    @PostMapping
    public ResponseEntity<Usuario> registrar(@RequestBody Usuario usuario) {
        try {
            Usuario nuevo = userService.registrar(usuario);
            nuevo.setPassword(null); // ocultar hash
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 🔄 Actualizar datos del usuario
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(
            @PathVariable Long id,
            @RequestBody UsuarioUpdateDTO dto) {

        return userRepository.findById(id).map(usuario -> {
            if (dto.getNombre() != null) {
                usuario.setNombre(dto.getNombre());
            }
            if (dto.getEmail() != null) {
                usuario.setEmail(dto.getEmail());
            }
            if (dto.getFotoPerfilUrl() != null) {
                usuario.setFotoPerfilUrl(dto.getFotoPerfilUrl());
            }
            if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
                usuario.setPassword(
                        new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode(dto.getPassword())
                );
            }

            Usuario actualizado = userRepository.save(usuario);
            actualizado.setPassword(null);
            return ResponseEntity.ok(actualizado);
        }).orElse(ResponseEntity.notFound().build());
    }

    // 📸 Subida de imagen de perfil
    @PostMapping("/{id}/foto")
    public ResponseEntity<String> subirFoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            String nombreArchivo = StringUtils.cleanPath(file.getOriginalFilename());

            String rutaBase = new File("target/classes/static/images").getAbsolutePath();
            Files.createDirectories(Paths.get(rutaBase));

            Path rutaImagen = Paths.get(rutaBase, nombreArchivo);
            Files.copy(file.getInputStream(), rutaImagen, StandardCopyOption.REPLACE_EXISTING);

            return ResponseEntity.ok("images/" + nombreArchivo);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al subir imagen");
        }
    }

    @GetMapping("/nivel/{usuarioId}")
    public ResponseEntity<NivelUsuarioDTO> obtenerNivelUsuario(@PathVariable Long usuarioId) {
        Usuario usuario = userRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        int nivel = nivelService.calcularNivel(usuario.getExperiencia());
        double progreso = nivelService.porcentajeProgreso(usuario);

        NivelUsuarioDTO dto = new NivelUsuarioDTO(nivel, usuario.getExperiencia(), progreso);
        return ResponseEntity.ok(dto);
    }

}
