package com.anetos.contact.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * * [ContactResponse]
 *
 * is model class for Contact.
 * @author
 * created by Jaydeep Bhayani on 30/10/2020
 */

@Parcelize
data class ContactResponse(
    var id: String? = null,
    var name: String? = null,
    var phone: String? = null,
    var photo: String? = null,
) : Parcelable