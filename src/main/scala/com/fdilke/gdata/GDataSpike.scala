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
import scala.collection.JavaConversions
import JavaConversions._

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
    util.Arrays.asList(DriveScopes.DRIVE, "https://spreadsheets.google.com/feeds")).
      setAccessType("online").
      setApprovalPrompt("auto").
      setAccessType("offline").
      setDataStoreFactory(dataStoreFactory).
      build()

  lazy val feed = {
    val credential = flow.loadCredential(USER_ID)
    credential.refreshToken()
    val accessToken = credential.getAccessToken

    val spreadsheetService = new SpreadsheetService("MySpreadsheetIntegration-v1")
    spreadsheetService.setHeader("Authorization", "Bearer " + accessToken);
    val metafeedUrl = new URL("https://spreadsheets.google.com/feeds/spreadsheets/private/full");
    spreadsheetService.getFeed(metafeedUrl, classOf[SpreadsheetFeed]);
  }
}

// Run this to get a user credential with an access and refresh token, then store it in local/.
object GDataSpikeSetupCredential extends App with GDataSpikeCommon {

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
  private val sheets = feed.getEntries()
  println(s"${sheets.size} sheets:")
  sheets.map { sheet =>
    val worksheets = sheet.getWorksheets
    println(s"\t${sheet.getTitle.getPlainText}: ${worksheets.size} worksheets")
    worksheets map { worksheet =>
      println(s"\t\t${worksheet.getTitle.getPlainText} (${worksheet.getColCount} x ${worksheet.getRowCount})")
    }
  }
}


