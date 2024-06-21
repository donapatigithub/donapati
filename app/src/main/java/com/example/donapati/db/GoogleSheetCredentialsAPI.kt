package com.example.donapati.db

import android.content.Context
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.auth.http.HttpCredentialsAdapter
import com.google.auth.oauth2.GoogleCredentials
import java.io.IOException
import java.security.GeneralSecurityException

class GoogleSheetCredentialsAPI(private val applicationContext: Context) {
    private val spreadsheetId = "19zGEh_ZxbXnA0A1DCoJ2iF4s_IevHszVAbUqbHn2Mz0"
    private val range = "LoginCredentials"

    @Throws(IOException::class, GeneralSecurityException::class)
    fun getSheetsService(): Sheets {
        val transport = GoogleNetHttpTransport.newTrustedTransport()
        val factory = GsonFactory.getDefaultInstance()
        val credentials = GoogleCredentials.fromStream(applicationContext.assets.open("credentials.json"))
            .createScoped(listOf("https://www.googleapis.com/auth/spreadsheets"))
        val requestInitializer = HttpCredentialsAdapter(credentials)

        return Sheets.Builder(transport, factory, requestInitializer)
            .setApplicationName("EazyKart")
            .build()
    }
}
