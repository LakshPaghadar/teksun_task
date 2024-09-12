package com.teksun.task.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teksun.task.database.FundTable
import com.teksun.task.databinding.RowFileFundListBinding

class FundListAdapter(var list: ArrayList<FundTable>):RecyclerView.Adapter<FundListAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: RowFileFundListBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RowFileFundListBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
       return list.size;
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item=list[position]
        holder.binding.textViewNameValue.text=item.fund_name
        holder.binding.textViewInvestValue.text=item.amount
        holder.binding.textViewCurrentValue.text=item.date
        holder.binding.textViewROIValue.text=item.fund_code
    }
}