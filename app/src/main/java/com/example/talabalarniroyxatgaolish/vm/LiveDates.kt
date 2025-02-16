package com.example.talabalarniroyxatgaolish.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.talabalarniroyxatgaolish.data.DavomatDataItem
import com.example.talabalarniroyxatgaolish.data.Rate
import com.example.talabalarniroyxatgaolish.data.StudentDataItem
import com.example.talabalarniroyxatgaolish.data.XonaDataItem
import com.example.talabalarniroyxatgaolish.data.TadbirlarDataItem

class LiveDates : ViewModel() {
    var tarbirlarLiveData: MutableLiveData<MutableList<TadbirlarDataItem>> = MutableLiveData()
    var davomatLiveData: MutableLiveData<MutableList<DavomatDataItem>> = MutableLiveData()
    var xonalarLiveData: MutableLiveData<MutableList<XonaDataItem>> = MutableLiveData()
    var studentlarLiveData: MutableLiveData<MutableList<StudentDataItem>> = MutableLiveData()
    var addedTadbirStudentLiveData: MutableLiveData<MutableList<StudentDataItem>> = MutableLiveData()
    var addTadbirStudentLiveData: MutableLiveData<MutableList<StudentDataItem>> = MutableLiveData()
    var rateLiveData: MutableLiveData<MutableList<Rate>> = MutableLiveData()

    fun getRate() : MutableLiveData<MutableList<Rate>> {
        return rateLiveData
    }

    fun getAddTadbirStudent() : MutableLiveData<MutableList<StudentDataItem>> {
        return addTadbirStudentLiveData
    }

    fun getAddedTadbirStudent() : MutableLiveData<MutableList<StudentDataItem>> {
        return addedTadbirStudentLiveData
    }

    fun getYigilish() : MutableLiveData<MutableList<TadbirlarDataItem>> {
        return tarbirlarLiveData
    }

    fun getDavomat() : MutableLiveData<MutableList<DavomatDataItem>> {
        return davomatLiveData
    }

    fun getXonalar() : MutableLiveData<MutableList<XonaDataItem>> {
        return xonalarLiveData
    }

    fun getStudentlar() : MutableLiveData<MutableList<StudentDataItem>> {
        return studentlarLiveData
    }
}