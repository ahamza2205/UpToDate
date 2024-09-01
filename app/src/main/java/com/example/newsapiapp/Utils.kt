package com.example.newsapiapp

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.newsapiapp.wrapper.Resource
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Utils {

    // COMPANION OBJECT ALLOWS US TO use the class without having to create an instance of it
    companion object {
        const val API_KEY = "f0875a92c36e48788f69809f8a1bd5b6"
        const val BASE_URL = "https://newsapi.org"


        fun DateFormat(oldstringDate: String?): String? {
            val newDate: String?
            val dateFormat = SimpleDateFormat("E, d MMM yyyy", Locale(getCountry()))
            newDate = try {
                val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(oldstringDate)
                dateFormat.format(date)
            } catch (e: ParseException) {
                e.printStackTrace()
                oldstringDate
            }
            return newDate
        }

        fun getCountry(): String {
            val locale = Locale.getDefault()
            val country = java.lang.String.valueOf(locale.country)
            return country.lowercase(Locale.getDefault())
        }


        fun <T> updateLiveData(
            response: Response<T>,
            mutableLiveData: MutableLiveData<Resource<T>>
        ) {
            try {
                mutableLiveData.postValue(Resource.Loading())
                if (response.isSuccessful) {
                    @Suppress("UNCHECKED_CAST")
                    mutableLiveData.postValue(Resource.Success(response.body() as T))
                }
                if (response.errorBody() != null) {
                    mutableLiveData.postValue(Resource.Error(response.message()))
                }
            } catch (e: SocketTimeoutException) {
                mutableLiveData.postValue(Resource.Error(response.message()))
                Log.d("TAG", "updateLiveData: ")
            } catch (e: UnknownHostException) {
                mutableLiveData.postValue(Resource.Error(response.message()))
            } catch (e: Exception) {
                mutableLiveData.postValue(Resource.Error(response.message()))
            }
        }

    }
}