package com.greenspot.app.adapter


import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.greenspot.app.R
import com.greenspot.app.model.ListBookingDetails


class CustomAdapter(ctx: Context?, editModelArrayList: ArrayList<ListBookingDetails>?) :
    RecyclerView.Adapter<CustomAdapter.MyViewHolder>() {
    private val inflater: LayoutInflater

    private val editModelArrayList = editModelArrayList

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val view: View = inflater.inflate(R.layout.item_detailsbooking, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {

        Log.d("print", "yes")
    }

    override fun getItemCount(): Int {
        return editModelArrayList!!.size
    }

    inner class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var editText: EditText
        var editAge: EditText

        init {
            editText = itemView.findViewById(R.id.et_name)
            editAge = itemView.findViewById(R.id.et_age)
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i1: Int,
                    i2: Int
                ) {
                }

                override fun onTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i1: Int,
                    i2: Int
                ) {
                }

                override fun afterTextChanged(editable: Editable) {}
            })
        }
    }

    companion object {
        lateinit var editModelArrayList: ArrayList<ListBookingDetails>
    }

    init {
        inflater = LayoutInflater.from(ctx)

    }
}