package com.greenspot.app.adapter

import android.app.DatePickerDialog
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.greenspot.app.R
import com.greenspot.app.model.ListBookingDetails
import java.util.*


class BookDetailsAdapter(val context: FragmentActivity?) :
    RecyclerView.Adapter<BookDetailsAdapter.FeatureViewHolder>() {

    private var data: List<ListBookingDetails> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeatureViewHolder {
        return FeatureViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_bookdetails, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: FeatureViewHolder, position: Int) {

        holder.bind(data[position], context)
        val movie = data[position]


        holder.title.setOnClickListener(View.OnClickListener {
            val expanded = movie.isExpanded()
            movie.setExpanded(!expanded)
            notifyItemChanged(position)

        })


    }


    fun swapData(data: List<ListBookingDetails>) {
        this.data = data
        notifyDataSetChanged()
    }

    class FeatureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        public lateinit var title: TextView

        fun bind(
            item: ListBookingDetails,
            context: FragmentActivity?
        ) = with(itemView) {


            val laydetails: LinearLayout
            val etfname: EditText
            val etlname: EditText
            val dob: EditText
            val gender: EditText
            val email: EditText
            val contactno: EditText
            val spGender: Spinner
            val rbGender: RadioGroup

            title = itemView.findViewById(R.id.txt_listTitle)
            etfname = itemView.findViewById(R.id.et_fname)
            etlname = itemView.findViewById(R.id.et_lname)
            dob = itemView.findViewById(R.id.et_dob)
            gender = itemView.findViewById(R.id.et_gender)
            email = itemView.findViewById(R.id.et_bookinemail)
            contactno = itemView.findViewById(R.id.et_bookingnumber)
            spGender = itemView.findViewById(R.id.sp_gender)
            laydetails = itemView.findViewById(R.id.lay_details)
            rbGender = itemView.findViewById(R.id.rg_gender)


            val expanded = item.isExpanded()
            laydetails.visibility = if (expanded) View.VISIBLE else View.GONE


            if (expanded) {

                title.setCompoundDrawablesWithIntrinsicBounds(
                    null, null, ContextCompat.getDrawable(
                        context!!, R.drawable.drawable_collops
                    ), null
                )

            } else {

                title.setCompoundDrawablesWithIntrinsicBounds(
                    null, null, ContextCompat.getDrawable(
                        context!!, R.drawable.drawable_add
                    ), null
                )

            }


            etfname.addTextChangedListener(object : TextWatcher {
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
                    item.first_name = etfname.text.toString()
//                    editModelArrayList?.get(this@MyViewHolder.adapterPosition)!!.name = editText.text.toString()
                }

                override fun afterTextChanged(editable: Editable) {}
            })

            etlname.addTextChangedListener(object : TextWatcher {
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

                    item.last_name = etlname.text.toString()
//                    editModelArrayList?.get(this@MyViewHolder.adapterPosition)!!.name = editText.text.toString()
                }

                override fun afterTextChanged(editable: Editable) {}
            })

            email.addTextChangedListener(object : TextWatcher {
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


                    item.email = email.text.toString()
//                    editModelArrayList?.get(this@MyViewHolder.adapterPosition)!!.name = editText.text.toString()
                }

                override fun afterTextChanged(editable: Editable) {}
            })

            contactno.addTextChangedListener(object : TextWatcher {
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

                    item.contact_number = contactno.text.toString()
//                    editModelArrayList?.get(this@MyViewHolder.adapterPosition)!!.name = editText.text.toString()
                }

                override fun afterTextChanged(editable: Editable) {}
            })


            val c = Calendar.getInstance()

            val year = c.get(Calendar.YEAR);
            val month = c.get(Calendar.MONTH);
            val day = c.get(Calendar.DAY_OF_MONTH);


            val dialog = DatePickerDialog(
                context,
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    val _year = year.toString()
                    val _month = if (month + 1 < 10) "0" + (month + 1) else (month + 1).toString()
                    val _date = if (dayOfMonth < 10) "0$dayOfMonth" else dayOfMonth.toString()
                    val _pickedDate = "$_month/$_date/$year"
                    Log.e("PickedDate: ", "Date: $_pickedDate") //
                    dob.setText(_pickedDate)// 2019-02-12
                    item.dob = _pickedDate
                },year, month, day
            )

            dialog.datePicker.maxDate = System.currentTimeMillis() - 1000



            dob.setOnClickListener(View.OnClickListener {


                dialog.show()
            })

            dob.setOnLongClickListener(OnLongClickListener {
                //  Do Something or Don't
                true
            })
            gender.setOnLongClickListener(OnLongClickListener {
                //  Do Something or Don't
                true
            })
            gender.setOnClickListener(View.OnClickListener {
                spGender.visibility = View.VISIBLE
                spGender.performClick()

            })
            val arrayForSpinner =
                arrayOf(context.getString(R.string.res_male), context.getString(R.string.res_female))
            val adapter: ArrayAdapter<String> =
                ArrayAdapter<String>(context, R.layout.spinner_item, arrayForSpinner)
            spGender.setAdapter(adapter);



            spGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    (parent.getChildAt(0) as TextView).visibility = View.GONE
                    print("onItemSelected position = $position id = $id")
                    val selectedItem = parent.getItemAtPosition(position).toString()

                    Log.e("bookingdetails", "selectitem " + selectedItem)

                    spGender.visibility = View.GONE

                    if (selectedItem.equals("Male")) {

                        item.gender = "M"

                    }

                    if (selectedItem.equals("Female")) {

                        item.gender = "F"

                    }


                    gender.setText(selectedItem)

                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }

            }

            item.title = "Mr"

            rbGender.setOnCheckedChangeListener(
                RadioGroup.OnCheckedChangeListener { group, checkedId ->
                    val radio: RadioButton = findViewById(checkedId)
                    Log.e("bookingdetails", "radio " + radio.text)

                    if (radio.text.equals("Mr.")) {

                        item.title = "Mr"

                    }
                    if (radio.text.equals("Mrs.")) {
                        item.title = "Mrs"

                    }
                    if (radio.text.equals("Miss")) {
                        item.title = "Miss"
                    }


//                    Toast.makeText(context," On checked change : ${radio.text}",
//                        Toast.LENGTH_SHORT).show()
                })


//            editModelArrayList?.get(this@MyViewHolder.adapterPosition)!!.name = editText.text.toString()


            //https://demonuts.com/android-recyclerview-with-edittext/


            title.setText(item.person)
            etfname.setText(item.first_name)
            etlname.setText(item.last_name)
            email.setText(item.email)
            contactno.setText(item.contact_number)


//            val placeDetailsAdapter = PlaceDetailsAdapter(context)
//            Common.setVerticalRecyclerView(context!!, rvPlacedetails)
//            placeDetailsAdapter.swapData(item.subItem)
//            rvPlacedetails.adapter = placeDetailsAdapter


        }


    }


}



