package org.wit.placemark.activities

import android.R.id.edit
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import org.wit.placemark.R
import org.wit.placemark.databinding.ActivityPlacemarkBinding
import org.wit.placemark.main.MainApp
import org.wit.placemark.models.PlacemarkModel
import timber.log.Timber
import timber.log.Timber.i

class PlacemarkActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityPlacemarkBinding
    var placemark = PlacemarkModel()

    var edit = false
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlacemarkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp

        i("Placemark Activity started...")

        if (intent.hasExtra("placemark_edit"))
        {
            edit = true
            placemark = intent.extras?.getParcelable("placemark_edit")!!
            binding.placemarkTitle.setText(placemark.title)
            binding.description.setText(placemark.description)
            binding.btnAdd.setText(R.string.menu_savePlacemark)
        }
        else {
            edit = false
            binding.btnAdd.setText(R.string.menu_addPlacemark)
        }

        binding.btnAdd.setOnClickListener {
            placemark.title = binding.placemarkTitle.text.toString()
            placemark.description = binding.description.text.toString()
            if (placemark.title.isNotEmpty()) {

                if (edit) {
                    app.placemarks.update(placemark.copy())
                } else {
                    app.placemarks.create(placemark.copy())
                }
                setResult(RESULT_OK)
                finish()
            }
            else {
                Snackbar.make(it, getString(R.string.enter_title), Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val titleRes = if (edit)
            R.string.menu_savePlacemark
        else
            R.string.menu_addPlacemark

        menu.findItem(R.id.item_add).setTitle(titleRes)

        return super.onCreateOptionsMenu(menu)
    }
        override fun onOptionsItemSelected(item: MenuItem): Boolean {

            when (item.itemId) {

                R.id.item_add -> {
                    placemark.title = binding.placemarkTitle.text.toString()
                    placemark.description = binding.description.text.toString()

                    if (placemark.title.isNotEmpty()) {

                        if (edit) {
                            app.placemarks.update(placemark.copy())
                        } else {
                            app.placemarks.create(placemark.copy())
                        }
                        setResult(RESULT_OK)
                        finish()
                    } else {
                        Snackbar.make(binding.root,
                            getString(R.string.enter_title),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
                R.id.item_cancel -> finish()
            }
            return super.onOptionsItemSelected(item)
        }
}