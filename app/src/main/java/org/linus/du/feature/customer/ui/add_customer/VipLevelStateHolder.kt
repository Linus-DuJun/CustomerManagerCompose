package org.linus.du.feature.customer.ui.add_customer

import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Size
import org.linus.du.R

const val SUPER_VIP = "超优客户"
const val NORMAL_VIP = "一般客户"
const val BAD_CUSTOMER = "客诉客户"

class VipLevelStateHolder(
    val levels: List<String> = listOf(SUPER_VIP, NORMAL_VIP, BAD_CUSTOMER)
) {
    var opened by mutableStateOf(false)
    var selectedLevel by mutableStateOf("")
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
        selectedLevel = levels[selectedIndex]
    }

    fun onSize(newSize: Size) {
        size = newSize
    }
}

@Composable
fun rememberLevelStateHolder() = remember {
    VipLevelStateHolder()
}