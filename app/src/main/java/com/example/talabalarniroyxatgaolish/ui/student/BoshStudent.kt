package com.example.talabalarniroyxatgaolish.ui.student

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.example.talabalarniroyxatgaolish.R
import com.example.talabalarniroyxatgaolish.databinding.FragmentBoshStudentBinding
import com.example.talabalarniroyxatgaolish.db.MyDatabase
import com.example.talabalarniroyxatgaolish.ui.Login
import com.example.talabalarniroyxatgaolish.utils.Utils.myInfo
import com.google.android.material.navigation.NavigationView

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class BoshStudent : Fragment(), NavigationView.OnNavigationItemSelectedListener {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    lateinit var callback: OnBackPressedCallback
    private lateinit var myDatabase: MyDatabase
    private var binding: FragmentBoshStudentBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBoshStudentBinding.inflate(layoutInflater)
        myDatabase = MyDatabase(requireContext())
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_student, YigilishlarStudent())
            .commit()
        myInfo = myDatabase.getAuth()[0]
        binding!!.apply {
            bottomNavigationStudent.setOnItemSelectedListener {
                when(it.itemId) {
                    R.id.yigilish_student -> {
                        replaceFragment(YigilishlarStudent())
                    }
                    R.id.profil_drawer_student -> {
                        replaceFragmentDrawer(ProfileStudent())
                    }
                    R.id.davomat_student -> {
                        replaceFragment(DavomatStudent())
                    }
                }
                true
            }
            drawerNavigationStudent.setNavigationItemSelectedListener(this@BoshStudent)
            val toggle = ActionBarDrawerToggle(
                requireActivity(), drawerLayoutStudent, boshToolbarStudent,
                R.string.open_nav, R.string.close_nav
            )
            drawerLayoutStudent.addDrawerListener(toggle)
            val header = drawerNavigationStudent.getHeaderView(0)
            val tv = header.findViewById<TextView>(R.id.name_header)
            tv.text = myInfo!!.name
            toggle.syncState()
            callback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (drawerLayoutStudent.isDrawerOpen(GravityCompat.START)) {
                        drawerLayoutStudent.closeDrawer(GravityCompat.START)
                    } else {
                        isEnabled = false
                        requireActivity().onBackPressedDispatcher.onBackPressed()
                    }
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(requireActivity(), callback)
        return binding!!.root
    }

    private fun replaceFragment(fr: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_student, fr)
            .commit()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BoshStudent().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.settings_drawer_student -> {
                Toast.makeText(requireContext(), "Hozirda xavfsizlik bo'limi ishga tushmagan.", Toast.LENGTH_SHORT).show()
                //replaceFragmentDrawer(Settings())
            }
            R.id.xavfsizlik_drawer_student -> {
                Toast.makeText(requireContext(), "Hozirda xavfsizlik bo'limi ishga tushmagan.", Toast.LENGTH_SHORT).show()
                //replaceFragmentDrawer(Xavfsizlik())
            }
            R.id.baholash_drawer_student -> {
                Toast.makeText(requireContext(), "Hozirda baholash bo'limi ishga tushmagan.", Toast.LENGTH_SHORT).show()            }
            R.id.chiqish_drawer_student -> {
                myInfo = null
                myDatabase.deleteAuth()
                val fragmentManager = requireActivity().supportFragmentManager
                for (fragment in fragmentManager.fragments) {
                    fragmentManager.beginTransaction().remove(fragment).commit()
                }
                fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, Login())
                    .commit()
            }
        }
        return true
    }
    private fun replaceFragmentDrawer(fr: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_student, fr)
            .addToBackStack(null)
            .commit()
        if (binding!!.drawerLayoutStudent.isDrawerOpen(GravityCompat.START)) {
            binding!!.drawerLayoutStudent.closeDrawer(GravityCompat.START)
        }
    }
}