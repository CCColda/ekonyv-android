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

private const val LOG_TAG = "Fetch"

enum class FetchMethod(val str: String) {
    GET("GET"),
    POST("POST"),
    DELETE("DELETE")
}

class FetchResult (
    conn: HttpURLConnection
) {
    val statusCode: Number = conn.responseCode
    val statusMessage: String = conn.responseMessage
    val headers: Map<String, List<String>> = conn.headerFields
    val body: String

    init {
        val reader = BufferedReader(InputStreamReader(conn.inputStream))
        body = reader.readText()
        reader.close()
    }

    fun csv_seq(): Sequence<Sequence<String>> {
        return body.splitToSequence(NEWLINE_REGEXP).map{ line ->
            CSV_REGEXP.findAll(line).map {match->
                match.groupValues[0]
            }
        }
    }

    fun csv(): List<List<String>> {
        return body.split(NEWLINE_REGEXP).map{ line ->
            val result = mutableListOf<String>()
            var match = CSV_REGEXP.find(line)

            while (match != null) {
                result.add(match.groupValues[1])
                match = match.next()
            }

            result
        }
    }

    companion object {
        private val NEWLINE_REGEXP = "\r\n|\n".toRegex();
        private val CSV_REGEXP = "(\"[^\"]*?\"|[^\"]*?)(?:,|$)".toRegex();
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
