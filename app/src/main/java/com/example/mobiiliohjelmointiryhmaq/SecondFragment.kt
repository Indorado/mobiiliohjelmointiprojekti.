package com.example.mobiiliohjelmointiryhmaq

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation


class SecondFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_second, container, false)

        val textField2=view.findViewById<TextView>(R.id.textView2)

        textField2.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.navigateToFirst)
        }

        return view
    }
}