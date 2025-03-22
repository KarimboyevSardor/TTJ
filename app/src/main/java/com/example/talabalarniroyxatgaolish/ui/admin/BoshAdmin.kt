package com.example.talabalarniroyxatgaolish.ui.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import com.example.talabalarniroyxatgaolish.R
import com.example.talabalarniroyxatgaolish.databinding.FragmentBoshAdminBinding
import com.example.talabalarniroyxatgaolish.db.MyDatabase
import com.example.talabalarniroyxatgaolish.ui.DasturHaqida
import com.example.talabalarniroyxatgaolish.ui.Login
import com.example.talabalarniroyxatgaolish.ui.Settings
import com.example.talabalarniroyxatgaolish.ui.Xavfsizlik
import com.example.talabalarniroyxatgaolish.utils.Utils.myInfo
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class BoshAdmin : Fragment(), NavigationView.OnNavigationItemSelectedListener {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var binding: FragmentBoshAdminBinding? = null
    lateinit var callback: OnBackPressedCallback
    private lateinit var myDatabase: MyDatabase
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var toolbar1: Toolbar
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBoshAdminBinding.inflate(layoutInflater)
        myDatabase = MyDatabase(requireContext())
        myInfo = myDatabase.getAuth()[0]
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_admin, Tadbirlar())
            .commit()
        bottomNavigation = binding!!.bottomNavigationAdmin
        toolbar1 = binding!!.boshToolbarAdmin
        bottomNavigation.visibility = VISIBLE

        binding!!.apply {
            (requireActivity() as AppCompatActivity).setSupportActionBar(boshToolbarAdmin)
            bottomNavigationAdmin.setOnItemSelectedListener {
                when(it.itemId) {
                    R.id.yigilish_admin -> {
                        replaceFragment(Tadbirlar())
                    }
                    R.id.xona_admin -> {
                        replaceFragment(Xonalar())
                    }
                    R.id.davomat_admin -> {
                        replaceFragment(EskiDavomat())
                    }
                    R.id.adminlar_admin -> {
                        replaceFragment(Adminlar())
                    }
                }
                true
            }
            drawerNavigation.setNavigationItemSelectedListener(this@BoshAdmin)
            val toggle = ActionBarDrawerToggle(
                requireActivity(), drawerLayout, boshToolbarAdmin,
                R.string.open_nav, R.string.close_nav
            )
            drawerLayout.addDrawerListener(toggle)
            val header = drawerNavigation.getHeaderView(0)
            val tv = header.findViewById<TextView>(R.id.name_header)
            tv.text = myInfo!!.name
            toggle.syncState()
            callback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        drawerLayout.closeDrawer(GravityCompat.START)
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

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

    private fun replaceFragment(fr: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_admin, fr)
            .commit()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BoshAdmin().apply {
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
            R.id.profil_drawer_admin -> {
                replaceFragmentDrawer(Profile())
            }
            R.id.add_student_drawer_admin -> {
                replaceFragmentDrawer(AddStudent())
            }
            R.id.settings_drawer_admin -> {
                replaceFragmentDrawer(Settings())
            }
            R.id.xavfsizlik_drawer_admin -> {
                replaceFragmentDrawer(Xavfsizlik())
            }
            R.id.baholash_drawer_admin -> {
                Toast.makeText(requireContext(), "Baholash", Toast.LENGTH_SHORT).show()
            }
            R.id.dastur_haqida_drawer_admin -> {
                replaceFragmentDrawer(DasturHaqida())
            }
            R.id.chiqish_drawer_admin -> {
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
            .replace(R.id.fragment_container_admin, fr)
            .addToBackStack(null)
            .commit()
        bottomNavigation.visibility = View.GONE
        if (binding!!.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding!!.drawerLayout.closeDrawer(GravityCompat.START)
        }
    }
}