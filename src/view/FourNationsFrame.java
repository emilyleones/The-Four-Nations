package view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import model.Civilization;
import model.Tribe;
import model.map.ResourceType;

/**
 * The primary frame for The Four Nations game. Handles panel switching
 * and window decoration.
 * @author Christopher Chapline, James Fagan, Emily Leones, Michelle Yung
 *
 */
public class FourNationsFrame extends JFrame {

	private static final long serialVersionUID = 3775114448785000079L;

	//Panels
	private Map<String, JPanel> panels;
	private MainMenuPanel mainMenuPanel;
	private NewGamePanel newGamePanel;
	private GameDisplayPanel gameDisplayPanel;
	private Timer timer;
	
	//Panel labels
	public final static String mainMenu = "MAIN_MENU";
	public final static String newGame = "NEW_GAME";
	public final static String gamePanel = "GAME_PANEL";
	
	public FourNationsFrame() {
		Civilization civ = Civilization.getInstance();
		if( civ.getMap() == null ) civ.setMap( new model.map.Map() );
		civ.setTribe( Tribe.values()[ (int) (Math.random() * Tribe.values().length) ] );
		this.timer = new Timer( 300, new ActionListener() {
			@Override public void actionPerformed(ActionEvent arg0) {
				//1% chance to lose 10% food stores
				if( Math.random() < 0.005 ) {
					ResourceType food = ResourceType.food;
					Civilization.getInstance().setResourceAmount(food,  (int) (Civilization.getInstance().getResourceAmount(food) * 0.90) );
					pause();
					JOptionPane.showMessageDialog( null,  "Famine strikes and eliminates 10% of your food stores!" );
					resume();
				}
				Civilization.getInstance().update();
				repaint();
				gameDisplayPanel.update();
			}
		});
		initComponents();
		initFrame();
	}
	
	private void initFrame() {
		super.setSize( this.mainMenuPanel.getSize() );
		super.setLayout( null );
		super.setDefaultCloseOperation( EXIT_ON_CLOSE );
		super.setLocationRelativeTo( null );
		super.setUndecorated( true );
		super.setBackground( new Color( 0, 0, 0, 0 ) );
		super.setResizable( false );
		super.setVisible( true );
	}
	
	private void initComponents() {
		//Main menu
		this.mainMenuPanel = new MainMenuPanel( this );
		this.mainMenuPanel.setLocation( 0, 0 );
		
		//New game menu
		this.newGamePanel = new NewGamePanel( this );
		this.newGamePanel.setLocation( 0, 0 );
		this.newGamePanel.setSize( this.mainMenuPanel.getSize() );
		
		//Game display panel
		this.gameDisplayPanel = new GameDisplayPanel( this );
		
		//Cards
		this.panels = new HashMap<String, JPanel>();
		this.panels.put( mainMenu, mainMenuPanel );
		this.panels.put( newGame, newGamePanel );
		this.panels.put( gamePanel, gameDisplayPanel );
		
		//Show main menu at startup
		showPanel( mainMenu );
	}
	
	public void pause() {
		this.timer.stop();
	}
	
	public void resume() {
		this.timer.start();
	}
	
	/**
	 * Creates an instance of FourNationsFrame 
	 */
	public static void main( String[] args ) {
		new FourNationsFrame();
	}
	
	/**
	 * Displays the panel that is mapped to the given String in the
	 * panel Map
	 */
	public void showPanel( String panelName ) {
		if( this.panels.containsKey(panelName) )
			super.setContentPane( this.panels.get( panelName ) );
	}
}
