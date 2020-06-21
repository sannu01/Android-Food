package com.internshala.assignment.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.internshala.assignment.R

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_profile, container, false)

        sharedPreferences= activity?.getSharedPreferences(getString(R.string.shared_preference),Context.MODE_PRIVATE)!!
        val profilename:TextView=view.findViewById(R.id.nameprofile)
        val profileEmail:TextView=view.findViewById(R.id.emailprofile)
        val profilephone:TextView=view.findViewById(R.id.phoneprofile)
        val addressprofile:TextView=view.findViewById(R.id.addressprofile)
        profilename.text=sharedPreferences.getString("name","User")
        profilephone.text=("+91 "+sharedPreferences.getString("phone","Mobile"))
        profileEmail.text=sharedPreferences.getString("email","Email")
        addressprofile.text=sharedPreferences.getString("address","Address")



        return view
    }

}
