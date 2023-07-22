package com.example.ekonyv.ui.subfragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.example.ekonyv.MainActivity
import com.example.ekonyv.R
import com.example.ekonyv.network.ServerManager
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [ConnectToServer.newInstance] factory method to
 * create an instance of this fragment.
 */
class ConnectToServer : Fragment() {
    private lateinit var serverManager: ServerManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        serverManager = (activity as MainActivity).serverManager

        return inflater.inflate(R.layout.fragment_connect_to_server, container, false).also {view ->
            view.findViewById<Button>(R.id.search_ip).setOnClickListener {
                val ip = view.findViewById<EditText>(R.id.ip_address).text.toString()
                val save = view.findViewById<CheckBox>(R.id.checkBox).isChecked

                lifecycleScope.launch {
                    serverManager.trySetIP(ip, lifecycleScope, save)
                }
            }
        }
    }
}