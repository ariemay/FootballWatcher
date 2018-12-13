package ariemaywibowo.footballwatcher.view.players

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import ariemaywibowo.footballwatcher.models.DetailPlayerItem
import ariemaywibowo.footballwatcher.presenter.DetailPlayerPresenter
import ariemaywibowo.footballwatcher.R
import ariemaywibowo.footballwatcher.utility.OutputServerStats
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail_player.*

class DetailPlayerActivity : AppCompatActivity() {

    var presenter = DetailPlayerPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_player)

        val idPlayer = intent.getStringExtra("ID_PLAYER")
        showDetailPlayer(idPlayer)

    }

    private fun showDetailPlayer(idPlayer: String?) {
        var player: MutableList<DetailPlayerItem>
        presenter.getDetailPlayer(idPlayer, object : OutputServerStats {

            override fun onSuccess(response: String) {
                Log.i("detailteam", response)
                try {
                    player = presenter.parsingDetailPlayer(response)
                    if (player.size < 1) {
                        Toast.makeText(this@DetailPlayerActivity, "Maaf, coba lagi", Toast.LENGTH_SHORT).show()
                    } else {
                        val playerDetail = player
                        Picasso.get().load(playerDetail[0].strFanart1).fit().centerCrop().into(playerGambar)
                        playerNameDetail.text = playerDetail[0].strPlayer
                        height.text = playerDetail[0].strHeight
                        weight.text = playerDetail[0].strWeight
                        playerInfo.text = playerDetail[0].strDescriptionEN


                    }
                } catch (e: NullPointerException) {
                    Log.i("ERROR", "NullPointerException")
                }
            }

            override fun onFailed(response: String) {
                Log.i("ERROR", response)
            }

            override fun onFailure(throwable: Throwable?) {
                Toast.makeText(this@DetailPlayerActivity, "No connection?", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
