package com.xmoker.auth.security;

import com.xmoker.auth.util.JwtUtil;
import com.xmoker.user.entity.Usuario;
import com.xmoker.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public JwtFilter(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {


        System.out.println("🧪 JwtFilter se está ejecutando...");
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        String email = jwtUtil.extractEmail(token);

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            Usuario usuario = userRepository.findByEmail(email).orElse(null);

            if (usuario != null && jwtUtil.isTokenValid(token)) {
                // Forzar carga completa (opcional si usas @Transactional en el repo)
                usuario.getId();
                usuario.getNombre();
                usuario.getEmail();

                System.out.println("🛡️ Usuario autenticado por JWT:");
                System.out.println("➡️ Email: " + usuario.getEmail());
                System.out.println("➡️ ID: " + usuario.getId());
                System.out.println("➡️ Nombre: " + usuario.getNombre());

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(usuario, null, Collections.emptyList());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
             else {
                System.out.println("❌ Usuario no encontrado o token inválido");
            }
        }

        filterChain.doFilter(request, response);
    }
}
