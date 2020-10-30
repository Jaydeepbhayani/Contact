package com.anetos.contact.ui.common

import android.content.Context
import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anetos.contact.core.helper.viewModelFactoryWithSingleArg
import com.anetos.contact.data.api.RemoteDataNotFoundException
import com.anetos.contact.repository.DataRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * * [DataViewModel]
 *
 * Common [ViewModel] class.
 *  @author
 *  created by Jaydeep Bhayani on 30/10/2020
 */

class DataViewModel(private val repository: DataRepository) : ViewModel() {

    companion object {
        val FACTORY =
            viewModelFactoryWithSingleArg(::DataViewModel)

        const val VISIBLE_THRESHOLD = 5
    }

    /**
     *  Contact Data mapping and refresh
     */
    val contactData = repository.contactData
    fun refreshContactData(context: Context, contactCursor: Cursor) {
        launchDataLoad { repository.getContactListData(context, contactCursor) }
    }

    //--------------------------------------------------------------------------------------------//
    /***
     * SnackBar and Spinner common for all datas
     */
    private var _snackBar: MutableLiveData<String> = MutableLiveData()
    val snackbar: LiveData<String> get() = _snackBar
    var spinner: MutableLiveData<Boolean> = MutableLiveData()
    val spinner1: LiveData<Boolean> get() = spinner

    private fun launchDataLoad(block: suspend () -> Unit): Job {
        return viewModelScope.launch {
            try {
                spinner.value = true
                block()
            } catch (error: RemoteDataNotFoundException) {
                _snackBar.value = error.message
            } finally {
                spinner.value = false
            }
        }
    }
    //--------------------------------------------------------------------------------------------//
}