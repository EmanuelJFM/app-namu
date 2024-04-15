package com.example.namumovil.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.namumovil.R
import com.example.namumovil.model.Reserva
import com.example.namumovil.interfaces.ItemClick
import com.example.namumovil.viewmodel.ReservaViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ReservaAdapter(private var reservas: MutableList<Reserva>, private val itemClick: ItemClick, private val viewModel: ReservaViewModel) : RecyclerView.Adapter<ReservaAdapter.ReservaViewHolder>() {

    class ReservaViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val tvTicket: TextView = view.findViewById(R.id.tv_Ticket)
        val tvEstado: TextView = view.findViewById(R.id.tv_Estado)
        val cancelButton: ImageButton = itemView.findViewById(R.id.cancelButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_reserva, parent, false)
        return ReservaViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReservaViewHolder, position: Int) {
        val reserva = reservas[position]
        holder.tvTicket.text = reserva.ticket.toString()
        holder.tvEstado.text = reserva.estado
        holder.itemView.setOnClickListener {
            itemClick.onItemClick(reserva)
        }
        holder.cancelButton.setOnClickListener {
            MaterialAlertDialogBuilder(it.context)
                .setTitle("Cancelar Reserva")
                .setMessage("¿Estás seguro de que quieres cancelar esta reserva?")
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton("Sí") { dialog, _ ->
                    // Actualiza el estado de la reserva a "Cancelado"
                    viewModel.updateReservaEstado(reserva.reservaId, "Cancelado",
                        onSuccess = {
                            MaterialAlertDialogBuilder(it.context)
                                .setTitle("Reserva Cancelada")
                                .setMessage("Su reserva ha sido cancelada con exito")
                                .setPositiveButton("Aceptar") { dialog, _ ->
                                    dialog.dismiss()
                                }
                        },
                        onFailure = {

                        }
                    )
                    dialog.dismiss()
                }
                .show()
        }
    }
    override fun getItemCount() = reservas.size
    fun setListData(data: ArrayList<Reserva>) {
        reservas = data
        notifyDataSetChanged()
    }
}

