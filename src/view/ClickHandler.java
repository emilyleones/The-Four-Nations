package view;

import java.awt.Component;

/**
 * A click handler is used by the InvisibleButton class to ensure
 * that clicks within the JPanel are handled
 * @author Christopher
 *
 */
public interface ClickHandler {
	public void handleClick( Component component );
}
