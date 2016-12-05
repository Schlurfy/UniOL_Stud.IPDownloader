package service.OAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.scribe.builder.ServiceBuilder;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;


import service.ManagementService;

/**
 * Klasse zur Autorisierung ueber OAuth
 * 
 * @author Fabian
 *
 */
public class OAuth
{ 
	/**
	 * Service fuer OAuth und RestIP abfragen
	 * 
	 * @author Fabian
	 */
	public OAuthService oauthService;
	
	private Token requestToken;
	
	/**
	 * Erzeuge OAuth Service
	 * 
	 * @author Fabian
	 */
	public OAuth(){
		// Nutze den callback, um "oauth_verifier" in der Url zu finden.
		
		//"https://elearning.uni-oldenburg.de/index.php"
		oauthService = new ServiceBuilder()
	                                .provider(model.StudIPApi.class)
	                                .apiKey("03447bb89cfb875bae1d141060dbc5290583c04dd")
	                                .apiSecret("cfe2344fcd44ae32b23b53da4dc0b494")
	                                .callback("https://elearning.uni-oldenburg.de/plugins.php/restipplugin/oauth")
	                                .build();
	}
	
	/**
	 * Managed Anmeldeprocess
	 * Methode Frage Request Token von Server an, Starten BrowerLoginStage und erhaet den Access Token.
	 * 
	 * @param user model.StudIPUser-Objekt bekommt Service
	 * @return Authorisations Url 
	 * 
	 * @author Fabian
	 */
	public String login()
	{
	    // Anfordern des Request Token		
		requestToken = oauthService.getRequestToken();	
		
		return oauthService.getAuthorizationUrl(requestToken);
	}
	
	/**
	 * Fragt ab, ob der StudIPUser Autorisierungsdaten hat, ob aus der Access Token Datei oder ueber Browser Login(hierfuer url angeben)
	 * @param url
	 * @return
	 * 
	 * @author Fabian
	 */
	public boolean loginReady(String url){

    	//System.out.println("----");

    	//System.out.println(ManagementService.getUser().getAccessToken());
		if(ManagementService.getUser().getAccessToken() == null&&url!=null){
			
			//exrahiere verifier aus Url
		    Verifier verifier = extraktVerifier(url);
		    
		    // Tausche Request Token und Verfier gegen Access Token
		    Token accessToken = oauthService.getAccessToken(requestToken, verifier);
			
		    
		    //Melde: neuer Access Token erhalten
		    ManagementService.newAccessToken(accessToken);
		    
		    ManagementService.addLog("Login erfolgreich");

		    return true;
		}else if(ManagementService.getUser().getAccessToken() !=null){
			return true;
		}
	    
	    return false;
	}
	
	/**
	 * Extrahiert aus der Callback url den OAuth_verifier
	 * @param Url Callback Url vom LoginBrowser
	 * @return Verifier des Nutzers
	 * 
	 * @author Fabian
	 */
	private Verifier extraktVerifier(String url){
		//exrahiere verifier aus Url
	    Pattern pattern = Pattern.compile( ".*oauth_verifier=([0-9a-f]*)" );		
	    Matcher matcher = pattern.matcher(url);
	    if(matcher.find()){
	    	String verifierString = matcher.group(1);
	    	return new Verifier(verifierString);
	    }else{
			return null;
	    }
	}
	
	
}
