package com.lugares.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lugares.databinding.LugarFilaBinding
import com.lugares.model.Lugar
import com.lugares.ui.lugar.LugarFragmentDirections

class LugarAdapter : RecyclerView.Adapter<LugarAdapter.LugarViewHolder>() {

    //una lista de los lugares a mostrar...
    private var listaLugares = emptyList<Lugar>()

    fun setData(lugares : List<Lugar>) {
        this.listaLugares = lugares
        notifyDataSetChanged()
    }

    inner class LugarViewHolder(private val itemBinding: LugarFilaBinding)
        : RecyclerView.ViewHolder(itemBinding.root) {
            fun bind(lugar: Lugar) {
                itemBinding.tvNombre.text = lugar.nombre
                itemBinding.tvCorreo.text = lugar.correo
                itemBinding.tvTelefono.text = lugar.telefono
                itemBinding.tvWeb.text = lugar.web

                Glide.with(itemBinding.root.context)
                    .load(lugar.rutaImagen)
                    .circleCrop()
                    .into(itemBinding.imagen)

                itemBinding.vistaFila.setOnClickListener {
                    val accion = LugarFragmentDirections
                        .actionNavLugarToNavUpdateLugar(lugar)
                    itemView.findNavController().navigate(accion)
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LugarViewHolder {
        val itemBinding = LugarFilaBinding.inflate(LayoutInflater.from(parent.context),
        parent,false)
        return LugarViewHolder(itemBinding)  //Devuelvo un card para "pintar" un lugar
    }

    override fun onBindViewHolder(holder: LugarViewHolder, position: Int) {
        val lugar = listaLugares[position]  //Recupero el lugar a "pintar"
        holder.bind(lugar)   //Se invoca la funcion definida en el inner class
    }

    override fun getItemCount(): Int {
        return listaLugares.size  //Para conocer la cantidad de "card" que debe hacer...
    }

}