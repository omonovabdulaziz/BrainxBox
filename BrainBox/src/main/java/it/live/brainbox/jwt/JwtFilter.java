package it.live.brainbox.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.live.brainbox.payload.ApiResponse;
import it.live.brainbox.service.impl.UserServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserServiceImpl userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer")) {
                token = token.substring(7);
                String userNameFromToken = jwtProvider.getUsername(token);
                UserDetails userDetails = userService.loadUserByUsername(userNameFromToken);
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
            }
        } catch (Exception e) {
            unAuthorize(response);
        }
        filterChain.doFilter(request, response);
    }

    private void unAuthorize(HttpServletResponse response) throws IOException {
        response.setStatus(401);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper()
                .writeValue(response.getOutputStream(),
                        ApiResponse.builder().message("UnAuth").status(401).build()
                );
    }

}