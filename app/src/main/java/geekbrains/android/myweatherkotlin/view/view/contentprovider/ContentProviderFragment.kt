package geekbrains.android.myweatherkotlin.view.view.contentprovider

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import geekbrains.android.myweatherkotlin.R
import geekbrains.android.myweatherkotlin.databinding.FragmentContentProviderBinding

class ContentProviderFragment : Fragment() {

    private var _binding: FragmentContentProviderBinding? = null
    private val binding: FragmentContentProviderBinding
        get() {
            return _binding!!
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContentProviderBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val REQUEST_CODE_READ_CONTACTS = 1984
    private val REQUEST_CODE_CALL = 451
    private var numberCurrent: String = "none"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
    }

    private fun checkPermission() {
        context?.let {
            when {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.READ_CONTACTS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    getContacts()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                    showDialog()
                }
                else -> {
                    myPermissionRequest()
                }
            }
        }
    }

    private fun myPermissionRequest() {
        requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_CODE_READ_CONTACTS)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_READ_CONTACTS) {
            when {
                (grantResults[0] == PackageManager.PERMISSION_GRANTED) -> {
                    getContacts()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                    showDialog()
                }
                else -> {
                    Log.d("My_Log", "Закончили")
                }
            }
        } else if (requestCode == REQUEST_CODE_CALL) {
            when {
                (grantResults[0] == PackageManager.PERMISSION_GRANTED) -> {
                    makeCall()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                    showDialog()
                }
                else -> {
                    makeCall()
                }
            }
        }
    }

    private fun getContacts() {
        context?.let { it ->
            val contentResolver = it.contentResolver
            val cursor = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.Contacts.DISPLAY_NAME + " ASC"
            )
            cursor?.let { cursor->
                for (i in 0 until cursor.count) {
                    cursor.moveToPosition(i)
                    val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    val contactId =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                    val number = getNumberFromId(contentResolver,contactId)
                    addView(name, number)
                }
            }
            cursor?.close()
        }
    }

    private fun getNumberFromId(contentResolver: ContentResolver, contactId: String): String {
        val phones = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = $contactId", null, null
        )
        var number: String = "none"
        phones?.let { cursor ->
            while (cursor.moveToNext()) {
                number =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            }
        }
        return number
    }

    private fun addView(name: String, number: String) {
        binding.containerForContacts.addView(TextView(requireContext()).apply {
            text = "$name:$number"
            textSize = 25f
            setOnClickListener {
                numberCurrent = number
                makeCall()
            }
        })
    }

    private fun showDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.need_permission_header)
            .setMessage(R.string.need_permission_text)
            .setPositiveButton(R.string.need_permission_give) { _, _ ->
                myPermissionRequest()
            }
            .setNegativeButton(R.string.need_permission_dont_give) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun makeCall() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CALL_PHONE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$numberCurrent"))
            startActivity(intent)
        } else {
            requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), REQUEST_CODE_CALL)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}