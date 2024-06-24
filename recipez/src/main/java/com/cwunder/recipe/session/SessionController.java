package com.cwunder.recipe.session;

// Java SE
import java.time.Instant;
import java.util.HashMap;

// DI
import org.springframework.beans.factory.annotation.Autowired;

// Annotations
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

// Http
import org.springframework.http.ResponseEntity;

// Security
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

@Controller
@RequestMapping("/session")
public class SessionController {
    @Autowired
    JwtEncoder jwtEnc;

    @PostMapping()
    public @ResponseBody ResponseEntity<?> createSession(Authentication authentication) {
        Instant now = Instant.now();
        long expiry = 36000L;
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .subject(authentication.getName())
                .build();
        var rspData = new HashMap<String, String>();
        var token = jwtEnc.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        rspData.put("token", token);
        return ResponseEntity.ok().body(rspData);
    }
}
