package com.greenspot.app.adapter

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.greenspot.app.R
import com.greenspot.app.interfaces.ItemClickListener
import com.greenspot.app.model.ListAddGuestDetails
import com.greenspot.app.utils.Common
import com.makeramen.roundedimageview.RoundedImageView
import kotlinx.android.synthetic.main.dialog_contry.*


class HotelRoomDetailsAdapter(
    val context: FragmentActivity?,
    val itemClickListener: ItemClickListener
) : RecyclerView.Adapter<HotelRoomDetailsAdapter.FeatureViewHolder>(), ItemClickListener {

    private lateinit var dialog: Dialog
    private var data: ArrayList<ListAddGuestDetails> = ArrayList()
    private val mContext: Context? = context

    var values = arrayOf("1", "2", "3", "4")


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeatureViewHolder {
        return FeatureViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_hotelroom, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: FeatureViewHolder, position: Int) {

        holder.bind(data[position], context)
        val movie = data[position]

        if (position == 0) {
            holder.ivDeleteroom.visibility = View.GONE
        }


        holder.ivDeleteroom.setOnClickListener(View.OnClickListener {

            removeItem(position)
            itemClickListener.recyclerViewListClicked(it, position, 1)

        })


/*
        holder.title.setOnClickListener(View.OnClickListener {
            val expanded = movie.isExpanded()
            movie.setExpanded(!expanded)
            notifyItemChanged(position)

        })*/


    }


    fun swapData(data: ArrayList<ListAddGuestDetails>) {
        this.data = data
        notifyDataSetChanged()
    }

    class FeatureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        public lateinit var title: TextView

        public lateinit var ivDeleteroom: RoundedImageView


        fun bind(
            item: ListAddGuestDetails,
            context: FragmentActivity?
        ) = with(itemView) {

            val txtRoom: TextView


            val lay_child1view: LinearLayout
            val lay_child2view: LinearLayout
            val lay_child3view: LinearLayout
            val lay_child4view: LinearLayout
            val lay_childage: LinearLayout

            val sp_adult: Spinner
            val sp_child: Spinner
            val sp_child1: Spinner
            val sp_child2: Spinner
            val sp_child3: Spinner
            val sp_child4: Spinner




            var flag = false

            txtRoom = itemView.findViewById(R.id.txt_room)



            lay_childage = itemView.findViewById(R.id.lay_childage)

            lay_child1view = itemView.findViewById(R.id.lay_child1view)
            lay_child2view = itemView.findViewById(R.id.lay_child2view)
            lay_child3view = itemView.findViewById(R.id.lay_child3view)
            lay_child4view = itemView.findViewById(R.id.lay_child4view)

            sp_adult = itemView.findViewById(R.id.sp_adult)
            sp_child = itemView.findViewById(R.id.sp_child)
            sp_child1 = itemView.findViewById(R.id.sp_child1)
            sp_child2 = itemView.findViewById(R.id.sp_child2)
            sp_child3 = itemView.findViewById(R.id.sp_child3)
            sp_child4 = itemView.findViewById(R.id.sp_child4)




            ivDeleteroom = itemView.findViewById(R.id.iv_delteroom)
            txtRoom.text = item.room


            val arrayForchild = arrayOf("0", "1", "2", "3", "4")
            val arrayForadult = arrayOf("1", "2", "3", "4")
            val arrayForchildage =
                arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12")

            val childadapter: ArrayAdapter<String> =
                ArrayAdapter<String>(context!!, R.layout.spinner_itemage, arrayForchild)

            val adultadapter: ArrayAdapter<String> =
                ArrayAdapter<String>(context!!, R.layout.spinner_itemage, arrayForadult)

            val childageapter: ArrayAdapter<String> =
                ArrayAdapter<String>(context!!, R.layout.spinner_itemage, arrayForchildage)


            sp_adult.setAdapter(adultadapter);
            sp_child.setAdapter(childadapter);
            sp_child1.setAdapter(childageapter);
            sp_child2.setAdapter(childageapter);
            sp_child3.setAdapter(childageapter);
            sp_child4.setAdapter(childageapter);

            sp_adult.setSelection(item.adult.toInt()-1)
            sp_child.setSelection(item.child.toInt())

            if (item.child.equals("0")) {
                lay_childage.visibility = View.GONE
            } else {
                lay_childage.visibility = View.VISIBLE

            }

            if (item.child1.isEmpty()) {
                lay_child1view.visibility = View.INVISIBLE
            } else {
                lay_child1view.visibility = View.VISIBLE
                sp_child1.setSelection(item.child1.toInt()-1)

            }
            if (item.child2.isEmpty()) {
                lay_child2view.visibility = View.INVISIBLE

            } else {
                lay_child2view.visibility = View.VISIBLE
                sp_child2.setSelection(item.child2.toInt()-1)

            }

            if (item.child3.isEmpty()) {
                lay_child3view.visibility = View.INVISIBLE
            } else {

                lay_child3view.visibility = View.VISIBLE
                sp_child3.setSelection(item.child3.toInt()-1)

            }

            if (item.child4.isEmpty()) {
                lay_child4view.visibility = View.INVISIBLE
            } else {
                lay_child4view.visibility = View.VISIBLE
                sp_child4.setSelection(item.child4.toInt()-1)
            }


            sp_adult.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {

                    val selectedItem = parent.getItemAtPosition(position).toString()
                    item.adult = selectedItem

                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }

            }



            sp_child1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    val selectedItem = parent.getItemAtPosition(position).toString()
                    item.child1 = selectedItem

                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }

            }



            sp_child2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {

                    val selectedItem = parent.getItemAtPosition(position).toString()
                    item.child2 = selectedItem
                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }

            }


            sp_child3.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {

                    val selectedItem = parent.getItemAtPosition(position).toString()
                    item.child3 = selectedItem
                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }

            }



            sp_child4.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {

                    val selectedItem = parent.getItemAtPosition(position).toString()
                    item.child4 = selectedItem

                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }

            }


            sp_child.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {



                    val selectedItem = parent.getItemAtPosition(position).toString()
                    item.child = selectedItem

                    if (selectedItem.equals("0")) {
                        lay_childage.visibility = View.GONE
                        lay_child1view.visibility = View.INVISIBLE
                        lay_child2view.visibility = View.INVISIBLE
                        lay_child3view.visibility = View.INVISIBLE
                        lay_child4view.visibility = View.INVISIBLE
                        item.child1 = ""
                        item.child2 = ""
                        item.child3 = ""
                        item.child4 = ""

                    } else if (selectedItem.equals("1")) {
                        lay_childage.visibility = View.VISIBLE
                        lay_child1view.visibility = View.VISIBLE
                        lay_child2view.visibility = View.INVISIBLE
                        lay_child3view.visibility = View.INVISIBLE
                        lay_child4view.visibility = View.INVISIBLE
                        item.child1 = "1"
                        item.child2 = ""
                        item.child3 = ""
                        item.child4 = ""
                    } else if (selectedItem.equals("2")) {
                        lay_childage.visibility = View.VISIBLE

                        lay_child1view.visibility = View.VISIBLE
                        lay_child2view.visibility = View.VISIBLE
                        lay_child3view.visibility = View.INVISIBLE
                        lay_child4view.visibility = View.INVISIBLE
                        item.child1 = "1"
                        item.child2 = "1"
                        item.child3 = ""
                        item.child4 = ""
                    } else if (selectedItem.equals("3")) {
                        lay_childage.visibility = View.VISIBLE
                        lay_child1view.visibility = View.VISIBLE
                        lay_child2view.visibility = View.VISIBLE
                        lay_child3view.visibility = View.VISIBLE
                        lay_child4view.visibility = View.INVISIBLE
                        item.child1 = "1"
                        item.child2 = "1"
                        item.child3 = "1"
                        item.child4 = ""
                    } else if (selectedItem.equals("4")) {
                        lay_childage.visibility = View.VISIBLE
                        lay_child1view.visibility = View.VISIBLE
                        lay_child2view.visibility = View.VISIBLE
                        lay_child3view.visibility = View.VISIBLE
                        lay_child4view.visibility = View.VISIBLE
                        item.child1 = "1"
                        item.child2 = "1"
                        item.child3 = "1"
                        item.child4 = "1"
                    }


                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }

            }

        }


    }

    private fun removeItem(position: Int) {
        this.data.removeAt(position)
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, this.data.size);
    }

    override fun recyclerViewListClicked(v: View, position: Int, flag: Int) {

        dialog.dismiss()


    }

    private fun openAgeDialog() {

        dialog = Dialog(mContext!!)
        dialog.setContentView(R.layout.dialog_contry)
        dialog.window?.setBackgroundDrawableResource(R.color.colorIdolDetailDialogBackground)
        dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )

        dialog.txt_title.setText(mContext.getString(R.string.str_selectcontry))
        val contryAdapter = AgeAdapter((mContext as FragmentActivity?)!!, this)

//        contryData.clear()
//        setContryData()
        val list = ArrayList<String>()
        for (i in 0 until values.size) {
            list.add(values[i])
        }

        Common.setVerticalRecyclerView(context!!, dialog.rv_bestseller)
        contryAdapter.swapData(list)

        dialog.rv_bestseller.adapter = contryAdapter

        dialog.lay_dialog.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }


}



