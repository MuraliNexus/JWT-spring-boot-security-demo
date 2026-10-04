package com.example.JWT.springBoot.easy.Filters;


import com.example.JWT.springBoot.easy.CustomUserdetailsService.CustomUserDetailsService;
import com.example.JWT.springBoot.easy.Util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component                        // to convert into filter;
public class JwtAuthFilter extends OncePerRequestFilter {

    private  final JWTUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtAuthFilter(JWTUtil jwtUtil, CustomUserDetailsService customUserDetailsService) {
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //TODO we extract token from user request;
        String authHeadaer = request.getHeader("Authorization");
           String token = null;
           String username = null;
        if(authHeadaer!=null && authHeadaer.startsWith("Bearer ")){
             token = authHeadaer.substring(7);
             username = jwtUtil.extracUserNamefromToken(token);
        }
        //TODO validate and set to spring context
        if(username !=null && SecurityContextHolder.getContext().getAuthentication()==null){

           UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

           if(jwtUtil.validateToken(username,userDetails,token)){
               // todo new token generated;
              UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
               authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
              SecurityContextHolder.getContext().setAuthentication(authToken);
           }
        }
        filterChain.doFilter(request,response);
    }
}
