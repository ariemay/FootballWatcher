package ariemaywibowo.footballwatcher.Utility

import ariemaywibowo.footballwatcher.Utility.Date.changeDate
import org.junit.Test

import org.junit.Assert.*

class DateTest {

    @Test
    fun testchangeDate() {
        val date : String = "2018-11-19"
        assertEquals("Mon, 19 Nov 2018", changeDate(date))
    }
}