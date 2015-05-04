package view.dialogs;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import model.GameImageLoader;
import view.ClickHandler;
import view.InvisibleButton;

public class SlideshowPanel extends JPanel implements ClickHandler{

	private static final long serialVersionUID = 2693234176449994734L;
	private List<BufferedImage> imagesInSlideshow = loadImages();
	private InvisibleButton gotoNextImage, gotoPreviousImage;
	private int currentImage = 0;
	private Container parent;
	
	public SlideshowPanel( Container parent ) {
		super.setLayout( null );
		this.parent = parent;
		
		this.gotoNextImage = new InvisibleButton( this, new Color(0,0,0,0), 0 );
		this.gotoNextImage.setSize( 90, 35 );
		this.gotoNextImage.setLocation( 280, 185 );
		
		this.gotoPreviousImage = new InvisibleButton( this, new Color(0,0,0,0), 0 );
		this.gotoPreviousImage.setSize( 120, 35 );
		this.gotoPreviousImage.setLocation( 90, 185 );
		
		super.add( this.gotoNextImage );
		super.add( this.gotoPreviousImage );
	}
	
	public List<BufferedImage> loadImages() {
		List<BufferedImage> images = new ArrayList<>();
		images.add( GameImageLoader.getImage( GameImageLoader.imagesFolder + "PanningAroundMap.png" ) );
		images.add( GameImageLoader.getImage( GameImageLoader.imagesFolder + "AboutUnits.png" ) );
		images.add( GameImageLoader.getImage( GameImageLoader.imagesFolder + "ClickingMiniMap.png" ) );
		images.add( GameImageLoader.getImage( GameImageLoader.imagesFolder + "WhatAreResources.png" ) );
		return images;
	}
	
	private void nextImage() {
		if( ++this.currentImage >= this.imagesInSlideshow.size() ) this.currentImage = 0;
	}
	
	private void previousImage() {
		if( --this.currentImage < 0 ) this.currentImage = this.imagesInSlideshow.size() - 1;
	}
	
	@Override public void paintComponent( Graphics g ) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.drawImage( this.imagesInSlideshow.get( this.currentImage ), 0, 0, null );
		Font font = new Font( "Sans", Font.PLAIN, 25 );
		g2.setFont( font );
		g2.drawString( "Previous", 100, 210 );
		g2.drawString( "Next", 300, 210 );
	}

	@Override public void handleClick(Component component) {
		if( component == gotoNextImage ) {
			nextImage();
		}
		else if( component == gotoPreviousImage ) {
			previousImage();
		}
		
		this.parent.repaint();
	}
}
