package ariemaywibowo.footballwatcher.Models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class DetailPlayerItem(
		val id: Long? = null,
		val strFanart1: String? = null,
		val strPlayer: String? = null,
		val strWeight: String? = null,
		val strHeight: String? = null,
		val strDescriptionEN: String? = null
) : Parcelable
