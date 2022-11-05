package org.linus.du.feature.customer.ui.add_customer

import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Size
import org.linus.du.R

const val USUAL_RECORD_ONE = "常用诊疗项目1"
const val USUAL_RECORD_TWO = "常用诊疗项目2"
const val USUAL_RECORD_THREE = "常用诊疗项目3"
const val USUAL_RECORD_FOUR = "常用诊疗项目4"
const val USUAL_RECORD_FIVE = "常用诊疗项目5"
const val USUAL_RECORD_SIX = "常用诊疗项目6"
const val USUAL_RECORD_SEVEN = "常用诊疗项目7"
const val USUAL_RECORD_EIGHT = "常用诊疗项目8"
const val USUAL_RECORD_NINE = "常用诊疗项目9"

val items = listOf(
    USUAL_RECORD_ONE,
    USUAL_RECORD_TWO,
    USUAL_RECORD_THREE,
    USUAL_RECORD_FOUR,
    USUAL_RECORD_FIVE,
    USUAL_RECORD_SIX,
    USUAL_RECORD_SEVEN,
    USUAL_RECORD_EIGHT,
    USUAL_RECORD_NINE
)

class VipRecordStateHolder(
    val records: List<String> = items
) {
    var opened by mutableStateOf(false)
    var selectedRecord by mutableStateOf("")
    var selectedIndex by mutableStateOf(-1)
    var size by mutableStateOf(Size.Zero)
    val icon: Int
        @Composable get() = if (opened) {
            R.drawable.ic_arrow_up
        } else {
            R.drawable.ic_arrow_down
        }

    fun onOpened(newValue: Boolean) {
        opened = newValue
    }

    fun onSelectedIndex(index: Int) {
        selectedIndex = index
        selectedRecord = records[selectedIndex]
    }

    fun onSize(newSize: Size) {
        size = newSize
    }
}

@Composable
fun rememberRecordsState() = remember {
    VipRecordStateHolder()
}