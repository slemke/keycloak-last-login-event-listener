package io.github.slemke.gdpr;

import java.time.DateTimeException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.jboss.logging.Logger;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventType;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.UserModel;

public class LastLoginEventListenerProvider implements EventListenerProvider {

    private Logger logger = Logger.getLogger(LastLoginEventListenerProvider.class);

    private final KeycloakSession session;

    private DateTimeFormatter dateTimeFormatter;

    private String zoneId;

    private String attribute;

    public LastLoginEventListenerProvider(
        KeycloakSession session,
        String zoneId,
        DateTimeFormatter dateTimeFormatter,
        String attribute
    ) {
        this.session = session;
        this.dateTimeFormatter = dateTimeFormatter;
        this.zoneId = zoneId;
        this.attribute = attribute;
    }

    @Override
    public void onEvent(Event event) {
        if (event.getType().equals(EventType.LOGIN)) {
            final UserModel user = session.users().getUserById(
                session.getContext().getRealm(),
                event.getUserId()
            );
            try {
                String lastLogin = ZonedDateTime.now(ZoneId.of(this.zoneId))
                    .format(this.dateTimeFormatter);
                user.setSingleAttribute(this.attribute, lastLogin);
            } catch(DateTimeException exception) {
                logger.error("Failed to parse last login date, because of invalid configuration", exception);
            }
        }
    }

    @Override
    public void onEvent(AdminEvent event, boolean includeRepresentation) { }

    @Override
    public void close() {}
}
