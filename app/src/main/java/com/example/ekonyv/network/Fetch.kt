package com.example.ekonyv.network

import android.util.Log
import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

internal const val LOG_TAG = "Fetch"

enum class FetchMethod(val str: String) {
    GET("GET"),
    POST("POST"),
    DELETE("DELETE")
}

class FetchResult (
    conn: HttpURLConnection
) {
    private val stream: InputStream = conn.inputStream

    val statusCode: Number = conn.responseCode
    val statusMessage: String = conn.responseMessage
    val headers: Map<String, List<String>> = conn.headerFields
    private var _text: String? = null

    fun body(): String? {
        if (_text != null)
            return _text;

        try {
            BufferedReader(InputStreamReader(stream)).use {
                _text = it.readText()
                return _text
            }
        } catch (exc: IOException) {
            return null
        }
    }

    fun csv(): Sequence<Sequence<String>> {
        val text = body() ?: return sequenceOf()

        return text.splitToSequence(NEWLINE_REGEXP).map{ line ->
            CSV_REGEXP.findAll(line).map {match->
                match.groupValues[0]
            }
        }
    }

    companion object {
        private val CSV_REGEXP = Regex(pattern = "/(?:\"[^\"]*\"|[^,]*?),/gm")
        private val NEWLINE_REGEXP = Regex(pattern = "/(?:\r\n|\n)/g");
    }
}

@OptIn(DelicateCoroutinesApi::class)
suspend fun fetch(url: URL, method: FetchMethod, scope: LifecycleCoroutineScope): FetchResult? {
    return suspendCoroutine {cont ->
        val threadContext = newSingleThreadContext("fetch_request")

        scope.launch(context = threadContext) {
            try {
                val connection = withContext(Dispatchers.IO) {
                    url.openConnection()
                } as HttpURLConnection

                connection.requestMethod = method.str

                Log.d(LOG_TAG, "Successfully fetched $url")
                cont.resume(FetchResult(connection))
            }
            catch (exc: IOException) {
                Log.e(LOG_TAG, "IOException while fetching $url: $exc")
                cont.resume(null)
            }
            finally {
                Log.d(LOG_TAG,"Closing threadContext for fetch from $url")
                threadContext.close()
            }
        }
    }
}
