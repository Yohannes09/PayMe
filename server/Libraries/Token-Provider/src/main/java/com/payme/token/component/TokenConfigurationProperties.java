package com.payme.token.component;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "token")
@Getter
@Setter
@AllArgsConstructor
public class TokenConfigurationProperties {
    private Signing signing;
    private Encoding encoding;
    private Validation validation;
    private DefaultClaims defaultClaims;
    private Map<String, TokenTemplate> templates;
    private Map<String, Map<String, TokenTemplateReference>> profiles;


    @Getter
    @Setter
    public static class Signing{
        private String algorithm;
        private int keySizeBits;
        private String keyId;
        private int rotationIntervalMinutes;
    }

    @Getter
    @Setter
    public static class Encoding{
        private String type;
        private boolean compress;
    }

    @Getter
    @Setter
    public static class Validation{
        private int clockSkewSeconds;
    }

    @Getter
    @Setter
    public static class DefaultClaims{
        private String audience;
        private String issuer;
    }

    @Getter
    @Setter
    public static class TokenTemplate{
        private int validityMinutes;
    }

    @Getter
    @Setter
    public static class TokenTemplateReference{
        private String template;
    }

    @PostConstruct
    public void validateProfiles(){
        boolean containsInvalidReference;

        if(!profiles.isEmpty()){
             containsInvalidReference = profiles.entrySet().stream()
                    .flatMap(profileEntry -> profileEntry.getValue().values().stream())
                    .anyMatch(reference -> !templates.containsKey(reference.getTemplate()));

            if (containsInvalidReference) {
                throw new IllegalStateException("Entered invalid reference within the profiles. ");
            }
        }

    }

}
