package com.anetos.contact.repository

import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.anetos.contact.data.model.ContactResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

open class DataMainRepository

/**
 * *[DataRepository]
 *
 * A data repo containing contact list, selected contact from list of contact details.
 * @author
 * created by Jaydeep Bhayani on 30/10/2020
 */

class DataRepository : DataMainRepository() {

    val contactData: MutableLiveData<List<ContactResponse>> = MutableLiveData()
    val saveContactData: MutableLiveData<Boolean> = MutableLiveData()

    // avoid triggering multiple requests in the same time
    private var isRequestInProgress = false

    /**
     * [Live Data] to load contactList from phone.  contactListData's will be loaded from the repository cache.
     * Observing this will not cause the repos to be refreshed, use [getContactListData].
     */
    suspend fun getContactListData(context: Context, contactCursor: Cursor) {

        withContext(Dispatchers.IO) {
            val itemArrayList: MutableList<ContactResponse> = mutableListOf()

            Log.e("count", "" + contactCursor.count)
            if (contactCursor.count > 0) {
                while (contactCursor.moveToNext()) {
                    val id: String =
                        contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID))
                    val photo: String? =
                        contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI))
                    val name: String = contactCursor.getString(
                        contactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                    )
                    val phoneNumber: String = contactCursor.getString(
                        contactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                    )

                    val contact = ContactResponse()
                    contact.name = name
                    contact.phone = phoneNumber
                    contact.photo = photo

                    itemArrayList.add(contact)
                    contactData.postValue(itemArrayList)
                }
                Log.d("ContactFromPhone:", "$itemArrayList")
            } else contactData.postValue(emptyList())
        }
    }

    class DataRefreshError(cause: Throwable) : Throwable(cause.message, cause)
}