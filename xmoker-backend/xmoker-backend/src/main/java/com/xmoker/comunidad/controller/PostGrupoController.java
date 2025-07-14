package com.xmoker.comunidad.controller;

import com.xmoker.comunidad.dto.PostGrupoDTO;
import com.xmoker.comunidad.entity.GrupoApoyo;
import com.xmoker.comunidad.entity.PostGrupo;
import com.xmoker.comunidad.service.GrupoService;
import com.xmoker.comunidad.service.PostGrupoService;
import com.xmoker.user.security.JwtService;
import com.xmoker.user.entity.Usuario;
import com.xmoker.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/posts-grupo")
public class PostGrupoController {

    @Autowired
    private PostGrupoService postService;

    @Autowired
    private GrupoService grupoService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    // ✅ Crear post con imagen (multipart)
    @PostMapping("/{grupoId}")
    public ResponseEntity<PostGrupoDTO> crearPost(
            @PathVariable Long grupoId,
            @RequestParam("contenido") String contenido,
            @RequestParam(value = "imagen", required = false) MultipartFile imagen,
            @AuthenticationPrincipal Usuario usuario) {

        GrupoApoyo grupo = grupoService.buscarPorId(grupoId);
        PostGrupo post = postService.crearPost(usuario, grupo, contenido, imagen);
        return ResponseEntity.ok(new PostGrupoDTO(post));
    }

    // ✅ Subir imagen
    @PostMapping("/subir-imagen")
    public ResponseEntity<String> subirImagenPost(@RequestParam("archivo") MultipartFile archivo) {
        try {
            String nombreOriginal = archivo.getOriginalFilename();
            String nombreSeguro = nombreOriginal != null
                    ? nombreOriginal.replaceAll("[^a-zA-Z0-9\\.\\-_]", "_")
                    : "imagen.png";

            String nombreArchivo = UUID.randomUUID() + "_" + nombreSeguro;
            String rutaBase = Paths.get("uploads").toAbsolutePath().toString();
            Files.createDirectories(Paths.get(rutaBase));

            Path rutaImagen = Paths.get(rutaBase, nombreArchivo);
            Files.copy(archivo.getInputStream(), rutaImagen, StandardCopyOption.REPLACE_EXISTING);

            return ResponseEntity.ok("/uploads/" + nombreArchivo);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al subir la imagen");
        }
    }

    // ✅ Obtener posts del grupo (con DTO)
    @GetMapping("/{grupoId}")
    public ResponseEntity<List<PostGrupoDTO>> obtenerPosts(@PathVariable Long grupoId) {
        GrupoApoyo grupo = grupoService.buscarPorId(grupoId);
        List<PostGrupoDTO> dtos = postService.obtenerPostsPorGrupo(grupo).stream()
                .map(PostGrupoDTO::new)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    // ✅ Eliminar post
    @DeleteMapping("/eliminar/{postId}")
    public ResponseEntity<?> eliminarPost(
            @PathVariable Long postId,
            @AuthenticationPrincipal Usuario usuario) {
        try {
            postService.eliminarPost(postId, usuario);
            return ResponseEntity.ok("Post eliminado");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el post");
        }
    }

    // ✅ Crear post con imagen por URL (token manual)
    @PostMapping("/{grupoId}/crear-con-url")
    public ResponseEntity<PostGrupoDTO> crearPostConUrl(
            @PathVariable Long grupoId,
            @RequestParam("contenido") String contenido,
            @RequestParam(value = "imagenUrl", required = false) String imagenUrl,
            @RequestHeader("Authorization") String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authHeader.substring(7);
        String email = jwtService.extractUsername(token);

        Usuario usuario = userRepository.findByEmail(email).orElse(null);
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        GrupoApoyo grupo = grupoService.buscarPorId(grupoId);

        PostGrupo post = new PostGrupo();
        post.setContenido(contenido);
        post.setImagenUrl(imagenUrl);
        post.setGrupo(grupo);
        post.setAutor(usuario);

        return ResponseEntity.ok(new PostGrupoDTO(postService.guardar(post)));
    }
}
