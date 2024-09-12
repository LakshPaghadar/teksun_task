package com.teksun.task.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.room.Room
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.teksun.task.R
import com.teksun.task.database.FundDatabase
import com.teksun.task.database.FundTable
import com.teksun.task.databinding.AddFundScreenBinding
import com.teksun.task.network.AuthViewModel
import com.teksun.task.network.response.SearchFundName
import com.teksun.task.network.response.SearchFundNameItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

@AndroidEntryPoint
class AddFundScreenFragment : Fragment() {
    val videmodel: AuthViewModel by viewModels()
    lateinit var binding: AddFundScreenBinding
    var fundList = arrayListOf<String>();
    var searchResponse: SearchFundName? = null
    lateinit var arrayAdapter: ArrayAdapter<String>

    var selectedDate: Long? = null;
    var selectedFund: SearchFundNameItem? = null;
    var selectedSpi: String? = null

    lateinit var database: FundDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddFundScreenBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textChangeListener()
        observeResponse()
        setAutoCompleteText()
        setOnClick()
    }

    private fun initDatabase() {
        //database=Room.databaseBuilder(activity?.applicationContext!!,FundDatabase::class.java,"fundDB").build()
        database = FundDatabase.getDatabaseFund(requireContext())

    }

    private fun setOnClick() {
        binding.editTextSIPDate.setOnClickListener {
            pickDate()
        }
        binding.buttonSave.setOnClickListener {
            if (validate()) {
                initDatabase()
                GlobalScope.launch {
                    database.fundDao().insertFund(
                        FundTable(
                            fund_name = binding.editTextSearchFund.text.toString(),
                            fund_code = selectedFund?.schemeCode.toString(),
                            amount = binding.editTextAmount.text.toString(),
                            date = binding.editTextSIPDate.text.toString(),
                        )
                    )
                }
                parentFragmentManager.popBackStack()
            }
        }
    }

    private fun findFundCode(): SearchFundNameItem? {
        var fund_name = binding.editTextSearchFund.text.toString()
        var a = searchResponse?.find { fund -> fund.schemeName == fund_name }
        return a
    }

    private fun setAutoCompleteText() {
        arrayAdapter = ArrayAdapter<String>(requireContext(), R.layout.drop_down_menu, fundList)
        binding.editTextSearchFund.setAdapter(arrayAdapter)
    }

    private fun textChangeListener() {
        binding.editTextSearchFundField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                binding.editTextSearchFund.text?.clear()
                if ((activity as MainActivity).checkInternet()) {
                    (activity as MainActivity).showLoader(true)
                    callApi(p0?.trim().toString())
                } else {
                    Toast.makeText(requireContext(), "No Internet", Toast.LENGTH_SHORT).show()
                }

            }
        })
    }

    private fun validate(): Boolean {
        selectedFund = findFundCode()
        binding.editTextSearchFund.clearFocus()
        binding.editTextSIPDate.clearFocus()
        binding.editTextAmount.clearFocus()
        if (binding.editTextSearchFund.text.toString().isBlank() || selectedFund == null) {
            Toast.makeText(requireContext(), "Please select fund", Toast.LENGTH_SHORT).show()
            return false;
        } else if (binding.editTextSIPDate.text.toString().isBlank()) {
            Toast.makeText(requireContext(), "Please select date", Toast.LENGTH_SHORT).show()
            return false;
        } else if (binding.editTextAmount.text.toString().isBlank()) {
            Toast.makeText(requireContext(), "Please enter amount", Toast.LENGTH_SHORT).show()
            return false;
        } else if (binding.editTextAmount.text.toString().toInt() < 500) {
            Toast.makeText(
                requireContext(),
                "Please enter amount higher than 500",
                Toast.LENGTH_SHORT
            ).show()
            return false;
        }
//        else if (dateValidate()) {
//            return true
//        }
        else {
            return true
        }
    }

    private fun dateValidate(): Boolean {
        if (selectedDate != null) {
            val date = Date(selectedDate!!)
            val simpleDateFormat = SimpleDateFormat("dd")
            val dateText = simpleDateFormat.format(date).toInt()
            if (dateText >= 5 && dateText <= 25) {
                return true;
            } else {
                Toast.makeText(
                    requireContext(),
                    "Please select date between 5 to 25",
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
        } else {
            Toast.makeText(requireContext(), "Please select date", Toast.LENGTH_SHORT).show()
            return false
        }
    }

    private fun callApi(search: String) {
        videmodel.searchApi(search) {
            (activity as MainActivity).showLoader(false)
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun pickDate() {
        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setValidator(DateValidatorPointBackward.now())

        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setCalendarConstraints(constraintsBuilder.build())
                .setTitleText("Select date")
                .build()

        datePicker.addOnPositiveButtonClickListener {
            // Respond to positive button click.
            selectedDate = it

            val date = Date(it)
            val simpleDateFormat = SimpleDateFormat("dd-MMM-yyyy")
            val dateText = simpleDateFormat.format(date)
            binding.editTextSIPDate.setText(dateText.toString())

            if (dateValidate()) {

            } else {
                binding.editTextSIPDate.text?.clear()
            }

        }

        datePicker.show(parentFragmentManager, "tag");
    }

    private fun observeResponse() {
        videmodel.searchApiResponse.observe(viewLifecycleOwner) {
            (activity as MainActivity).showLoader(false)
            Log.e("RESPONSE_API", it.body().toString())
            if (it.isSuccessful) {
                searchResponse = it.body()
                Log.e("RESPONSE_API", it.body()?.size.toString())
                var data = it.body()
                var list = arrayListOf<String>()
                data?.forEach {
                    list.add(it.schemeName)
                }
                fundList.addAll(list)
                arrayAdapter.notifyDataSetChanged()
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.editTextSearchFund.showDropDown()
                }, 1000)

            }
        }
    }
}