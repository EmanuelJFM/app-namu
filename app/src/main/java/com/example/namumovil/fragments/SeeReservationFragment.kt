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
import com.example.namumovil.interfaces.ItemClick
import com.example.namumovil.databinding.FragmentSeeReservationBinding
import com.example.namumovil.model.Reserva
import com.google.firebase.auth.FirebaseAuth

class SeeReservationFragment : Fragment(), ItemClick {
    private var _binding: FragmentSeeReservationBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ReservaAdapter
    private lateinit var viewModel: ReservaViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSeeReservationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializa el RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        viewModel = ViewModelProvider(this).get(ReservaViewModel::class.java)
        adapter = ReservaAdapter(ArrayList(), this, viewModel)
        recyclerView.adapter = adapter
        val loggedInUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        viewModel.getReservaData(loggedInUserId).observe(viewLifecycleOwner, Observer { reservas ->
            adapter.setListData(ArrayList(reservas))
            adapter.notifyDataSetChanged()
        })
    }

    override fun onItemClick(reserva: Reserva) {
        val detailReservationFragment = DetailReservationFragment.newInstance(reserva)
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.container, detailReservationFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
