package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import model.Civilization;
import model.GameImageLoader;
import model.Tribe;
import model.map.Map;

/**
 * The panel that is shown when a user attempts to start a new game
 * @author Christopher Chapline, James Fagan, Emily Leones, Michelle Yung
 *
 */
public class NewGamePanel extends JPanel implements ClickHandler {

	private static final long serialVersionUID = 7759171050606885943L;
	private static BufferedImage background = GameImageLoader.getImage( GameImageLoader.imagesFolder + "bkg.png" );
	private InvisibleButton water, earth, fire, air, goBack;
	private JFrame parent;

	public NewGamePanel( JFrame parent ) {
		super.setLayout( null );
		super.setBackground( new Color( 0, 0, 0, 0 ) );

		this.parent = parent;

		//Buttons
		this.water = new InvisibleButton( this );
		this.water.setToolTipText( "Water Tribe" );
		this.water.setSize(138, 138);
		this.water.setLocation( 110, 98 );

		this.earth = new InvisibleButton( this );
		this.earth.setToolTipText( "Earth Kingdom" );
		this.earth.setSize( 138, 138 );
		this.earth.setLocation( 780, 98 );

		this.fire = new InvisibleButton( this );
		this.fire.setToolTipText( "Fire Nation" );
		this.fire.setSize( 138, 138 );
		this.fire.setLocation( 110, 476 );

		this.air = new InvisibleButton( this );
		this.air.setToolTipText( "Air Nomads" );
		this.air.setSize( 138, 138 );
		this.air.setLocation( 780, 476 );
		
		this.goBack = new InvisibleButton( this, Color.black, 5 );
		this.goBack.setToolTipText( "Return to main menu" );
		this.goBack.setSize( 110, 35 );
		this.goBack.setLocation( 440, 375 );

		super.add( this.water );
		super.add( this.earth );
		super.add( this.fire );
		super.add( this.air );
		super.add( this.goBack );
	}


	@Override public void paintComponent( Graphics g ) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		
		//Draw background image
		g2.drawImage( background, 0, 0, null );
		
		//Get default font and color from graphics
		Font defaultFont = g2.getFont();
		Color defaultColor = g2.getColor();
		
		//drawString related variables
		Font selectTribeFont = new Font( "Helvetica", Font.BOLD, 60 );
		Font goBackFont = new Font( "Helvetica", Font.PLAIN, 25 );
		
		int selectTribeX = super.getWidth() / 2 - 270;
		int selectTribeY =  super.getHeight() / 2;
		String selectTribe = "Please select a tribe: ";
		
		String goBack = "Go back";
		int goBackX = super.getWidth() / 2 - 65;
		int goBackY = 400;
		
		//Enable anti-aliasing for drawing strings
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setColor( Color.black );
		
		//Draw outlines
		g2.setFont( selectTribeFont );
		g2.drawString( selectTribe, selectTribeX + 3, selectTribeY );
		g2.drawString( selectTribe, selectTribeX - 3, selectTribeY );
		g2.drawString( selectTribe, selectTribeX, selectTribeY + 3 );
		g2.drawString( selectTribe, selectTribeX, selectTribeY - 3 );
		
		g2.setFont(goBackFont);
		g2.drawString( goBack, goBackX + 2, goBackY );
		g2.drawString( goBack, goBackX - 2, goBackY );
		g2.drawString( goBack, goBackX, goBackY + 2 );
		g2.drawString( goBack, goBackX, goBackY - 2 );
		
		//Draw regular strings
		g2.setColor( Color.white );
		g2.setFont( selectTribeFont );
		g2.drawString( selectTribe, selectTribeX, selectTribeY );
		g2.setFont( goBackFont );
		g2.drawString( goBack, goBackX, goBackY );
		
		//Reset graphics settings
		g2.setFont( defaultFont );
		g2.setColor( defaultColor );
	}


	@Override public void handleClick(Component component) {
		Civilization civ = Civilization.getInstance();
		Tribe t = null;
		
		if( component == this.goBack ) {
			((FourNationsFrame) this.parent).showPanel( FourNationsFrame.mainMenu );
			return;
		}
		
		else if( component == this.water )  t = Tribe.WATER; 
		else if( component == this.earth ) 	t = Tribe.EARTH;
		else if( component == this.fire ) 	t = Tribe.FIRE;
		else if( component == this.air ) 	t = Tribe.AIR;
		String gameName = JOptionPane.showInputDialog(this, "Please enter a name for this game." );
		civ.reinitCivilization( new Map(), t, gameName );
		((FourNationsFrame) this.parent).resume();
		((FourNationsFrame) this.parent).showPanel( FourNationsFrame.gamePanel );
	}

}
