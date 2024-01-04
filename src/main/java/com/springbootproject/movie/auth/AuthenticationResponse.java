package com.springbootproject.movie.auth;

//import com.sun.tools.javac.parser.JavacParser;
//import com.sun.tools.javac.parser.JavacParser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String token;
}
