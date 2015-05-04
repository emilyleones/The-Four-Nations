package view.dialogs;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JDialog;
import javax.swing.JFrame;

import view.ClickHandler;

/**
 * A modal dialog that displays gameplay instructions relative to some parent frame
 * @author Christopher
 *
 */
public class HowToPlayDialog extends JDialog implements ClickHandler {
	private static final long serialVersionUID = -1885482032550659943L;
	private HowToPlayPanel hpp;

	public HowToPlayDialog( JFrame parent ) {
		this.hpp = new HowToPlayPanel( this );
		super.setSize( this.hpp.getWidth(), this.hpp.getHeight() );
		super.setResizable( false );
		super.setUndecorated( true );
		super.setBackground( new Color( 0, 0, 0, 0) );
		super.setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );
		super.setVisible( true );
		super.setLocationRelativeTo( parent );
		
		super.setContentPane( this.hpp );
	}

	@Override public void handleClick(Component component) {
		dispose();
	}
}
