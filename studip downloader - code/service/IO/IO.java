package service.IO;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Scanner;
import service.ManagementService;

public class IO {

	/**
	 * Schreibt String in Datei
	 * 
	 * @param write
	 *            String mit gewuenschtem Dateiinhalt
	 * @param fileName
	 *            String mit Dateinamen
	 * @return True, falls das Speichern erfolgreich war.
	 * @author Fabian
	 */
	public boolean write(String write, String fileName) {
		FileWriter fw;
		File file = new File(fileName);
		try {
			//erstelle, wenn noetig, das Verzeichnis
			if(file.getParentFile()!=null){
				if(!file.getParentFile().exists()){
					// Versuche Verzeichnis zu erstellen
					if(file.getParentFile().mkdirs()==false){
						ManagementService.addLog("Fehler beim Erstellen des Verzeichnis " + fileName);
						return false;
					}
				}
			}
			
			// schreibe Daten
			fw = new FileWriter(fileName);

			fw.write(write);

			fw.close();
		} catch (IOException e) {
			ManagementService.addLog("Fehler beim Schreiben der Datei " + fileName);
			return false;
		}

		return true;
	}

	/**
	 * Liest Datei in einen String
	 * 
	 * @param fileName
	 *            Dateiname der zu lesenden Datei
	 * @return String mit Dateinhalt (Zeilentrennung ueber Backslash n)
	 * @author Fabian
	 */
	public String read(String fileName) {
		String retVal = "";
		File  file = new File(fileName);
		
		if(file.exists()){// existiert das file?
			try {
				FileReader myReader = new FileReader(file);
				Scanner scanner = new Scanner(myReader);
	
				while (scanner.hasNextLine()) {
					retVal += scanner.nextLine();
					if (scanner.hasNextLine()) {
						retVal += "\n";
					}
				}
				scanner.close(); // Scanner schliesst auch den gescannten Stream
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				retVal = null;
			}
		}
		return retVal;
	}

	/**
	 * Laed die Datei runder und speichere diese in user.getDocPath() +
	 * fileName, sofern diese nicht existiert.
	 * 
	 * @param user
	 *            inputstream des response
	 * @param fileName
	 *            file name wohin es gespeichert werden soll
	 * 
	 * @author Fabian
	 */
	public String download(InputStream inputStream,
			String fileName) {	
		File file = new File(ManagementService.getUser().docPath+fileName);
		
		if(!file.exists())
			try{
				//erstelle, wenn noetig, das Verzeichnis
				if(!file.getParentFile().exists()){
					// Versuche Verzeichnis zu erstellen
					if(file.getParentFile().mkdirs()==false){
						ManagementService.addLog("Fehler beim Erstellen des Verzeichnis " + fileName);
						return "";
					}
				}
				
				//Falls aeltere Datei existiert wird diese ueberschrieben
				if(file.exists()){
					file.delete();
				}
				
				Files.copy(inputStream, file.toPath());
			} catch (Exception e) {
				e.printStackTrace();
				ManagementService.addLog("Fehler beim Speichern von" + fileName);
				return "";
			} finally {
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
						ManagementService.addLog("Fehler beim Speichern von" + fileName);
						return "";
					}
				}
			}
		else
			ManagementService.addLog("Datei existier bereits - "+ fileName);
			
		return file.getName();
		
		//To Do loeschen
		/*// Versuche Verzichniss zu erstellen, falls noetig.
		if (!checkDir(ManagementService.getUser().docPath)) {
			return "";
		}

		// extrahiere Dateierweiterung
		String fileExt = "";
		Pattern pattern = Pattern.compile("(.*)\\.(.*)");
		Matcher matcher = pattern.matcher(fileName);
		if (matcher.find()) {
			fileName = matcher.group(1);
			fileExt = matcher.group(2);
		} else {
			return null;
		}

		Path path = null;
		try {
			// ueberschreiben der Datei verhindern
			if ((new File(ManagementService.getUser().docPath + fileName + "." + fileExt))
					.exists()) {
				int i = 1;
				for (; (new File(ManagementService.getUser().docPath + fileName + "(" + i + ")."
						+ fileExt)).exists(); i++)
					;

				path = Paths.get(ManagementService.getUser().docPath + fileName + "(" + i + ")."
						+ fileExt);
			} else {
				path = Paths.get(ManagementService.getUser().docPath + fileName + "." + fileExt);
			}

			// Speichere Stream
			Files.copy(inputStream, path);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return path.toString();*/

	}

	/**
	 * Stellt sicher, dass das angegebene Verzeichnis existiert.
	 * 
	 * @param dir
	 *            Pfad zum Verseichnis
	 * @return false, falls das Verzeichnis nicht existiert und nicht erstellt
	 *         wurde
	 * 
	 * @author Fabian
	 */
	public boolean checkDir(String dirName) {
		File dir = new File(dirName);
		if (!dir.exists()) {
			return dir.mkdir();
		} else {
			return true;
		}
	}

	/**
	 * Methode schreibt Log in vom user angegebene Datei
	 * 
	 * @param user
	 * @param logText
	 *            Gesammter Logtext
	 * 
	 * @author Fabian
	 */
	public void saveLog(String logText, String fileName) {
		write(logText, fileName);
	}

	//todo delete
	/**
	 * Speichert Access Token
	 * 
	 * @return hat das speichern geklappt
	 * 
	 * @author Fabian
	 *
	 *         public boolean saveAccessToken(StudIPUser user){
	 *         if(user.getAccessToken()!=null){ return
	 *         write("Token:"+user.getAccessToken
	 *         ().getToken()+"\nSecret:"+user.getAccessToken().getSecret(),
	 *         user.getAccessTokenPath()); }else{ return false; } }
	 */

	/**
	 * Laed Access Token aus im StudIPUser angegebener Datei
	 * 
	 * @param user
	 *            StudIPUser-Objekt
	 * @return hat das laden geklappt?
	 * 
	 * @author Fabian
	 *
	 *         public boolean loadAccessToken(StudIPUser user){
	 * 
	 *         String accessFile = read(user.getAccessTokenPath()); Pattern
	 *         pattern = Pattern.compile(
	 *         "Token:([0-9a-f]*)\nSecret:([0-9a-f]*)" ); Matcher matcher =
	 *         pattern.matcher(accessFile);
	 * 
	 *         if(matcher.find()){ user.setAccessToken(new
	 *         Token(matcher.group(1),matcher.group(2))); return true; }else{
	 *         return false; } }
	 */
}
