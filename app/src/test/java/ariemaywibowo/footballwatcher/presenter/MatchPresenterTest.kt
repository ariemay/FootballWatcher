package ariemaywibowo.footballwatcher.presenter

import ariemaywibowo.footballwatcher.server.RetroService
import ariemaywibowo.footballwatcher.utility.OutputServerStats
import ariemaywibowo.footballwatcher.view.MatchActivity
import com.nhaarman.mockito_kotlin.doAnswer
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import com.nhaarman.mockito_kotlin.mock
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.mockito.Mockito.*
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MatchPresenterTest {

    @Mock
    private
    lateinit var view: MatchActivity

    @Mock
    private
    lateinit var callback: OutputServerStats

    @Mock
    private
    lateinit var retroServ: RetroService

    @Mock
    lateinit var presenter: MatchPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = MatchPresenter(view)
    }

    @Test
    fun getLastMatches() {
        val leagueID = "4328"
        val mockCall: Call<ResponseBody> = mock()
        val bodyResponse: ResponseBody = mock()
        val responseTest = Response.success(bodyResponse)

        GlobalScope.launch {
            `when`(retroServ.getPastMatches(leagueID)).thenReturn(mockCall)
            doAnswer {
                val callBack: Callback<ResponseBody> = it.getArgument(0)
                callBack.onResponse(mockCall, responseTest)
            }.`when`(mockCall).enqueue(any())

            presenter.getLastMatches(leagueID, callback)

            verify(presenter).getLastMatches(leagueID, callback)
            verify(callback).onSuccess(responseTest.body().string())
        }
    }

    @Test
    fun getNextMatch() {
        val leagueID = "4328"
        val mockCall: Call<ResponseBody> = mock()
        val bodyResponse: ResponseBody = mock()
        val responseTest = Response.success(bodyResponse)

        GlobalScope.launch {
            `when`(retroServ.getNextMatches(leagueID)).thenReturn(mockCall)
            doAnswer {
                val callBack: Callback<ResponseBody> = it.getArgument(0)
                callBack.onResponse(mockCall, responseTest)
            }.`when`(mockCall).enqueue(any())

            presenter.getNextMatch(leagueID, callback)

            verify(presenter).getNextMatch(leagueID, callback)
            verify(callback).onSuccess(responseTest.body().string())
        }
    }

    @Test
    fun searchEvents() {
        val teamName = "Juventus"
        val mockCall: Call<ResponseBody> = mock()
        val bodyResponse: ResponseBody = mock()
        val responseTest = Response.success(bodyResponse)

        GlobalScope.launch {
            `when`(retroServ.searchEvents(teamName)).thenReturn(mockCall)
            doAnswer {
                val callBack: Callback<ResponseBody> = it.getArgument(0)
                callBack.onResponse(mockCall, responseTest)
            }.`when`(mockCall).enqueue(any())

            presenter.searchEvents(teamName, callback)

            verify(presenter).searchEvents(teamName, callback)
            verify(callback).onSuccess(responseTest.body().string())
        }
    }

}