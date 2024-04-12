package com.example.namumovil.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.namumovil.R
import com.example.namumovil.adapter.ReservaAdapter
import com.example.namumovil.viewmodel.ReservaViewModel

class SeeReservationFragment : Fragment() {

    private lateinit var adapter: ReservaAdapter
    private lateinit var viewModel: ReservaViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla el layout para este fragmento
        return inflater.inflate(R.layout.fragment_see_reservation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializa el RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Inicializa el adaptador
        adapter = ReservaAdapter(ArrayList())
        recyclerView.adapter = adapter

        // Inicializa el ViewModel
        viewModel = ViewModelProvider(this).get(ReservaViewModel::class.java)

        // Observa los datos de las reservas
        viewModel.getReservaData().observe(viewLifecycleOwner, Observer { reservas ->
            // Actualiza los datos del adaptador
            adapter.setListData(ArrayList(reservas))
            adapter.notifyDataSetChanged()
        })
    }
}
