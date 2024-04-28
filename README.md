# Keycloak Last Login Event Listener

This extension adds an event listener that records the users last login time.

## Installation

* Download the latest release from the release page
* Move the jar to `/opt/keycloak/providers`
* Make sure it is loaded correctly by checking the provider info in the admin-console
* Go to your realm > Realm Settings > Events
* Add the event listener `last-login`

## Configuration

The listener currently supports three configuration properties:

* `attribute`: allows you to define the user attribute name where the last login time is stored
* `zone-id`: allows you to change the zone id of the zoned date time
* `pattern`: allows you define a custome date time pattern

**Example**
```shell
--spi-events-listener-last-login-attribute=last_login
--spi-events-listener-last-login-zone-id=America/Los_Angeles
--spi-events-listener-last-login-pattern=dd.MM.yyyy
```