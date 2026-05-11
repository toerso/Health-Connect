package com.toerso.healthconnect.utility;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class IpExtractor {

    public String extractIp(HttpServletRequest request) {
        // X-Forwarded-For is set by reverse proxies (nginx, AWS ALB)
        // It contains the real client IP
        String forwarded = request.getHeader("X-Forwarded-For");

        if (forwarded != null && !forwarded.isBlank()) {
            // X-Forwarded-For can contain a chain of IPs: "clientIP, proxy1, proxy2"
            // The first one is always the real client
            return forwarded.split(",")[0].trim();
        }

        // Fall back to direct connection IP
        return request.getRemoteAddr();
    }
}
