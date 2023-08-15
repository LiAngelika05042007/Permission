package com.angelika.permissions.ui.activity

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.angelika.permissions.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListener()
    }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            binding.image.setImageURI(uri)
        }

    private fun setupListener() {
        var isPermission = false
        binding.btnOpenGallery.setOnClickListener {
            if (checkSelfPermission(READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                galleryLauncher.launch("image/*")

            } else {
                requestPermissions(arrayOf(READ_EXTERNAL_STORAGE), 100)
                if (shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "Доступ запрещен", Toast.LENGTH_SHORT).show()
                    isPermission = true
                } else {
                    if (!(checkSelfPermission(READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) && isPermission) {
                        dialogue()
                    }
                }
            }
        }
    }

    private fun dialogue() {
        AlertDialog.Builder(this).apply {
            setTitle("Настройки")
            setMessage("Перейдите к настройкам, чтобы получить доступ")
            setPositiveButton("Перейти") { _: DialogInterface, _: Int ->
                openSetting()
            }
            setNegativeButton("Отмена") { _: DialogInterface, _: Int ->
                Toast.makeText(this@MainActivity, "Доступ запрещен", Toast.LENGTH_SHORT).show()
            }
        }.show()
    }

    private fun openSetting() {
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.parse("package:$packageName")
            startActivity(this)
        }
    }
}