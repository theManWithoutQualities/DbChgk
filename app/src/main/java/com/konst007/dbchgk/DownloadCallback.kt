package com.konst007.dbchgk

import android.net.NetworkInfo

const val ERROR = -1
const val CONNECT_SUCCESS = 0
const val GET_INPUT_STREAM_SUCCESS = 1
const val PROCESS_INPUT_STREAM_IN_PROGRESS = 2
const val PROCESS_INPUT_STREAM_SUCCESS = 3

interface DownloadCallback<T> {

    fun updateFromDownload(result: T?)

    fun getActiveNetworkInfo(): NetworkInfo

    fun onProgressUpdate(progressCode: Int, percentComplete: Int)

    fun finishDownloading()
}