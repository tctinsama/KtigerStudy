package org.example.ktigerstudybe.service.auth;

import org.example.ktigerstudybe.dto.req.GoogleSignInRequest;
import org.example.ktigerstudybe.dto.req.SignInRequest;
import org.example.ktigerstudybe.dto.req.SignUpRequest;
import org.example.ktigerstudybe.dto.resp.AuthResponse;
import org.example.ktigerstudybe.dto.resp.GoogleSignInResponse;

public interface AuthService {
    AuthResponse signUp(SignUpRequest request);
    AuthResponse signIn(SignInRequest request);
    void forgotPassword(String email);
    void resetPassword(String token, String newPassword);

    GoogleSignInResponse googleSignIn(GoogleSignInRequest request);

}
