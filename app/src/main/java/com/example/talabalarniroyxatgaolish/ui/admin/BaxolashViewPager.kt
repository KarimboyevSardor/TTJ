package com.example.talabalarniroyxatgaolish.ui.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.talabalarniroyxatgaolish.databinding.FragmentBaxolashViewPagerAdminBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class BaxolashViewPager : Fragment() {
    private var param1: Long? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getLong(ARG_PARAM1)
        }
    }

    private var binding: FragmentBaxolashViewPagerAdminBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBaxolashViewPagerAdminBinding.inflate(layoutInflater)

        binding!!.apply {

        }

        return binding!!.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Long) =
            BaxolashViewPager().apply {
                arguments = Bundle().apply {
                    putLong(ARG_PARAM1, param1)
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}