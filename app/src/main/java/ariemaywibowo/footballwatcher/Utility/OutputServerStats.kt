package ariemaywibowo.footballwatcher.Utility

interface OutputServerStats {
    fun onSuccess(response: String)
    fun onFailed(response: String)
    fun onFailure(throwable: Throwable?)
}