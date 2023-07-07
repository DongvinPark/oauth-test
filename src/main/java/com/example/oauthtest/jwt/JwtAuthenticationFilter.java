package com.example.oauthtest.jwt;

import com.example.oauthtest.enums.JwtTypes;
import com.example.oauthtest.jwt.principal.CommonPrincipal;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtProvider jwtProvider;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
  ) throws ServletException, IOException {
    try {
      String token = parseBearerToken(request);

      if (token != null && !token.equalsIgnoreCase("null")) {

        CommonPrincipal principal = jwtProvider.validateAndGetPrincipal(token);

        if (principal.getJwtType().equals(JwtTypes.ADMIN.getValue())) {
          setSecurityContextByAdminId(request, principal.getAdminId());
        } else if (principal.getJwtType().equals(JwtTypes.MOBILE.getValue())) {
          setSecurityContextByUserId(request, principal.getJti());
        }
      }
      filterChain.doFilter(request, response);
    } catch (Exception e) {
      log.error("Could not set user authentication in security context");
      response.setContentType("application/json");
      String json =
          "{"
              + "\n\tmessage : " + "인증되지 않음!!"
              + "\n}";
      response.getWriter().write(json);
    }

  }

  private static void setSecurityContextByAdminId(HttpServletRequest request, Integer adminId) {
    AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
        adminId,
        null,
        AuthorityUtils.NO_AUTHORITIES
    );

    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    securityContext.setAuthentication(authentication);
    SecurityContextHolder.setContext(securityContext);
  }

  private static void setSecurityContextByUserId(HttpServletRequest request, String userId) {
    AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
        userId,
        null,
        AuthorityUtils.NO_AUTHORITIES
    );

    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    securityContext.setAuthentication(authentication);
    SecurityContextHolder.setContext(securityContext);
  }

  private String parseBearerToken(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");

    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }

}
