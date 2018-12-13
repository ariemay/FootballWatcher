package ariemaywibowo.footballwatcher.utility

interface OutputServerStats {
    fun onSuccess(response: String)
    fun onFailed(response: String)
    fun onFailure(throwable: Throwable?)
}