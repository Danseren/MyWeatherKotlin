package geekbrains.android.myweatherkotlin.view.view.contentprovider

import android.Manifest
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import geekbrains.android.myweatherkotlin.databinding.FragmantContentProviderBinding

class ContentProviderFragment : Fragment() {

    private var _binding: FragmantContentProviderBinding? = null
    private val binding: FragmantContentProviderBinding
        get() {
            return _binding!!
        }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmantContentProviderBinding.inflate(inflater)
        return binding.root
    }

    private val REQUEST_CODE_READ_CONTACTS = 1984

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
    }

    private fun checkPermission() {
        val permissionResult =
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS)
        if (permissionResult == PackageManager.PERMISSION_GRANTED) {
            getContacts()
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
            AlertDialog.Builder(requireContext())
                .setTitle("Доступ к контактам")
                .setMessage("Без Вашего разрешения, эта часть приложения работать НЕ БУДЕТ!")
                .setPositiveButton("Предоставить доступ") { _, _ ->
                    permissionRequest(Manifest.permission.READ_CONTACTS)
                }
                .setNegativeButton("Не предоставлять доступ") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        } else {
            getContacts()

        }
        Log.d("My_Log", "$permissionResult")
    }

    fun permissionRequest(permission: String) {
        requestPermissions(arrayOf(permission), REQUEST_CODE_READ_CONTACTS)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_READ_CONTACTS) {
            for (index in permissions.indices) {
                if (permissions[index] == Manifest.permission.READ_CONTACTS
                    && grantResults[index] == PackageManager.PERMISSION_GRANTED
                ) {
                    getContacts()
                    Log.d("My_Log", "It's alive!")
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun getContacts() {
        val contentResolver: ContentResolver = requireContext().contentResolver
        val cursorWithContacts: Cursor? = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null, null, null,
            ContactsContract.Contacts.DISPLAY_NAME + " ASC"
        )
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}