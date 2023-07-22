package com.example.ekonyv.network

import android.util.Log
import androidx.lifecycle.LifecycleCoroutineScope
import com.example.ekonyv.data.ServerPreference
import com.example.ekonyv.data.ServerPreferenceDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import java.net.URL
import java.util.Date

private const val LOG_TAG = "SRVMGR";

class ServerManager (private val serverPreferenceDao: ServerPreferenceDao) {
    data class ServerInfo (
        val name: String,
        val version: String
    )

    private var _ip: String? = null
    private var _info: ServerInfo? = null

    val ip: String?
        get() = _ip
    val info: ServerInfo?
        get() = _info

    val ipChangeCallbacks = mutableListOf<(String, ServerInfo)->Unit>()

    private fun parseInfo(csv: List<List<String>>): ServerInfo? {
        if (csv.size <= 2)
            return null

        if (csv[1].size <= 2 || csv[2].size <= 2)
            return null

        return ServerInfo(csv[1][1], csv[2][1])
    }

    suspend fun tryIP(ip: String, scope: LifecycleCoroutineScope): ServerInfo? {
        if (!verifyIP(ip)) {
            Log.e(LOG_TAG, "Hostname $ip failed verification.")
            return null
        }

        return try {
            fetch(URL("http://$ip:80/ekonyv"), FetchMethod.GET, scope).let {
                Log.d(LOG_TAG, "Fetched $ip/ekonyv; statusCode: ${it?.statusCode ?: "<invalid>"}")
                if (it?.statusCode == 200) {
                    parseInfo(it.csv())
                }
                else {
                    null
                }
            }
        } catch (exc: Throwable) {
            null
        }
    }

    suspend fun trySetIP(ip: String, scope: LifecycleCoroutineScope, save: Boolean): Boolean {
        tryIP(ip, scope).also{info ->
            info ?: return false

            if (save)
                updateIP(ip);

            _ip = ip;
            ipChangeCallbacks.forEach{ cb -> cb(ip, info) }

            return true
        }
    }

    suspend fun trySetSavedIPs(scope: LifecycleCoroutineScope, save: Boolean): ServerPreference? {
        for (pref in serverPreferenceDao.getAllOrdered()) {
            if (trySetIP(pref.ip_address, scope, save)) {
                return pref
            }
        }

        return null
    }

    fun updateIP(ip: String) {
        serverPreferenceDao.insert(
            ServerPreference(
                0, ip, Date().time
            )
        )
    }

    companion object {
        private val IP_ADDRESS_REGEX = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$".toRegex()
        private val HOSTNAME_REGEX = "^(([a-z0-9]|[a-z0-9][a-z0-9\\-]*[a-z0-9])\\.)*([a-z0-9]|[a-z0-9][a-z0-9\\-]*[a-z0-9])$".toRegex(RegexOption.IGNORE_CASE)

        fun verifyIP(ip: String): Boolean {
            return IP_ADDRESS_REGEX.matches(ip) || HOSTNAME_REGEX.matches(ip)
        }
    }
}