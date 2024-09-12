package com.teksun.task

import android.app.ProgressDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class BaseActivity:AppCompatActivity() {


    lateinit var progrssDialog:ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        progrssDialog=ProgressDialog(this)
        progrssDialog.setMessage("Loading")
        progrssDialog.setCancelable(false)
        progrssDialog.setCanceledOnTouchOutside(false)


        super.onCreate(savedInstanceState)
    }
    fun showLoader(flag:Boolean){
        if (flag){
            if (!progrssDialog.isShowing){
                progrssDialog.show()
            }
        } else {
            if (progrssDialog.isShowing){
                progrssDialog.dismiss()
            }
        }
    }
    fun loadFragment(fragment: Fragment,isAdd:Boolean,isBackStack:Boolean){
        var manager=supportFragmentManager
        var transaction=manager.beginTransaction()
        if (isAdd){
            transaction.add(R.id.placeHolder,fragment,fragment::class.java.simpleName)
        } else {
            transaction.replace(R.id.placeHolder,fragment,fragment::class.java.simpleName)
        }
        if (isBackStack){
            transaction.addToBackStack(fragment::class.java.simpleName)
        }
        transaction.commit()

    }
}