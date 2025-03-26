package com.example.talabalarniroyxatgaolish.utils

import com.example.talabalarniroyxatgaolish.data.AdminDataItem
import com.example.talabalarniroyxatgaolish.data.Auth
import com.example.talabalarniroyxatgaolish.data.DavomatDataItem
import com.example.talabalarniroyxatgaolish.data.Date
import com.example.talabalarniroyxatgaolish.data.Oy
import com.example.talabalarniroyxatgaolish.data.Rate
import com.example.talabalarniroyxatgaolish.data.StudentDataItem
import com.example.talabalarniroyxatgaolish.data.StudentInfo
import com.example.talabalarniroyxatgaolish.data.XonaDataItem
import com.example.talabalarniroyxatgaolish.data.TadbirlarDataItem

object Utils {
    var tadbirlarList: MutableList<TadbirlarDataItem> = mutableListOf()
    var davomatList: MutableList<DavomatDataItem> = mutableListOf()
    var xonalarList: MutableList<XonaDataItem> = mutableListOf()
    var studentlarList: MutableList<StudentDataItem> = mutableListOf()
    var addedTadbirStudentList: MutableList<StudentDataItem> = mutableListOf()
    var rateList: MutableList<Rate> = mutableListOf()
    var dates: MutableList<Date> = mutableListOf()
    var oyList: MutableList<Oy> = mutableListOf()
    var currentDateDavomat: MutableList<DavomatDataItem> = mutableListOf()
    var adminsList: MutableList<AdminDataItem> = mutableListOf()
    var myInfo: Auth? = null
    var studentInfoData: StudentInfo? = null
}