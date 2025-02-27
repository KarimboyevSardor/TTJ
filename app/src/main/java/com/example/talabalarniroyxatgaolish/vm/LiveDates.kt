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
    var qoshilganStudentLiveData: MutableLiveData<MutableList<StudentDataItem>> = MutableLiveData()
    var qoshiluvchiStudentLiveData: MutableLiveData<MutableList<StudentDataItem>> = MutableLiveData()
    var addedTadbirStudentLiveData: MutableLiveData<MutableList<StudentDataItem>> = MutableLiveData()
    var addTadbirStudentLiveData: MutableLiveData<MutableList<StudentDataItem>> = MutableLiveData()
    var rateLiveData: MutableLiveData<MutableList<Rate>> = MutableLiveData()
    var baholashLiveData: MutableLiveData<MutableList<Rate>> = MutableLiveData()
    var xonaDavomatLiveData: MutableLiveData<MutableList<DavomatDataItem>> = MutableLiveData()
    var xonaStudentLiveDate: MutableLiveData<MutableList<StudentDataItem>> = MutableLiveData()

    fun getStudentRoom() : MutableLiveData<MutableList<StudentDataItem>>{
        return xonaStudentLiveDate
    }

    fun getXonaDavomat() : MutableLiveData<MutableList<DavomatDataItem>> {
        return xonaDavomatLiveData
    }

    fun getAddTadbirStudent() : MutableLiveData<MutableList<StudentDataItem>> {
        return addTadbirStudentLiveData
    }

    fun getAddedTadbirStudent() : MutableLiveData<MutableList<StudentDataItem>> {
        return addedTadbirStudentLiveData
    }

    fun getBaholash() : MutableLiveData<MutableList<Rate>> {
        return baholashLiveData
    }

    fun getRate() : MutableLiveData<MutableList<Rate>> {
        return rateLiveData
    }

    fun getQoshiluvchiStudent() : MutableLiveData<MutableList<StudentDataItem>> {
        return qoshiluvchiStudentLiveData
    }

    fun getQoshilganStudent() : MutableLiveData<MutableList<StudentDataItem>> {
        return qoshilganStudentLiveData
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