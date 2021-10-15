package com.allan.springBootBoard.security.config;

import com.allan.springBootBoard.security.filter.CustomAuthenticationFilter;
import com.allan.springBootBoard.security.handler.CustomLoginFailHandler;
import com.allan.springBootBoard.security.handler.CustomLoginSuccessHandler;
import com.allan.springBootBoard.security.oauth2.CustomOAuth2UserService;
import com.allan.springBootBoard.web.member.domain.MemberRole;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.CharacterEncodingFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig  {

    @Order(2)
    @Configuration
    @RequiredArgsConstructor
    public class UserSecurityConfig extends WebSecurityConfigurerAdapter{

        @NonNull
        CustomOAuth2UserService customOAuth2UserService;

        @NonNull
        BCryptPasswordEncoder passwordEncoder;

        // 정적 자원 security 설정 미적용.
        @Override
        public void configure(WebSecurity web) throws Exception {
            web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {

            // 한글 깨짐 현상을 위한 인코딩 필터
            // spring security을 사용 할 때는 encordingFilter 가 csrfFilter 앞에 위치해야 한다.
            CharacterEncodingFilter filter = new CharacterEncodingFilter();
            filter.setEncoding("UTF-8");
            filter.setForceEncoding(true);

            http
                .authorizeRequests()
                // board 요청에 대해서는 로그인을 인증을 요구함(로그인을 요구함)
                .antMatchers("/board/*").authenticated()
                    .anyRequest().permitAll()
                // 나머지 요청들에 대해서는 인증을 요구하지 않음(로그인을 요구하지 않음)
                //.anyRequest().permitAll()
                .and()
                    // 로그인시 설정
                    .formLogin()
                        // 로그인 페이지를 제공하는 URL 설정
                        .loginPage("/serviceLogin/loginForm")
                .and()
                    .logout()
                        .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                            .logoutSuccessUrl("/serviceLogin/loginForm")
                .and()
                // 인증 커스텀 필터 설정
                .addFilterBefore(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                // 인코딩 필터를 CSRF필터 앞에 배치시켜주어야 함.
                .addFilterBefore(filter, CsrfFilter.class);
        }

        @Bean
        public CustomAuthenticationFilter customAuthenticationFilter() throws Exception {
            CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager());
            customAuthenticationFilter.setFilterProcessesUrl("/user/login");
            customAuthenticationFilter.setAuthenticationSuccessHandler(customLoginSuccessHandler());
            customAuthenticationFilter.setAuthenticationFailureHandler(customLoginFailHandler());
            customAuthenticationFilter.afterPropertiesSet();
            return customAuthenticationFilter;
        }

        @Bean
        public CustomLoginSuccessHandler customLoginSuccessHandler(){
            return new CustomLoginSuccessHandler();
        }

        @Bean
        public CustomLoginFailHandler customLoginFailHandler() {
            return new CustomLoginFailHandler();
        }

        @Bean
        public CustomAuthenticationProvider customAuthenticationProvider() {
            return new CustomAuthenticationProvider(passwordEncoder);
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.authenticationProvider(customAuthenticationProvider());
        }

    }

    @Order(1)
    @Configuration
    @RequiredArgsConstructor
    public class Oauth2SecurityConfig extends WebSecurityConfigurerAdapter{
        @NonNull
        CustomOAuth2UserService customOAuth2UserService;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
//                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                .antMatchers("/", "/css/**", "/images/**",
                        "/js/**", "/h2-console/**").permitAll()
//                .antMatchers("/serviceLogin/loginForm").permitAll()
                .antMatchers("/board/*").authenticated()
                    .anyRequest().permitAll()
                .and()
                    .formLogin()
                    // 로그인 페이지를 제공하는 URL 설정
                    .loginPage("/serviceLogin/loginForm")
                .and()
                    .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                    .logoutSuccessUrl("/board/getBoardList")
                .and()
                    .oauth2Login()
                        .userInfoEndpoint()
                            .userService(customOAuth2UserService);
        }
    }

}
