/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proxy;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

/**
 *
 * @author DELL
 */
public class GoogleProxy {

    private static final String CLIENT_ID = "1234567890-abcde12345.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "GOCSPX-abcdef1234567890";
    private static final String REDIRECT_URI = "http://localhost:8080/HolaTech/callback";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private static final GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
            new NetHttpTransport(), JSON_FACTORY, CLIENT_ID, CLIENT_SECRET,
            Collections.singleton("email profile"))
            .setAccessType("offline")
            .build();

    // Tạo URL để điều hướng người dùng đến Google Login
    public static String getLoginUrl() {
        return flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URI).build();
    }

    // Xác thực mã từ Google và lấy thông tin người dùng
    public static GoogleIdToken.Payload getUserInfo(String code) throws IOException, GeneralSecurityException {
        try {
            GoogleTokenResponse credential = flow.newTokenRequest(code)
                    .setRedirectUri(REDIRECT_URI)
                    .execute() /*.toCredential()*/;

            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), JSON_FACTORY)
                    .setAudience(Collections.singletonList(CLIENT_ID))
                    .build();

            GoogleIdToken idToken = verifier.verify(credential.getAccessToken());
            if (idToken != null) {
                return idToken.getPayload();
            }
        } catch (TokenResponseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
