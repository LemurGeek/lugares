package com.lugares.ui.lugar

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.lugares.R
import com.lugares.databinding.FragmentAddLugarBinding
import com.lugares.model.Lugar
import com.lugares.viewmodel.LugarViewModel

class AddLugarFragment : Fragment() {
    private lateinit var lugarViewModel: LugarViewModel
    private var _binding: FragmentAddLugarBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        lugarViewModel = ViewModelProvider(this).get(LugarViewModel::class.java)

        _binding = FragmentAddLugarBinding.inflate(inflater, container, false)

        binding.btAddLugar.setOnClickListener {
            agregarLugar();
        }

        ubicaGPS()

        return binding.root
    }

    private var conPermisos:Boolean=true

    private fun ubicaGPS() {
        //Ubicacion tendrá la info de las coordenadas y demás
        val ubicacion: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        //Se valida si se tienen los permisos
        if (ActivityCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_COARSE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {

            //Se solicitan los permisos al usuario
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION),105)
        }

        //Efectivamente se busca la info del gps
        if(conPermisos) {
            ubicacion.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    binding.tvLatitud.text = "${location.latitude}"
                    binding.tvLongitud.text = "${location.longitude}"
                    binding.tvAltura.text = "${location.altitude}"
                } else {
                    binding.tvLatitud.text = "9.9358"
                    binding.tvLongitud.text = "-84.0360"
                    binding.tvAltura.text = "0.0"
                }
            }
        }

    }

    private fun agregarLugar() {
        val nombre=binding.etNombre.text.toString()
        if (nombre.isNotEmpty()) {
            val correo=binding.etCorreo.text.toString()
            val telefono=binding.etTelefono.text.toString()
            val web=binding.etWeb.text.toString()
            val latitud = binding.tvLatitud.text.toString().toDouble()
            val longitud = binding.tvLongitud.text.toString().toDouble()
            val altura = binding.tvAltura.text.toString().toDouble()

            val lugar= Lugar("",nombre,correo,telefono,web,
                latitud,longitud,altura,
                "","")

            lugarViewModel.addLugar(lugar)
            Toast.makeText(requireContext(),
                getString(R.string.msg_lugar_add),
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigate(R.id.action_addLugarFragment_to_nav_lugar)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}