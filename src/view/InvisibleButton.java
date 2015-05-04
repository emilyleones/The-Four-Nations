package view;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 * A transparent JPanel that gains a white border when moused over. When clicked, it will
 * send a message to it's handler.
 * @author Christopher Chapline, James Fagan, Emily Leones, Michelle Yung
 *
 */
public class InvisibleButton extends JPanel implements MouseListener  {
	private static final long serialVersionUID = -6894259703329191579L;
	private Border border = BorderFactory.createLineBorder(Color.WHITE, 5);
	private ClickHandler handler;

	/**
	 * Creates an invisbile button that is white and 5 pixels thick
	 * @param handler The object that handles when the mouse clicks within the panel area
	 */
	public InvisibleButton( ClickHandler handler ) {
		this( handler, Color.white, 5 );
	}
	
	/**
	 * Creates an invisible button with a variable color and border witdth
	 * @param handler The object that handles when the mouse clicks within the panel area
	 * @param borderColor The color of the mouse-over border
	 * @param borderThickness The thickness of the generated border
	 */
	public InvisibleButton( ClickHandler handler, Color borderColor, int borderThickness ) {
		super.addMouseListener( this );
		super.setOpaque( false );
		this.handler = handler;
		this.border = BorderFactory.createLineBorder( borderColor, borderThickness );
	}

	/**
	 * When the component is moused over, draw the border
	 */
	@Override public void mouseEntered(MouseEvent e) {
		this.setBorder( border );
	}

	/**
	 * When the mouse exists the component, remove the border
	 */
	@Override public void mouseExited(MouseEvent e) {
		this.setBorder( null );
	}

	/**
	 * Send a message to the click handler when the mouse clicks inside the component.
	 */
	@Override public void mouseClicked(MouseEvent e) { 
		if( this.handler != null ) this.handler.handleClick(this);
	}

	/* Unused inherited methods */
	@Override public void mousePressed(MouseEvent e) { }
	@Override public void mouseReleased(MouseEvent e) { }

	

}