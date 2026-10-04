package com.example.JWT.springBoot.easy.Controller;


import com.example.JWT.springBoot.easy.Entity.AuthRequest;
import com.example.JWT.springBoot.easy.Util.JWTUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

//to generate token
@RestController
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/authenticate")
    public String generateToken(@RequestBody AuthRequest authRequest){

        try{
        //it needs two name,password
         authenticationManager.authenticate(
                 new UsernamePasswordAuthenticationToken(authRequest.getName(),authRequest.getPassword())
         );
        } catch (Exception e) {
            throw e;
        }
        //TODO JWT TOKEN
        return jwtUtil.generateToken(authRequest.getName());
    }
}
