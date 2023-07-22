package com.example.ekonyv.ui.server

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.example.ekonyv.MainActivity
import com.example.ekonyv.R
import com.example.ekonyv.databinding.FragmentServerBinding
import com.example.ekonyv.ui.subfragments.ConnectToServer
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onSubscription

class ServerFragment : Fragment() {
    private var _binding: FragmentServerBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentServerBinding.inflate(inflater, container, false)
        val root: View = binding.root

        (activity as MainActivity).serverManager.ipChangeCallbacks.add { ip, info ->
            binding.sfStatus.text = getString(R.string.sf_status_connected, ip)
            binding.sfInfo.text = getString(R.string.sf_info, info.name, info.version)
        }

        childFragmentManager.commit {
            replace(R.id.sf_connect_to_server, ConnectToServer())
            setReorderingAllowed(true)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}