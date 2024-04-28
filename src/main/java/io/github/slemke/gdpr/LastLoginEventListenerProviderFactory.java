package io.github.slemke.gdpr;

import java.time.format.DateTimeFormatter;
import java.util.List;
import org.keycloak.Config.Scope;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventListenerProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;

public class LastLoginEventListenerProviderFactory implements EventListenerProviderFactory {

    public static final String ID = "last-login";

    private String attribute;

    private String zoneId;

    private DateTimeFormatter dateTimePattern;

    @Override
    public EventListenerProvider create(KeycloakSession session) {
        return new LastLoginEventListenerProvider(
            session, 
            this.zoneId,
            this.dateTimePattern,
            this.attribute
        );
    }

    @Override
    public void init(Scope config) {
        String attribute = config.get("attribute");
        if(attribute == null || attribute.isBlank()) {
            this.attribute = "lastLogin";
        } else {
            this.attribute = attribute;
        }
        
        String zoneId = config.get("zone-id");
        if(zoneId == null || zoneId.isBlank()) {
            this.zoneId = "UTC";
        } else {
            this.zoneId = zoneId;
        }

        String pattern = config.get("pattern");
        if(pattern == null || pattern.isBlank()) {
            this.dateTimePattern = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        } else {
            this.dateTimePattern = DateTimeFormatter.ofPattern(pattern);
        }
    }

    @Override
    public void postInit(KeycloakSessionFactory factory) { }

    @Override
    public void close() { }

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public List<ProviderConfigProperty> getConfigMetadata() {
        return ProviderConfigurationBuilder.create()
                .property()
                .name("attribute")
                .type("string")
                .helpText("The name of the last login user attribute (default ist lastLogin)")
                .defaultValue("lastLogin")
                .add()
                .property()
                .name("zone-id")
                .type("string")
                .helpText("The zone id for the last login format (default is UTC)")
                .defaultValue("UTC")
                .add()
                .property()
                .name("pattern")
                .type("string")
                .helpText("Custom date time format pattern (default is the ISO date time format)")
                .add()
                .build();
    }
}
