package com.fdilke.gdata

import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import java.util
import java.io.{InputStreamReader, BufferedReader}
import com.google.api.client.json.jackson.JacksonFactory
import com.google.api.client.googleapis.auth.oauth2.{GoogleAuthorizationCodeFlow, GoogleTokenResponse, GoogleCredential}
import com.google.api.services.drive.DriveScopes
import com.fdilke.util.ReadProperties

// User: Felix Date: 14/05/2014 Time: 18:21

object GDataSpike extends App {
  private val oauthConfig = ReadProperties("local/oauth.config")
  private val CLIENT_ID = oauthConfig("client.id")
  private val CLIENT_SECRET = oauthConfig("client.secret")
  private val REDIRECT_URI = oauthConfig("redirect.uri")

  val httpTransport: HttpTransport = new NetHttpTransport
  val jsonFactory: JsonFactory = new JacksonFactory
  val googleAuthorizationCodeFlow: GoogleAuthorizationCodeFlow = new GoogleAuthorizationCodeFlow.Builder(
    httpTransport, jsonFactory, CLIENT_ID, CLIENT_SECRET,
    util.Arrays.asList(DriveScopes.DRIVE)).setAccessType("online")
    .setApprovalPrompt("auto").setAccessType("offline").build()
  val redirectUrl:String = googleAuthorizationCodeFlow.newAuthorizationUrl().
    setRedirectUri(REDIRECT_URI).build()

  println("Please open the following URL in your browser then type the authorization code:")
  println("  " + redirectUrl)
  val br: BufferedReader = new BufferedReader(new InputStreamReader(System.in))

  val code: String = br.readLine()
  println(s"code was: $code")

  val googleTokenResponse: GoogleTokenResponse = googleAuthorizationCodeFlow.
      newTokenRequest(code).setRedirectUri(REDIRECT_URI).execute()
  val googleCredential: GoogleCredential = new GoogleCredential().
      setFromTokenResponse(googleTokenResponse)
  println("service account user = " + googleCredential.getServiceAccountUser)

  println("googleCredential =" + googleCredential)
}
