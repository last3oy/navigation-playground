package com.last3oy.navigation.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController

class SecondFragment : Fragment() {

    val btShow: Button by findView(R.id.bt_show)
    val etPhone: EditText by findView(R.id.et_phone)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_second, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btShow.setOnClickListener {
            val phoneNum = etPhone.text.toString()
            if (phoneNum.isBlank()) {
                return@setOnClickListener
            }

            val bundle = bundleOf("phoneNum" to phoneNum)
            view.findNavController().navigate(R.id.action_secondFragment_to_thirdFragment2, bundle)
        }
    }
}