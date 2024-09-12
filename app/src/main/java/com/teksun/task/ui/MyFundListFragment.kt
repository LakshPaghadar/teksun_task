package com.teksun.task.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teksun.task.database.FundDatabase
import com.teksun.task.database.FundTable
import com.teksun.task.databinding.FundListScreenBinding
import com.teksun.task.network.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MyFundListFragment:Fragment() {

    val videmodel: AuthViewModel by viewModels()
    lateinit var binding: FundListScreenBinding
    lateinit var fundAdapter:FundListAdapter
    lateinit var database: FundDatabase
    var fundsTableList= arrayListOf<FundTable>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FundListScreenBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        setOnClickListner()
        observeResponse()
        initDatabase()
    }

    override fun onResume() {
        super.onResume()
    }
    private fun initDatabase(){
        database= FundDatabase.getDatabaseFund(requireContext())
        database.fundDao().getFunds().observe(viewLifecycleOwner){
            fundsTableList.clear()
            fundsTableList.addAll(it)
            fundAdapter.notifyDataSetChanged()
            if ((activity as MainActivity).checkInternet()){
                it.forEach { it1->
                    if (it1.fund_code!=""){
                        callApi(it1.fund_code)
                    }
                }
            } else {
                Toast.makeText(requireContext(),"No Internet",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun callApi(code:String){
        videmodel.detailsApi(code){
            Toast.makeText(requireContext(),it,Toast.LENGTH_SHORT).show()
        }
    }
    private fun observeResponse(){
        videmodel.detailsApiResponse.observe(viewLifecycleOwner){
            (activity as MainActivity).showLoader(false)
            Log.e("RESPONSE_API",it.body().toString())
            if (it.isSuccessful){

            }
        }
    }

    private fun setAdapter(){
        fundAdapter=FundListAdapter(fundsTableList)
        binding.recyclerViewFundList.apply {
            adapter=fundAdapter
            layoutManager=LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
        }
    }
    private fun setOnClickListner(){
        binding.buttonAddFund.setOnClickListener {
            (activity as MainActivity).loadFragment(AddFundScreenFragment(),false,true)
        }
    }


}