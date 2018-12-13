package ariemaywibowo.footballwatcher.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class PlayerItem(
	val id: Long? = null,
	val idPlayer: String? = null,
	val strPlayer: String? = null,
	val strCutout: String? = null,
	val strPosition: String? = null
) : Parcelable
