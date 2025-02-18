package com.example.talabalarniroyxatgaolish.ui.admin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.talabalarniroyxatgaolish.adapter.BaholashAdapter
import com.example.talabalarniroyxatgaolish.data.Rate
import com.example.talabalarniroyxatgaolish.databinding.FragmentBaxolashViewPagerAdminBinding
import com.example.talabalarniroyxatgaolish.utils.Utils.rateList
import com.example.talabalarniroyxatgaolish.utils.Utils.tadbirlarList
import com.example.talabalarniroyxatgaolish.vm.BaholashViewPagerAdminVm
import com.example.talabalarniroyxatgaolish.vm.LiveDates
import kotlinx.coroutines.launch
import java.util.Calendar

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class BaxolashViewPager : Fragment() {
    private var param1: Long? = null
    private var param2: String? = null
    private val TAG = "BAHOLASHVIEWPAGER"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getLong(ARG_PARAM1)
        }
    }

    private var binding: FragmentBaxolashViewPagerAdminBinding? = null
    lateinit var liveDates: LiveDates
    lateinit var baholashAdapter: BaholashAdapter
    lateinit var baholashViewPagerAdminVm: BaholashViewPagerAdminVm
    private var baholangan = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBaxolashViewPagerAdminBinding.inflate(layoutInflater)
        liveDates = ViewModelProvider(requireActivity())[LiveDates::class]
        baholashViewPagerAdminVm = ViewModelProvider(requireActivity())[BaholashViewPagerAdminVm::class]
        val tadbir = tadbirlarList.filter { it.id == param1!!}[0]
        liveDates.baholashLiveData.value = rateList.filter { it.meeting_id == param1!! }.toMutableList()
        baholashAdapter = BaholashAdapter(rateList.filter { it.meeting_id == param1!! }.toMutableList(), requireContext(), requireActivity())
        liveDates.getRate().observe(requireActivity()) { it ->
            baholashAdapter.updateList(it.filter { it.meeting_id == param1!! }.toMutableList())
            Log.d(TAG, "onCreateView: LiveData ${it.filter { it.meeting_id == param1!! }.toMutableList()}")
        }
        binding!!.apply {
            recyclerView.adapter = baholashAdapter
            btnSubmit.setOnClickListener {
                if (liveDates.baholashLiveData.value!!.isNotEmpty()) {
                    editRate(liveDates.baholashLiveData.value!!)
                }
            }
            for (i in 0 until liveDates.baholashLiveData.value!!.size) {
                if (liveDates.baholashLiveData.value!![i].rate.toFloat() > 0.0) {
                    baholangan = true
                    break
                }
            }
            if (baholangan) {
                isEnabled.text = "Qayta baholash"
                isEnabled.isChecked = false
                isEmpty.visibility = View.VISIBLE
            } else {
                isEnabled.text = "Baholash"
                isEnabled.isChecked = true
                isEmpty.visibility = View.GONE
            }
            isEnabled.setOnCheckedChangeListener { compoundButton, b ->
                if (b) {
                    isEnabled.text = "Baholash"
                    isEmpty.visibility = View.GONE
                } else {
                    isEnabled.text = "Qayta baholash"
                    isEmpty.visibility = View.VISIBLE
                }
            }
            val date_time = tadbir.time.split(" ")
            val date = date_time[0].split("-")
            val calendar = Calendar.getInstance()
            val nowMonth = calendar.get(Calendar.MONTH) + 1
            val nowYear = calendar.get(Calendar.YEAR)
            val nowDay = calendar.get(Calendar.DAY_OF_MONTH)
            Log.d(TAG, "onCreateView: $nowDay, $nowMonth, $nowYear")
            if (date[1] == "12") {
                if (nowMonth == 1 && nowDay.toString() == date[0]) {
                    isEmpty.visibility = View.VISIBLE
                    isEnabled.visibility = View.GONE
                }
            } else {
                if (nowMonth == (date[1].toInt() + 1) && nowDay.toString() == date[0]) {
                    isEmpty.visibility = View.VISIBLE
                    isEnabled.visibility = View.GONE
                }
            }
            info.setOnClickListener {
                Toast.makeText(requireContext(), "Baholashni tadbir boshlanganidan 1 oygacha o'zgartirish mumkin.", Toast.LENGTH_SHORT).show()
            }
        }

        return binding!!.root
    }

    private fun editRate(value: MutableList<Rate>) {
        baholashViewPagerAdminVm.editRate(requireContext(), value, requireActivity())
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

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: ")
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: ")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ")
        liveDates.getRate().observe(requireActivity()) { it ->
            baholashAdapter.updateList(it.filter { it.meeting_id == param1!! }.toMutableList())
        }
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: ")
    }
}