package org.linus.du.feature.customer.ui.add_customer


import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize

import org.linus.core.ui.common.BaseTopAppBar
import org.linus.core.ui.theme.Gray200
import org.linus.core.ui.theme.Gray300
import org.linus.core.ui.theme.Red500
import org.linus.du.R

@Composable
fun AddCustomerScreen(
    viewModel: AddCustomerViewModel,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = { BaseTopAppBar(
            title = "添加客户",
            onBackClick = onBackClick
        ) }
    ) {
        Box(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                .fillMaxHeight()
        ) {
            ContentView(
                viewModel = viewModel,
                state = viewModel.screenState.collectAsState(),
                onBackClick = onBackClick,
                onSaveClick = { viewModel.obtainEvent(AddCustomerScreenEvent.SaveEvent) }
            )
        }
    }
}

@Composable
private fun ContentView(
    viewModel: AddCustomerViewModel,
    state: State<AddCustomerScreenStateHolder>,

    onSaveClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            NameView(
                name = state.value.name,
                isError = state.value.noNameError,
                onValueChanged = { viewModel.obtainEvent(AddCustomerScreenEvent.NameInputEvent(it)) }
            )
            Spacer(modifier = Modifier.height(16.dp))
            PhoneView(
                phone = state.value.phone,
                isError = state.value.noPhoneError,
                onValueChanged = { viewModel.obtainEvent(AddCustomerScreenEvent.PhoneInputEvent(it)) }
            )
            Spacer(modifier = Modifier.height(16.dp))
            VipLevelView(
                screenState = state,
                onVipLevelSelected = { viewModel.obtainEvent(AddCustomerScreenEvent.VipLevelSelectedEvent(it))}
            )

        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.height(68.dp)) {
            BottomButtonsView(
                onCancelClick = onBackClick,
                onSaveClick = onSaveClick
            )
        }
    }
}

@Composable
private fun VipLevelView(
    screenState: State<AddCustomerScreenStateHolder>,
    onVipLevelSelected: (String) -> Unit
) {
    val vipLevelState = rememberLevelStateHolder()
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box {
            VipLevelOutlinedTextField(state = vipLevelState, isError = screenState.value.noPhoneError)
            VipLevelDropdownMenuView(state = vipLevelState, onLevelSelected = onVipLevelSelected)
        }
    }
}

@Composable
private fun VipLevelOutlinedTextField(
    state: VipLevelStateHolder,
    isError: Boolean
) {
    OutlinedTextField(
        modifier = Modifier
            .focusable(false)
            .clickable { state.onOpened(true) }
            .fillMaxWidth()
            .onFocusChanged { if (it.isFocused) state.onOpened(true) }
            .onGloballyPositioned { state.onSize(it.size.toSize()) },
        value = state.selectedLevel,
        onValueChange = {},
        isError = isError,
        label = { Text(stringResource(id = R.string.vip_level)) },
        trailingIcon = {
            Icon(
                painter = painterResource(id = state.icon),
                contentDescription = null,
                modifier = Modifier.clickable { state.onOpened(!state.opened) }
            )
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colors.primary,
            focusedLabelColor = MaterialTheme.colors.primary,
            cursorColor = MaterialTheme.colors.primary,
            errorBorderColor = Red500,
            errorCursorColor = Red500,
            errorLabelColor = Red500
        )
    )
}

@Composable
private fun VipLevelDropdownMenuView(
    state: VipLevelStateHolder,
    onLevelSelected: (String) -> Unit
) {
    DropdownMenu(
        modifier = Modifier.width( with(LocalDensity.current) { state.size.width.toDp()} ),
        expanded = state.opened,
        onDismissRequest = { state.onOpened(false) }
    ) {
        state.levels.forEachIndexed { index, level ->
            DropdownMenuItem(onClick = {
                state.onSelectedIndex(index)
                state.onOpened(false)
                onLevelSelected(level)
            }) {
                Text(level)
            }
        }
    }
}


@Composable
private fun NameView(
    name: String,
    isError: Boolean,
    onValueChanged: (String) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = name,
        onValueChange = onValueChanged,
        label = { Text("姓名") },
        isError = isError,
        maxLines = 1,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colors.primary,
            focusedLabelColor = MaterialTheme.colors.primary,
            cursorColor = MaterialTheme.colors.primary,
            errorBorderColor = Red500,
            errorLabelColor = Red500,
            errorCursorColor = Red500
        )
    )
}

@Composable
private fun PhoneView(
    phone: String,
    isError: Boolean,
    onValueChanged: (String) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = phone,
        onValueChange = onValueChanged,
        label = { Text("电话") },
        isError = isError,
        maxLines = 1,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colors.primary,
            focusedLabelColor = MaterialTheme.colors.primary,
            cursorColor = MaterialTheme.colors.primary,
            errorBorderColor = Red500,
            errorLabelColor = Red500,
            errorCursorColor = Red500
        )
    )
}


@Composable
private fun BottomButtonsView(
    onCancelClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    Column {
        Divider(modifier = Modifier.height(1.dp),
            color = Gray200)
        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.Companion
            .align(Alignment.CenterHorizontally)
            .padding(12.dp)) {
            TextButton(modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 6.dp),
                shape = RoundedCornerShape(4.dp),
                elevation = ButtonDefaults.elevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp,
                    disabledElevation = 0.dp,
                    hoveredElevation = 0.dp,
                    focusedElevation = 0.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Transparent,
                    disabledBackgroundColor = Color.Transparent,
                    contentColor = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled)),
                onClick = onCancelClick) {
                Text(text = "取消")
            }
            Button(modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 6.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.primary,
                    disabledBackgroundColor = Gray300,
                    contentColor = Color.White),
                shape = RoundedCornerShape(4.dp),
                onClick = onSaveClick ) {
                Text(text = "添加")
            }
        }
    }
}