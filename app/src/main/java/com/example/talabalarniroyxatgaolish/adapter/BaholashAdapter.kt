package com.example.talabalarniroyxatgaolish.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.talabalarniroyxatgaolish.R
import com.example.talabalarniroyxatgaolish.data.Rate
import com.example.talabalarniroyxatgaolish.databinding.BaholashRvItemBinding
import com.example.talabalarniroyxatgaolish.vm.LiveDates

class BaholashAdapter(var rates: MutableList<Rate>, val context: Context, val activity: FragmentActivity) : Adapter<BaholashAdapter.BaholashAdapterVh>() {

    private val TAG = "BAHOLASHADAPTER"
    fun updateList(rates: MutableList<Rate>) {
        val diffCallback = BaholashDiffUtil(this.rates, rates)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.rates = rates
        diffResult.dispatchUpdatesTo(this)
    }

    class BaholashDiffUtil(
        private val oldList: List<Rate>,
        private val newList: List<Rate>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
    lateinit var liveDates: LiveDates

    inner class BaholashAdapterVh(val binding: BaholashRvItemBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaholashAdapterVh {
        return BaholashAdapterVh(BaholashRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return rates.size
    }
    private var zor = false
    private var yaxshi = false
    private var orta = false
    private var bolmaydi = false
    private var yomon = false
    private val korinmas = ContextCompat.getColor(context, R.color.korinmas)
    private val korinar = ContextCompat.getColor(context, R.color.white)

    override fun onBindViewHolder(holder: BaholashAdapterVh, position: Int) {
        liveDates = ViewModelProvider(activity)[LiveDates::class]
        Log.d(TAG, "onBindViewHolder: Adapter ${liveDates.baholashLiveData.value}")
        holder.binding.zor.text = "\uD83D\uDE0D"
        holder.binding.yaxshi.text = "\uD83D\uDE42"
        holder.binding.orta.text = "\uD83D\uDE10"
        holder.binding.bolmaydi.text = "\uD83D\uDE41"
        holder.binding.yomon.text = "\uD83D\uDE21"
        val rate = rates[position]
        holder.binding.txtStudentName.text = rate.name
        if (rate.emoji != "") {
            when (rate.emoji) {
                "\uD83D\uDE0D" -> {
                    holder.binding.zor.setTextColor(korinar)
                }
                "\uD83D\uDE42" -> {
                    holder.binding.yaxshi.setTextColor(korinar)
                }
                "\uD83D\uDE10" -> {
                    holder.binding.orta.setTextColor(korinar)
                }
                "\uD83D\uDE41" -> {
                    holder.binding.bolmaydi.setTextColor(korinar)
                }
                "\uD83D\uDE21" -> {
                    holder.binding.yomon.setTextColor(korinar)
                }
            }
        }
        holder.binding.ratingBar.rating = rate.rate.toFloat()

        holder.binding.zor.setOnClickListener { view ->
            if (!zor) {
                rates[position].emoji = holder.binding.zor.text.toString()
                liveDates.baholashLiveData.value = rates
                allFalse(holder.binding.orta, holder.binding.yaxshi, holder.binding.yomon, holder.binding.bolmaydi, "zor")
                zor = true
                holder.binding.zor.setTextColor(korinar)
            } else {
                zor = false
                holder.binding.zor.setTextColor(korinmas)
            }
        }
        holder.binding.yaxshi.setOnClickListener { view ->
            if (!yaxshi) {
                rates[position].emoji = holder.binding.yaxshi.text.toString()
                liveDates.baholashLiveData.value = rates
                allFalse(holder.binding.orta, holder.binding.zor, holder.binding.yomon, holder.binding.bolmaydi, "yaxshi")
                yaxshi = true
                holder.binding.yaxshi.setTextColor(korinar)
            } else {
                yaxshi = false
                holder.binding.yaxshi.setTextColor(korinmas)
            }
        }
        holder.binding.orta.setOnClickListener { view ->
            if (!orta) {
                rates[position].emoji = holder.binding.orta.text.toString()
                liveDates.baholashLiveData.value = rates
                allFalse(holder.binding.zor, holder.binding.yaxshi, holder.binding.yomon, holder.binding.bolmaydi, "orta")
                orta = true
                holder.binding.orta.setTextColor(korinar)
            } else {
                orta = false
                holder.binding.orta.setTextColor(korinmas)
            }
        }
        holder.binding.bolmaydi.setOnClickListener { view ->
            if (!bolmaydi) {
                rates[position].emoji = holder.binding.bolmaydi.text.toString()
                liveDates.baholashLiveData.value = rates
                allFalse(holder.binding.orta, holder.binding.yaxshi, holder.binding.yomon, holder.binding.zor, "bolmaydi")
                bolmaydi = true
                holder.binding.bolmaydi.setTextColor(korinar)
            } else {
                bolmaydi = false
                holder.binding.bolmaydi.setTextColor(korinmas)
            }
        }
        holder.binding.yomon.setOnClickListener { view ->
            if (!yomon) {
                rates[position].emoji = holder.binding.yomon.text.toString()
                liveDates.baholashLiveData.value = rates
                allFalse(holder.binding.orta, holder.binding.yaxshi, holder.binding.zor, holder.binding.bolmaydi, "yomon")
                yomon = true
                holder.binding.yomon.setTextColor(korinar)
            } else {
                yomon = false
                holder.binding.yomon.setTextColor(korinmas)
            }
        }
        holder.binding.ratingBar.setOnRatingBarChangeListener { ratingBar, ball, b ->
            rates[position].rate = ball.toString()
            liveDates.baholashLiveData.value = rates
        }
    }

    private fun allFalse(tv1: TextView, tv2: TextView, tv3: TextView, tv4: TextView, check: String) {
        tv1.setTextColor(korinmas)
        tv2.setTextColor(korinmas)
        tv3.setTextColor(korinmas)
        tv4.setTextColor(korinmas)
        when (check) {
            "zor" -> {
                yaxshi = false
                orta = false
                bolmaydi = false
                yomon = false
            }
            "yaxshi" -> {
                zor = false
                orta = false
                bolmaydi = false
                yomon = false
            }
            "orta" -> {
                yaxshi = false
                zor = false
                bolmaydi = false
                yomon = false
            }
            "bolmaydi" -> {
                yaxshi = false
                orta = false
                zor = false
                yomon = false
            }
            "yomon" -> {
                yaxshi = false
                orta = false
                bolmaydi = false
                zor = false
            }
        }
    }
}