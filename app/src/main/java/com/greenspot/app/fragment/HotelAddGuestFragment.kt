package com.greenspot.app.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.greenspot.app.R


class HotelAddGuestFragment : Fragment() {
    // TODO: Rename and change types of parameters


    fun newInstance(): HotelAddGuestFragment {

        val args = Bundle()

        val fragment = HotelAddGuestFragment()
        fragment.setArguments(args)
        return fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hotel_add_guest, container, false)
    }

    // TODO: Rename method, update argument and hook method into UI event

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onDetach() {
        super.onDetach()

    }


    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }


}
