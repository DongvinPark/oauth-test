package com.example.oauthtest.config;

//import com.example.oauthtest.jwt.JwtAuthenticationFilter;
//import com.example.oauthtest.oauth.google.CustomLoginSuccessHandler;
//import com.example.oauthtest.oauth.google.CustomLoginSuccessHandler;
//import com.example.oauthtest.jwt.JwtAuthenticationFilter;
import com.example.oauthtest.oauth.google.CustomLoginSuccessHandler;
import com.example.oauthtest.oauth.google.CustomOAuth2UserService;
import com.example.oauthtest.oauth.google.CustomOIDCUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

  //private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final CustomOAuth2UserService customOAuth2UserService;
  private final CustomOIDCUserService customOIDCUserService;
  private final CustomLoginSuccessHandler customLoginSuccessHandler;

  @Bean
  public PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http
        .cors().disable()
        .csrf().disable()
        .httpBasic().disable()
        .formLogin().disable()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .antMatcher("/**")
        .authorizeRequests()
        .antMatchers(
//            "/content/*", "/admin-jwt", "/mobile-jwt",
//
//            //swagger를 spring security를 사용하면서도 접속가능하게 해주기 위한 url 설정.
//            "/swagger-resources/**", "/swagger-ui/**",
//            "/v3/api-docs", "/swagger*/**", "/categories"
            "/**"
        )
        .permitAll()//회원가입과 로그인은 인증이 없어야 한다.
        .anyRequest()
        .authenticated()
        //.and().logout().logoutSuccessUrl("/")
        .and()
        .oauth2Login()
        .successHandler(customLoginSuccessHandler)
        .userInfoEndpoint()
        .userService(customOAuth2UserService)
        .oidcUserService(customOIDCUserService);

    // cors 필터 다음에 JWT 필터를 넣어줌.
    //http.addFilterAfter(jwtAuthenticationFilter, CorsFilter.class);

    return http.build();

  }
}
