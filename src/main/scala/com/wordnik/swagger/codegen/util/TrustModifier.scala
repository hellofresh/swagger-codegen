package com.wordnik.swagger.codegen.util

import java.net._
import javax.net.ssl._
import java.security._
import java.security.cert._
//remove if not needed
import scala.collection.JavaConversions._

object TrustModifier {

  private val TRUSTING_HOSTNAME_VERIFIER = new TrustingHostnameVerifier()

  private var factory: SSLSocketFactory = _

  def relaxHostChecking(conn: HttpURLConnection) {
    if (conn.isInstanceOf[HttpsURLConnection]) {
      val httpsConnection = conn.asInstanceOf[HttpsURLConnection]
      val factory = prepFactory(httpsConnection)
      httpsConnection.setSSLSocketFactory(factory)
      httpsConnection.setHostnameVerifier(TRUSTING_HOSTNAME_VERIFIER)
    }
  }

  def prepFactory(httpsConnection: HttpsURLConnection): SSLSocketFactory = {
    synchronized {
      if (factory == null) {
        val ctx = SSLContext.getInstance("TLS")
        ctx.init(null, Array(new AlwaysTrustManager()), null)
        factory = ctx.getSocketFactory
      }
      factory
    }
  }

  private class TrustingHostnameVerifier extends HostnameVerifier {

    def verify(hostname: String, session: SSLSession): Boolean = true
  }

  private class AlwaysTrustManager extends X509TrustManager {

    def checkClientTrusted(arg0: Array[X509Certificate], arg1: String) {
    }

    def checkServerTrusted(arg0: Array[X509Certificate], arg1: String) {
    }

    def getAcceptedIssuers(): Array[X509Certificate] = null
  }
}