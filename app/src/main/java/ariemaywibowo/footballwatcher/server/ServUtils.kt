package ariemaywibowo.footballwatcher.server

object ServUtils {
    val apiService: RetroService?
        get() = RetroCli.getClient(FootballAPI.END_POINT)?.create(RetroService::class.java)
}