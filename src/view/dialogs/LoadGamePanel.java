package view.dialogs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import view.ClickHandler;
import view.InvisibleButton;

import model.GameImageLoader;
import model.SaveLoadManager;

/**
 * The panel that displays available game saves and allows the user to load them
 * @author Christopher Chapline, James Fagan, Emily Leones, Michelle Yung
 *
 */
public class LoadGamePanel extends JPanel{

	private static final long serialVersionUID = 4151226823918499102L;

	//Image
	private BufferedImage background = GameImageLoader.getImage( GameImageLoader.imagesFolder + "AvatarScroll.png" );

	//Component names for ClickHandler
	public final static String LOAD = "LOAD";
	public final static String GO_BACK = "GOBACK";
	
	//Components
	private JList<String> savedGames;
	private Color transparent = new Color( 0, 0, 0, 0 );
	private JScrollPane savedGamesScroller;
	private InvisibleButton loadGame, goBack;
	private Border border = BorderFactory.createTitledBorder( new LineBorder( new Color( 0, 0, 0) ), "Please select a save from below: ", 0, 0 );
	private DefaultListModel<String> listModel;

	public LoadGamePanel( ClickHandler handler ) {
		//Panel
		super.setLayout( null );
		super.setSize( background.getWidth(), background.getHeight() );
		super.setBackground( transparent );

		//List
		this.listModel = loadGames();
		this.savedGames = new JList<>();
		this.savedGames.setModel( this.listModel );
		this.savedGames.setFont( new Font( "Sans", Font.PLAIN, 25 ) );
		this.savedGames.setSize( new Dimension( 440, 175 ) );
		this.savedGames.setBackground( new Color( 233, 203, 143 ) );

		//Scrollpane
		this.savedGamesScroller = new JScrollPane( this.savedGames );
		this.savedGamesScroller.setSize( this.savedGames.getSize() );
		this.savedGamesScroller.setBorder( this.border );
		this.savedGamesScroller.setBackground( new Color( 233, 203, 143 ) );
		this.savedGamesScroller.setLocation( super.getSize().width / 2 - this.savedGamesScroller.getWidth() / 2, 
				super.getSize().height / 2 - 120);
		
		//Buttons
		this.loadGame = new InvisibleButton( handler, Color.black, 1 );
		this.loadGame.setName( LOAD );
		this.loadGame.setLocation( 95, 460 );
		this.loadGame.setSize( 130, 50 );
		this.goBack = new InvisibleButton( handler, Color.black, 1 );
		this.goBack.setName( GO_BACK );
		this.goBack.setLocation( 345, 455);
		this.goBack.setSize( 190, 55 );

		//Add components
		super.add( this.savedGamesScroller );
		super.add( this.loadGame );
		super.add( this.goBack );
	}

	@Override public void paintComponent( Graphics g ) {
		super.paintComponent( g );
		Graphics2D g2 = (Graphics2D)g;
		g2.drawImage( background, 0, 0, null );

		//Store default font/color, set new ones
		Font defaultFont = g2.getFont();
		Font font = new Font( "Sans", Font.PLAIN, 45  );
		Color defaultColor = g2.getColor();
		Color color = new Color( 175, 110, 45, 255/2 ); //Lightish brown with 50% alpha
		g2.setFont( font );
		g2.setColor(color);

		//Draw strings and reset font/color
		g2.drawString( "Load", 105, 500);
		g2.drawString( "Go Back", 350, 500 );
		g2.setFont(defaultFont);
		g2.setColor(defaultColor);
	}

	private DefaultListModel<String> loadGames() {
		DefaultListModel<String> model = new DefaultListModel<String>();
		
		ArrayList<String> savedGames = SaveLoadManager.getSavedGames();
		for( String savedGame : savedGames ) {
			model.addElement(savedGame.replaceAll(".fnsf", ""));
		}
		
		return model;
	}
	
	/**
	 * Returns if an item on the JList is currently selected
	 */
	public boolean saveIsSelected() {
		return ! this.savedGames.isSelectionEmpty();
	}
	
	/**
	 * Returns the item currently selected in the JList
	 */
	public String getSelectedSave() {
		return this.savedGames.getSelectedValue();
	}
}