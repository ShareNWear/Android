package com.myoutfit.models.network

import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.myoutfit.models.error.ErrorModel
import com.myoutfit.utils.extentions.logd
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.io.IOException

enum class ApiRequestStatus {
    RUNNING,
    SUCCESSFUL,
    FAILED,
    NO_INTERNET
}

data class NetworkState constructor(
    val status: ApiRequestStatus,
    val error: CommonServerError? = null
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
                    val errorBody = t.response()?.errorBody()
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

        fun error(errorBody: ResponseBody?): NetworkState {
            val errorBodyContent = errorBody?.string()
                ?: return NetworkState(ApiRequestStatus.FAILED, CommonServerError(UNKNOWN_ERROR, TITLE_UNKNOWN_ERROR))

            val errorModel = try {
                Gson().fromJson(errorBodyContent, ErrorModel::class.java)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }

            return when {
                errorModel?.error?.code != null -> {
                    NetworkState(
                        ApiRequestStatus.FAILED, CommonServerError(
                            errorModel.error.code,
                            errorModel.error.message ?: ""
                        )
                    )
                }
                errorBodyContent.contains("Unauthenticated") -> NetworkState(
                    ApiRequestStatus.FAILED, CommonServerError(
                        "Unauthenticated",
                        "Unauthenticated, please login again via Facebook"
                    )
                )
                else -> NetworkState(ApiRequestStatus.FAILED, CommonServerError(UNKNOWN_ERROR, TITLE_UNKNOWN_ERROR))
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