package com.example.ekonyv.ui.server

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.example.ekonyv.R
import com.example.ekonyv.databinding.FragmentServerBinding
import com.example.ekonyv.ui.subfragments.ConnectToServer

class ServerFragment : Fragment() {

    private var _binding: FragmentServerBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val serverViewModel =
            ViewModelProvider(this).get(ServerViewModel::class.java)

        _binding = FragmentServerBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /*serverViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }*/

        childFragmentManager.commit {
            val fragment = ConnectToServer()
            add(R.id.connect_to_server_container, fragment)
            setReorderingAllowed(true)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadFragment(fragment: Fragment) {
        /*childFragmentManager.beginTransaction().also {
            it.add(R.id.server_sub, fragment)
            it.commit()
        }*/
    }
}