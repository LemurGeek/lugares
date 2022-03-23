package com.lugares.ui.lugar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.lugares.R
import com.lugares.adapter.LugarAdapter
import com.lugares.databinding.FragmentLugarBinding
import com.lugares.viewmodel.LugarViewModel

class LugarFragment : Fragment() {

    private lateinit var lugarViewModel: LugarViewModel
    private var _binding: FragmentLugarBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        lugarViewModel =ViewModelProvider(this).get(LugarViewModel::class.java)

        _binding = FragmentLugarBinding.inflate(inflater, container, false)

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_nav_lugar_to_addLugarFragment)
        }

        //Activar el Recycler View
        val lugarAdapter=LugarAdapter()  //Se crea un objeto adapter
        val reciclador = binding.reciclador   //Se recupera el recyclar view de la vista

        reciclador.adapter = lugarAdapter   //Se asigna lugarAdapter como el adapter de reciclador
        reciclador.layoutManager = LinearLayoutManager(requireContext())

        //Se crea un "observador" para mostrar la info de la tabla lugares...
        // se actualiza cuando cambio el set de datos
        lugarViewModel.getAllData.observe(viewLifecycleOwner) { lugares ->
            lugarAdapter.setData(lugares)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}