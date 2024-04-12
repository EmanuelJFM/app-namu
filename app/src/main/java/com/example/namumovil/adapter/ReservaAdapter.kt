package com.example.namumovil.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.namumovil.R
import com.example.namumovil.model.Reserva

class ReservaAdapter(private var reservas: MutableList<Reserva>) : RecyclerView.Adapter<ReservaAdapter.ReservaViewHolder>() {

    class ReservaViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val tvTicket: TextView = view.findViewById(R.id.tv_Ticket)
        val tvCliente: TextView = view.findViewById(R.id.tv_Cliente)
        val tvFecha: TextView = view.findViewById(R.id.tv_Fecha)
        val tvEstado: TextView = view.findViewById(R.id.tv_Estado)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_reserva, parent, false)
        return ReservaViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReservaViewHolder, position: Int) {
        val reserva = reservas[position]
        holder.tvTicket.text = reserva.ticket.toString()
        holder.tvCliente.text = reserva.nombres
        holder.tvFecha.text = reserva.fechaReserva
        holder.tvEstado.text = reserva.estado
    }

    override fun getItemCount() = reservas.size

    fun setListData(data: ArrayList<Reserva>) {
        reservas = data
        notifyDataSetChanged() // Notifica al RecyclerView que los datos han cambiado
    }
}
