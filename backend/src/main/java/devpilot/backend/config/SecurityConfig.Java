@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final GithubOAuth2UserService githubOAuth2UserService;
   

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http,
        AuthenticationSuccessHandler oauth2AuthSuccessHandler,
        AuthenticationFailureHandler oAuth2AuthenticationFailureHandler
    ) throws Exception {

        http
            .cors(Customizer.withDefaults())

            .csrf(csrf -> csrf.disable())

            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            )

            .authorizeHttpRequests(auth -> auth

                .requestMatchers(
                    "/api/auth/login-url",
                    "/oauth2/**",
                    "/login/oauth2/**",
                    "/error"
                ).permitAll()

                .requestMatchers(HttpMethod.OPTIONS, "/**")
                .permitAll()

                .requestMatchers("/api/**")
                .authenticated()

                .anyRequest()
                .permitAll()
            )

            .exceptionHandling(exception ->
                exception.authenticationEntryPoint(
                    new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)
                )
            )

            .oauth2Login(oauth2 -> oauth2

                .userInfoEndpoint(userInfo ->
                    userInfo.userService(githubOAuth2UserService)
                )

                .successHandler(oauth2AuthSuccessHandler)

                .failureHandler(oAuth2AuthenticationFailureHandler)
            )

            .logout(logout -> logout

                .logoutUrl("/api/auth/logout")

                .logoutSuccessHandler((request, response, authentication) ->
                    response.setStatus(HttpStatus.NO_CONTENT.value())
                )

                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("DEVPILOT_SESSION")
            );

        return http.build();
    }

    @Bean
    AuthenticationSuccessHandler oauth2AuthSuccessHandler(
            @Value("${app.frontend-url}") String frontendUrl
    ) {
        SimpleUrlAuthenticationSuccessHandler handler =
                new SimpleUrlAuthenticationSuccessHandler();

        handler.setDefaultTargetUrl(
                frontendUrl + "/auth/callback"
        );

        return handler;
    }

    @Bean
    AuthenticationFailureHandler oAuth2AuthenticationFailureHandler(
            @Value("${app.frontend-url}") String frontendUrl
    ) {
        SimpleUrlAuthenticationFailureHandler handler =
                new SimpleUrlAuthenticationFailureHandler();

        handler.setDefaultTargetUrl(
                frontendUrl + "/login?error=oauth_Failed"
        );

        return handler;
    }
}