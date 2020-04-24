package com.myoutfit.models.network

import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.io.IOException

enum class ApiRequestStatus {
    RUNNING,
    SUCCESSFUL,
    FAILED,
    NO_INTERNET
}

data class NetworkState private constructor(
    val status: ApiRequestStatus,
    val commonError: CommonServerError? = null
) {

    companion object {
        const val NO_INTERNET_CODE = "no_internet_code"
        const val UNKNOWN_ERROR = "unknown_error_code"
        const val BAD_REQUEST = "bad request"
        const val NO_INTERNET_CONNECTION = "No Internet Connection!"

        private const val TITLE_ERROR = "error"
        private const val TITLE_UNKNOWN_ERROR = "unknown error"

        val LOADING = NetworkState(ApiRequestStatus.RUNNING)
        val SUCCESSFUL = NetworkState(ApiRequestStatus.SUCCESSFUL)
        val FAILED = NetworkState(ApiRequestStatus.FAILED)
        val NO_INTERNET = NetworkState(ApiRequestStatus.NO_INTERNET)

        fun error(t: Throwable?): NetworkState {
            return when (t) {
                is HttpException -> {
                    val errorBody = t.response().errorBody()
                    val serverError =
                        getCommonErrorFromResponse(errorBody)
                    NetworkState(
                        ApiRequestStatus.FAILED,
                        serverError
                    )
                }
                is IOException -> {
                    val serverError = CommonServerError(
                        NO_INTERNET_CODE,
                        "You do not have internet connection"
                    )
                    NetworkState(
                        ApiRequestStatus.FAILED,
                        serverError
                    )
                }
                else -> {
                    val serverError =
                        CommonServerError(UNKNOWN_ERROR, TITLE_UNKNOWN_ERROR)
                    NetworkState(
                        ApiRequestStatus.FAILED,
                        serverError
                    )
                }
            }
        }


        private fun getCommonErrorFromResponse(
            errorBody: ResponseBody?,
            alternateError: String? = null
        ): CommonServerError? {
            return errorBody?.let {
                try {
                    val errorResponse =
                        Gson().fromJson(errorBody.string(), ServerErrorResponse::class.java)
                    errorResponse?.data?.get(0)
                } catch (jse: JsonSyntaxException) {
                    jse.printStackTrace()
                    null
                } catch (jpe: JsonParseException) {
                    jpe.printStackTrace()
                    null
                }
            } ?: CommonServerError(
                UNKNOWN_ERROR,
                alternateError ?: TITLE_UNKNOWN_ERROR
            )
        }


    }
}