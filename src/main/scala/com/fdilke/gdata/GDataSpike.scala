package com.fdilke.gdata

import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import java.util
import java.io.{File, InputStreamReader, BufferedReader}
import com.google.api.client.json.jackson.JacksonFactory
import com.google.api.client.googleapis.auth.oauth2.{GoogleAuthorizationCodeFlow, GoogleTokenResponse, GoogleCredential}
import com.google.api.services.drive.DriveScopes
import com.fdilke.util.ReadProperties
import com.google.api.client.util.store.{FileDataStoreFactory, DataStoreFactory}
import com.google.gdata.client.spreadsheet.SpreadsheetService
import java.net.URL
import com.google.gdata.data.spreadsheet.{SpreadsheetEntry, SpreadsheetFeed}
import com.google.gdata.client.authn.oauth.{GoogleOAuthHelper, OAuthHmacSha1Signer, GoogleOAuthParameters}

// User: Felix Date: 14/05/2014 Time: 18:21

trait GDataSpikeCommon {
  protected val oauthConfig = ReadProperties("local/oauth.config")
  protected val CLIENT_ID = oauthConfig("client.id")
  protected val CLIENT_SECRET = oauthConfig("client.secret")
  protected val REDIRECT_URI = oauthConfig("redirect.uri")
  protected val SERVICE_ACCOUNT = oauthConfig("service.account")
  protected val USER_ID = oauthConfig("user.id")

  val httpTransport: HttpTransport = new NetHttpTransport
  val jsonFactory: JsonFactory = new JacksonFactory
  val dataStoreFactory: DataStoreFactory = new FileDataStoreFactory(
    new File("local/credentialDataStore")
  )
  val flow: GoogleAuthorizationCodeFlow = new GoogleAuthorizationCodeFlow.Builder(
    httpTransport, jsonFactory, CLIENT_ID, CLIENT_SECRET,
    util.Arrays.asList(DriveScopes.DRIVE)).
      setAccessType("online").
      setApprovalPrompt("auto").
      setAccessType("offline").
      setDataStoreFactory(dataStoreFactory).
      build()
}

object GDataSpike extends App with GDataSpikeCommon {

  val redirectUrl:String = flow.
    newAuthorizationUrl().
    setRedirectUri(REDIRECT_URI).build()

  println("Please open the following URL in your browser then type the authorization code:")
  println("  " + redirectUrl)
  val br: BufferedReader = new BufferedReader(new InputStreamReader(System.in))

  val code: String = br.readLine()
  println(s"code was: $code")

  val tokenResponse: GoogleTokenResponse = flow.
    newTokenRequest(code).setRedirectUri(REDIRECT_URI).execute()

  val credential: GoogleCredential = new GoogleCredential.Builder().
    setJsonFactory(jsonFactory).
    setTransport(httpTransport).
    setClientSecrets(CLIENT_ID, CLIENT_SECRET).
    build()
  credential.setFromTokenResponse(tokenResponse)

  println(s"service account user = ${credential.getServiceAccountUser}")
  println(s"service account id = ${credential.getServiceAccountId}")
  println(s"service account scopes = ${credential.getServiceAccountScopesAsString}")

  println("googleCredential =" + credential)
  println(s"access token = ${credential.getAccessToken}")
  println(s"refresh token = ${credential.getRefreshToken}")
  println(s"flow.credentialStore = ${flow.getCredentialStore}")
  println(s"flow.credentialDataStore = ${flow.getCredentialDataStore}")
  flow.createAndStoreCredential(tokenResponse, USER_ID)
  println("Stored!")
  println("reloading it:" + flow.loadCredential(USER_ID))
}

object GDataSpikeReloadCredential extends App with GDataSpikeCommon {

  Seq("xx", USER_ID) foreach { id =>
    val credential = flow.loadCredential(id)

    println(s"Credential for($id) :")
    if (credential != null) {
      println(s"access = ${credential.getAccessToken}")
      println(s"refresh = ${credential.getRefreshToken}")
    }
  }
}

object ShowSpreadsheets extends App with GDataSpikeCommon {

  def duffStuff() {
    // Authorize the service object for a specific user
    val oauthParameters = new GoogleOAuthParameters
    oauthParameters.setOAuthConsumerKey(CLIENT_ID)
    oauthParameters.setOAuthConsumerSecret(CLIENT_SECRET)
    oauthParameters.setScope(DriveScopes.DRIVE)

    val signer = new OAuthHmacSha1Signer

    val oauthHelper = new GoogleOAuthHelper(signer);
    oauthHelper.getUnauthorizedRequestToken(oauthParameters) // needed?

    val spreadsheetService = new SpreadsheetService("MySpreadsheetIntegration-v1")
    spreadsheetService.setOAuthCredentials(oauthParameters, signer)

    // Define the URL to request.  This should never change.
    val SPREADSHEET_FEED_URL = new URL(
      "https://spreadsheets.google.com/feeds/spreadsheets/private/full")

    // Make a request to the API and get all spreadsheets.
    val feed = spreadsheetService.getFeed(SPREADSHEET_FEED_URL, classOf[SpreadsheetFeed])
    val spreadsheets = feed.getEntries()
  }

  val credential = flow.loadCredential(USER_ID)
  credential.refreshToken()
  val accessToken = credential.getAccessToken

  val spreadsheetService = new SpreadsheetService("MySpreadsheetIntegration-v1")
  spreadsheetService.setHeader("Authorization", "Bearer " + accessToken);
  val metafeedUrl = new URL("https://spreadsheets.google.com/feeds/spreadsheets/private/full");
  val feed = spreadsheetService.getFeed(metafeedUrl, classOf[SpreadsheetFeed]);

  val spreadsheets = feed.getEntries();
  println("**** List of spreadsheets:")
  for (i <- 0 to spreadsheets.size) {
    val entry = spreadsheets.get(i);
    println("\t" + entry.getTitle().getPlainText());
  }
  println("**** List of spreadsheets: end")

}


