package view.dialogs;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import model.Civilization;
import model.CivilizationState;
import model.SaveLoadManager;

import view.ClickHandler;
import view.FourNationsFrame;

/**
 * This dialog displays available games to load and allows the user to load
 * previously saved game states.
 * @author Christopher Chapline, James Fagan, Emily Leones, Michelle Yung
 *
 */
public class LoadGameDialog extends JDialog implements ClickHandler {

	private static final long serialVersionUID = 4351947229344581937L;
	private LoadGamePanel lgp;
	private JFrame parent;

	public LoadGameDialog( JFrame parent ) {
		this.lgp = new LoadGamePanel( this );
		this.parent = parent;
		
		super.setSize( this.lgp.getWidth(), this.lgp.getHeight() );
		super.setResizable( false );
		super.setUndecorated( true );
		super.setBackground( new Color( 0, 0, 0, 0) );
		super.setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );
		super.setVisible( true );
		super.setLocationRelativeTo( parent );
		super.setContentPane( this.lgp );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override public void handleClick(Component component) {
		System.out.println( component.getName() );
		if( component.getName() == LoadGamePanel.GO_BACK)
			dispose();
		else if( component.getName() == LoadGamePanel.LOAD ) {
			System.out.println( "Load Game" );
			if( lgp.saveIsSelected() ) {
				//TODO: Add primary game frame so that this isn't pointless
				System.gc();
				CivilizationState cs = SaveLoadManager.loadGame( lgp.getSelectedSave() );
				Civilization.getInstance().parseCivilizationState( cs );
				((FourNationsFrame) this.parent).showPanel( FourNationsFrame.gamePanel );
				System.out.println( "Loading " + lgp.getSelectedSave() );
				((FourNationsFrame) this.parent).resume();
				dispose();
			}
			else {
				JOptionPane.showMessageDialog( this, "You must select a save!" );
			}
		}
	}
}
