package org.linus.core.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable


@Composable
fun BaseTopAppBar(
    title: String,
    onBackClick: () -> Unit,
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        title = { Text(text = title) },
        backgroundColor = MaterialTheme.colors.background.copy(
            alpha = 0.97f
        ),
        contentColor = MaterialTheme.colors.onSurface,
        navigationIcon = {
            IconButton(onClick = { onBackClick() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    tint = MaterialTheme.colors.onSurface,
                    contentDescription = "返回"
                )
            }
        },
        actions = actions
    )
}