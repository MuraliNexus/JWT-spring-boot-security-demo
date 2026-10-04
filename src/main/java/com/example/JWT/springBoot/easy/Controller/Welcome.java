package com.example.JWT.springBoot.easy.Controller;


import com.example.JWT.springBoot.easy.Entity.Users;
import com.example.JWT.springBoot.easy.Repoistory.UserRepoistory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Welcome {

    public final PasswordEncoder passwordEncoder;
    public final UserRepoistory userRepoistory;
    public final AuthenticationManager authenticationManager;

    public Welcome(PasswordEncoder passwordEncoder, UserRepoistory userRepoistory, AuthenticationManager authenticationManager) {
        this.passwordEncoder = passwordEncoder;
        this.userRepoistory = userRepoistory;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/welcome")
    public String welcomemsg(){
        return "welcome to web after security";
    }

    @PostMapping("/sigin")
    public String register(@RequestBody Users user) {

        System.out.println("Incoming username: " + user.getName());
        System.out.println("Repo result: " + userRepoistory.findByName(user.getName()));

        if (userRepoistory.findByName(user.getName()).isPresent()) {
            return "User already exists!";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        userRepoistory.save(user);

        return "User Registered Successfully";
    }

    //@PostMapping("/login")
    public String login(@RequestBody Users user){
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getName(),
                            user.getPassword()
                    )
            );
            if(auth.isAuthenticated()){
                return "login successfully !"+ user.getName();
                // return "redirect:/welcome"
                //or
                // just msg written
            }
            else {
                return "login failed";
            }
        } catch (Exception e){
            return "Invalid username or password";
        }
    }


}
