package org.example.Controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.auth.AuthResponseDTO;
import org.example.dto.auth.LoginDTO;
import org.example.dto.auth.RegisterDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import org.example.services.AccountService;

@RestController
@RequestMapping("api/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService service;
    private static final String RECAPTCHA_SECRET_KEY = "6LeOnGooAAAAAI-XYC9nM-Xtg-BVSB8P-eJXLzb4";
    private static final String RECAPTCHA_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";
    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterDTO registrationRequest) {
        try {
            service.register(registrationRequest);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginDTO request) {
        try {
            if (verifyRecaptcha(request.getRecaptchaToken())) {
                var auth = service.login(request);
                return ResponseEntity.ok(auth);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    private boolean verifyRecaptcha(String recaptchaToken) {
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("secret", RECAPTCHA_SECRET_KEY);
        requestBody.add("response", recaptchaToken);

        ResponseEntity<Map> response = restTemplate.postForEntity(RECAPTCHA_VERIFY_URL, requestBody, Map.class);

        Map<String, Object> responseBody = response.getBody();
        if (responseBody != null && responseBody.containsKey("success")) {
            boolean success = (boolean) responseBody.get("success");
            return success;
        }

        return false;
    }
}
