package com.anetos.contact.data.repository

import android.content.Context
import com.anetos.contact.repository.DataRepository

/**
 * *[Injection]
 *
 * All the viewModel Injections will go here.
 * @author
 * created by Jaydeep Bhayani on 30/10/2020
 */
object Injection {
    fun provideDataRepository(context: Context) =
        DataRepository()
}