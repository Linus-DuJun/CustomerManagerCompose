package org.linus.du.feature.customer.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CustomerDetailInfoScreen() {
    Scaffold {
        Box(modifier = Modifier.fillMaxSize()) {
            Text("customer detail")
        }
    }
}