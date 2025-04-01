package com.example.talabalarniroyxatgaolish.ui.student

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.talabalarniroyxatgaolish.R
import com.example.talabalarniroyxatgaolish.adapter.CalendarViewPagerAdapter
import com.example.talabalarniroyxatgaolish.adapter.CalendarViewPagerAdapterStudent
import com.example.talabalarniroyxatgaolish.adapter.DavomatAdapter
import com.example.talabalarniroyxatgaolish.data.Oy
import com.example.talabalarniroyxatgaolish.databinding.FragmentDavomatStudentBinding
import com.example.talabalarniroyxatgaolish.utils.Utils.davomatList
import com.example.talabalarniroyxatgaolish.utils.Utils.myInfo
import com.example.talabalarniroyxatgaolish.utils.Utils.oyList
import com.example.talabalarniroyxatgaolish.vm.DavomatStudentVm
import com.example.talabalarniroyxatgaolish.vm.EskiDavomatAdminVm
import com.example.talabalarniroyxatgaolish.vm.LiveDates
import com.example.talabalarniroyxatgaolish.vm.Resource
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class DavomatStudent : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var oy = 0
    private var currentMonth = 0
    lateinit var calendarViewPagerAdapter: CalendarViewPagerAdapterStudent
    lateinit var liveDates: LiveDates
    lateinit var davomatAdapter: DavomatAdapter
    private var binding: FragmentDavomatStudentBinding? = null
    private val TAG = "DAVOMATSTUDENT"
    lateinit var davomatStudentVm: DavomatStudentVm
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDavomatStudentBinding.inflate(layoutInflater)
        liveDates = ViewModelProvider(requireActivity())[LiveDates::class]
        val calendar = Calendar.getInstance()
        oy = calendar.get(Calendar.MONTH) + 1
        currentMonth = oy
        davomatStudentVm = ViewModelProvider(requireActivity())[DavomatStudentVm::class]
        setDate()
        getDavomat()
        isCheckEmpty()
        calendarViewPagerAdapter = CalendarViewPagerAdapterStudent(this,currentMonth + 12)
        davomatAdapter = DavomatAdapter(davomatList.filter { !it.is_there && it.student_id == myInfo!!.id }.toMutableList(), requireContext())
        liveDates.getDavomat().observe(requireActivity()) { it ->
            davomatAdapter.updateList(it.filter { !it.is_there && it.student_id == myInfo!!.id }.toMutableList())
        }
        binding!!.apply {
            studentShimmer.startShimmer()
            viewpager2.adapter = calendarViewPagerAdapter
            viewpager2.layoutDirection = View.LAYOUT_DIRECTION_LTR
            viewpager2.setCurrentItem(calendarViewPagerAdapter.itemCount - 1, false)
            viewpager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                }
            })

            studentDavomatRv.adapter = davomatAdapter
        }

        return binding!!.root
    }

    private fun isCheckEmpty() {
        if (davomatList.isNotEmpty()) {
            binding!!.studentShimmer.stopShimmer()
            binding!!.studentShimmer.visibility = GONE
            binding!!.studentDavomatRv.visibility = VISIBLE
        }
    }

    private fun getDavomat() {
        lifecycleScope.launch {
            if (isAdded) {
                try {
                    davomatStudentVm.getDavomat(requireContext())
                    davomatStudentVm._davomat.collect {
                        when (it) {
                            is Resource.Error -> {
                                Toast.makeText(
                                    requireContext(),
                                    "Server bilan bog'lanib bo'lmadi.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                binding!!.studentShimmer.stopShimmer()
                                binding!!.studentShimmer.visibility = GONE
                            }

                            is Resource.Loading -> {}
                            is Resource.Success -> {
                                davomatList = it.data
                                liveDates.davomatLiveData.value = davomatList
                                isCheckEmpty()
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.d(TAG, "getDavomat: ${e.message}")
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: ")
    }

    fun formatDate(date: String): String {
        val parts = date.split("-")
        if (parts.size != 3) return date
        val year = parts[0]
        val month = parts[1].toInt().toString()
        val day = parts[2].toInt().toString()
        return "$year-$month-$day"
    }

    private fun setDate() {
        val calendar = Calendar.getInstance()
        val currentOy = calendar.get(Calendar.MONTH) + 1
        val currentYil = calendar.get((Calendar.YEAR))
        var i = currentOy + 12
        while (i >= 1) {
            if (i > 12) {
                val oy = i % 12
                oyList.add(Oy(getMonthName(currentYil, i), oy, currentYil))
            } else {
                oyList.add(Oy(getMonthName(currentYil - 1, i), i, currentYil - 1))
            }
            i--
        }
        oyList.reverse()
    }

    fun getMonthName(year: Int, month: Int): String {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month - 1)
        }
        val sdf = SimpleDateFormat("MMMM", Locale("uz"))
        return sdf.format(calendar.time)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DavomatStudent().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}