package com.anetos.contact.data.api


/**
 * * [Exception]
 * RemoteDataNotFoundException to handle exception from response
 * @author
 * created by Jaydeep Bhayani on 30/10/2020
 */

open class DataSourceException(message: String? = null) : Exception(message)

class RemoteDataNotFoundException : DataSourceException("Data not Found")