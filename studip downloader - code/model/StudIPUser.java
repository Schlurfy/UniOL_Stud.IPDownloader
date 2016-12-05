package model;

import java.util.ArrayList;
import java.util.List;

import org.scribe.model.Token;




import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Klasse fuer Daten und Methoden des Users
 * 
 * @author Felix, Fabian
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StudIPUser
{ 
	
	/**
	 * Dokumentenpfad, default: "download/"
	 * @author Fabian
	 */
	public String docPath = "download/";
	
	
	public String token = null;
	public String secrect = null;
	
	public List<String> isDownloaded = new ArrayList<String>();
	
	/**
	 * Name und Speicherort des Access Token
	 * @author Fabian
	 */
	public String userPath = "StudIP_User.json";
	
	/**
	 * Name und Speicherort des Logs
	 * @author Fabian
	 */
	public String logFilePath = "StudIP_Log.txt";
	
	/**
	 * Variable fuer gesammten Log
	 * @author Fabian
	 */
	@JsonIgnore
	public String logText = "";
	
	//constructor
	public StudIPUser()
	{
		
	}

	@JsonIgnore
	public Token getAccessToken()
	{
		if(this.token != null && this.secrect != null)
			return new Token(this.token,this.secrect);
		return null;
	}

	@JsonIgnore
	public void setAccessToken(Token token)
	{
		this.token = token.getToken();
		this.secrect = token.getSecret();
	}

	
	public void setIsDownladed(List<String> list)
	{
		this.isDownloaded = list;
	}
	
	public void addIsDownloaded(String s)
	{
		this.isDownloaded.add(s);
	}
	
	public List<String> getIsDownloaded()
	{
		return this.isDownloaded;
	}
	
	/**
	 * Setze Speicherort der Access Token Datei
	 * @param accessTokenPath String Pfad + Dateiname
	 * 
	 * @author Fabian
	 */
	public void setUserPath(String _userPath){
		this.userPath = _userPath;
	}

	/**
	 * Setze Speicherort fuer Dateien von StudIP
	 * @param accessTokenPath String Pfad
	 * 
	 * @author Fabian
	 */
	public void setDocPath(String docPath) {
		this.docPath = docPath;
	}
	
	public void setLogFilePath(String logFilePath) {
		this.logFilePath = logFilePath;
	}

	/**
	 * Fuege Log-Eintrag hinzu
	 * @param log
	 * 
	 * @author Fabian
	 */
	public void addLogText(String log) {
		this.logText += log;
	}	
}
