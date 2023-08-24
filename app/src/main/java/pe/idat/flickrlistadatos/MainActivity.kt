package pe.idat.flickrlistadatos

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.GridLayout
import pe.idat.flickrlistadatos.viewmodel.PhotoViewModel
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.switchmaterial.SwitchMaterial
import pe.idat.flickrlistadatos.adapter.PhotosAdapter
import pe.idat.flickrlistadatos.databinding.ActivityMainBinding
import pe.idat.flickrlistadatos.utils.DatabaseUtils

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding

    private lateinit var nodeSwitch: MaterialSwitch
    var isFirstTime = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val navView = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_principal)as NavHostFragment
        val navController =navView.navController
        binding.navView.setupWithNavController(navController)


        nodeSwitch=binding.switchDarkLight
        nodeSwitch.setOnCheckedChangeListener {compoundButton ,state->

            if (binding.switchDarkLight.isChecked){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                nodeSwitch.thumbDrawable = resources.getDrawable(R.drawable.baseline_dark_mode, null)
                isFirstTime = false

            }
            else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                nodeSwitch.thumbDrawable = resources.getDrawable(R.drawable.baseline_light_mode, null)
                isFirstTime = false

            }


        }

        if (isFirstTime) {
            isFirstTime = false


            val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK


            when (currentNightMode) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    nodeSwitch.isChecked = true
        println("night")          }
                Configuration.UI_MODE_NIGHT_NO -> {
                    nodeSwitch.isChecked = false
                }
                Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                    // No se puede determinar el tema (puede ser debido a la versión de Android)
                }
            }


        }





    }
}