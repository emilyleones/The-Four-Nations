package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Manages saving and loading of games
 * @author Christopher Chapline, James Fagan, Emily Leones, Michelle Yung
 *
 */
public class SaveLoadManager {

	//User save location
	private static final String fileSep = File.separator;
	private static final String baseDirectory = System.getProperty( "user.home" );
	private static final String saveLocation = baseDirectory + fileSep + "The Four Nations" + fileSep + "saves";

	/**
	 * Saves a game to the specified file name
	 * @param gameName The name of the file to save the game to
	 * @param civState The persistant state of the game to save
	 */
	public static void saveGame( String gameName, CivilizationState civState ) {
		File saveFile = new File( saveLocation, gameName + ".fnsf" );
		ObjectOutputStream oos = null;
		try {
			//Create the file if it does not exist
			if( ! saveFile.exists() ) {
				new File( saveLocation ).mkdirs(); //Create directories
				saveFile.createNewFile();
			}

			//Initialize stream
			oos = new ObjectOutputStream( new FileOutputStream( saveFile, false ) );

			//Write objects and flush stream
			oos.writeObject( civState );
			oos.flush();
			oos.reset();
		}
		catch( IOException ioe ) {
			System.out.println( "In SaveLoadManager.saveGame" );
			ioe.printStackTrace();
		}
		finally {
			//Attempt to close stream
			try {
				if( oos != null )
					oos.close();
			}
			catch( IOException ioe ) {
				System.err.println( "There was an error closing the stream." );
				ioe.printStackTrace();
			}
		}
	}

	/**
	 * Loads a civilization state from the specified file
	 * @param gameName The name of the save file
	 * @return The loaded civlization state or null if no saved game could be found
	 */
	public static CivilizationState loadGame( String gameName ) {
		//Create file
		File saveFile = new File( saveLocation, gameName + ".fnsf" );
		if( !saveFile.exists() ) return null; //If the save file doesn't exist, return null;

		//Create stream and CivilizationState. Initialize both to null
		CivilizationState cs = null;
		ObjectInputStream ois = null;

		try {
			ois = new ObjectInputStream( new FileInputStream( saveFile ) );
			cs = (CivilizationState) ois.readObject();
		}
		catch( IOException ioe ) {
			System.err.println( "In SaveLoadManager.loadGame" );
			ioe.printStackTrace();
		} catch (ClassNotFoundException cnfe) {
			System.err.println( "In SaveLoadManager.loadGame - Corrupted save file." );
			cnfe.printStackTrace();
		}
		finally {
			try {
				//Attempt to close stream
				if( ois != null ) ois.close();
			}
			catch( IOException ioe ) {
				System.err.println( "There was an error closing the stream." );
				ioe.printStackTrace();
			}
		}
		
		//Return loaded game OR null if no game exists
		return cs;
	}
	
	/**
	 * Returns a list of all saved games in the game folder
	 */
	public static ArrayList<String> getSavedGames() {
		ArrayList<String> savedGames = new ArrayList<>();
		File saveFileLocation = new File( saveLocation );
		
		//Check that the save locations is actually a directory
		if( saveFileLocation.isDirectory() ) {
			//Filter the files in the directory with the "fnsf" (Four Nations Save File) extension
			String[] matchedFiles = saveFileLocation.list( new FilenameFilter() {
				@Override public boolean accept(File dir, String name) {
					return name.endsWith(".fnsf");
				}
			});
			savedGames.addAll( Arrays.asList( matchedFiles ) );
		}
		
		return savedGames;
	}
}
