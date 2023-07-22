package com.example.ekonyv.ui.subfragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.example.ekonyv.R
import com.example.ekonyv.network.FetchMethod
import kotlinx.coroutines.launch
import com.example.ekonyv.network.fetch
import java.net.URL

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ConnectToServer.newInstance] factory method to
 * create an instance of this fragment.
 */
class ConnectToServer : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_connect_to_server, container, false).also {view ->
            view.findViewById<Button>(R.id.button).setOnClickListener {
                val ip = view.findViewById<EditText>(R.id.ip_address).text.toString()

                lifecycleScope.launch {
                    val fetchResult = fetch(URL("http://example.com:80/"), FetchMethod.GET, lifecycleScope)

                    val text_view = view.findViewById<TextView>(R.id.response_preview)
                    text_view.text = "${fetchResult?.statusCode ?: "invalid code"} ${fetchResult?.body() ?: "empty body"}"
                }
            }
        }
    }
}