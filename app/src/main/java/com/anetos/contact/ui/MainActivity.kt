package com.anetos.contact.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.view.View.VISIBLE
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anetos.contact.R
import com.anetos.contact.core.BaseActivity
import com.anetos.contact.core.helper.dataViewModelProvider
import com.anetos.contact.data.model.ContactResponse
import com.anetos.contact.ui.common.DataViewModel
import com.anetos.contact.ui.listcontact.ContactListAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    val TAG = javaClass.simpleName

    private lateinit var viewModel: DataViewModel

    lateinit var contactListAdapter: ContactListAdapter
    lateinit var contactCursor: Cursor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = dataViewModelProvider()

        progress.visibility = VISIBLE
        viewModel.contactData.observe(this, Observer { values ->
            progress.visibility = View.GONE
            if (values.size > 0) {
                contactListAdapter.setData(this, values)
            } else {
                errorLayout.visibility = VISIBLE
                showSnackBar(container, "\uD83D\uDE28 Something went wrong.", "OK")
            }
        })

        addContact.setOnClickListener { view: View ->
            saveContact()
        }

        showContacts()
        setAdapter()
    }

    override fun onResume() {
        super.onResume()
        showContacts()
    }

    private fun setAdapter() {
        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        contactListAdapter = ContactListAdapter()
        rvContact.layoutManager = layoutManager
        rvContact.adapter = contactListAdapter
        rvContact.setHasFixedSize(true)

        //animation
        rvContact.adapter?.notifyDataSetChanged()
        rvContact.scheduleLayoutAnimation()

        rvContact.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = layoutManager.itemCount
                val visibleItemCount = layoutManager.childCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                //listScrolled(visibleItemCount, lastVisibleItem, totalItemCount)
            }
        })

        contactListAdapter.setonItemClickListener(object :
            ContactListAdapter.onItemclickListener {
            override fun onItemClick(position: Int, itemArrayList: List<ContactResponse>) {
                /*val intent = Intent(
                    this@MainActivity,
                    NewsDetailActivity::class.java
                )
                intent.putExtra("position", position)
                intent.putParcelableArrayListExtra("news_detail", itemArrayList as ArrayList)
                startActivity(intent)*/
            }
        })
    }


    override fun onStop() {
        contactCursor.close()
        super.onStop()
    }

    fun saveContact() {
        val intent = Intent(Intent.ACTION_INSERT, ContactsContract.Contacts.CONTENT_URI).apply {
            type = ContactsContract.RawContacts.CONTENT_TYPE
        }
        startActivity(intent)
    }

    private fun showContacts() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS),
                PERMISSIONS_REQUEST_READ_WRITE_CONTACTS
            )
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            contactCursor = applicationContext.contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME.toString() + " ASC"
            )!!

            viewModel.refreshContactData(this, contactCursor)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSIONS_REQUEST_READ_WRITE_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                showContacts()
            } else {
                showSnackBar(container, "\uD83D\uDE28 Give permission to show the contacts", "OK")
            }
        }
    }

    companion object {
        private const val PERMISSIONS_REQUEST_READ_WRITE_CONTACTS = 100
    }
}