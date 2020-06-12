package com.lecezar.hotelupgrade.utils.base

import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {
//    @Suppress("PropertyName", "VariableNaming")
//    protected val _apiCallsCount = MutableLiveData<Int>()
//    val apiCallsCount: LiveData<Int>
//        get() = _apiCallsCount
//
//    protected fun performApiCall(block: suspend CoroutineScope.() -> Unit) {
//        _apiCallsCount.map {
//            it + 1
//        }
//        viewModelScope.launch {
//            block()
//            _apiCallsCount.map {
//                it - 1
//            }
//        }
//    }
//
//    @Suppress("MagicNumber")
//    protected fun checkInternetAvailable(callback: (isInternet: Boolean) -> Unit) {
//        performApiCall {
//            try {
//                val timeoutMs = 1500
//                val socket = Socket()
//                val socketAddress = InetSocketAddress("8.8.8.8", 53)
//
//                withContext(Dispatchers.IO) {
//                    socket.connect(socketAddress, timeoutMs)
//                    socket.close()
//                }
//
//                callback(true)
//            } catch (e: IOException) {
//                callback(false)
//            }
//        }
//    }
}