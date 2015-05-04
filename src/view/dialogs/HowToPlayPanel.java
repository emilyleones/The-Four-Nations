package view.dialogs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import model.GameImageLoader;
import view.ClickHandler;
import view.InvisibleButton;

/**
 * A panel containing instructions on how to play the game
 * @author Christopher
 *
 */
public class HowToPlayPanel extends JPanel {

	private static final long serialVersionUID = -6401990241674052192L;
	
	//Images
	private BufferedImage howToPlay = GameImageLoader.getImage( GameImageLoader.imagesFolder + "AvatarScroll.png" );
	
	//Components
	private InvisibleButton exitButtonHandler;
	private SlideshowPanel slideshow;

	public HowToPlayPanel( ClickHandler handler) {
		//Panel settings
		super.setLayout( null );
		super.setSize( new Dimension( howToPlay.getWidth(), howToPlay.getHeight() ) );
		super.setBackground( new Color( 0, 0, 0, 0 ) );
		
		//Invisible button
		this.exitButtonHandler = new InvisibleButton( handler, Color.white, 3 );
		this.exitButtonHandler.setSize( 105, 45 );
		this.exitButtonHandler.setLocation( 240, 630 );
		
		//Slideshow
		this.slideshow = new SlideshowPanel( this );
		this.slideshow.setSize( 435, 220 );
		this.slideshow.setBackground( new Color( 0, 0, 0, 0 ) );
		this.slideshow.setLocation( 80, 280);
		
		//Add button
		super.add( this.exitButtonHandler );
		super.add( this.slideshow );
	}
	
	@Override public void paintComponent( Graphics g ) {
		super.paintComponent( g );
		Graphics2D g2 = (Graphics2D)g;
		
		//Draw background image
		g2.drawImage( howToPlay, 0, 0, null );
		
		//Store default font/color, set new ones
		Font defaultFont = g2.getFont();
		Font font = new Font( "Sans", Font.PLAIN, 35  );
		Color defaultColor = g2.getColor();
		Color color = new Color( 175, 110, 45, 255/2 ); //Lightish brown with 50% alpha
		g2.setFont( font );
		g2.setColor(color);
		
		//Draw string and reset font/color
		g2.drawString( "Close", 250, 665 );
		g2.setFont(defaultFont);
		g2.setColor(defaultColor);
	}
}
