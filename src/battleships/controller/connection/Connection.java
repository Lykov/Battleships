package battleships.controller.connection;

import battleships.common.ShipType;
import battleships.common.events.ActionEvent;

/**
 * Унифицирует взаимодействие View-Controller.
 * @author ALEX
 */
public interface Connection 
{
    abstract public void sendNewGameEvent();
    abstract public void sendShotEvent(final int x, final int y);
    abstract public void sendShipPlacedEvent(final int x, final int y, final ShipType ship);
    abstract public void sendActionEvent(final ActionEvent event);
}
