package org.linus.du.feature.customer.ui.customer_edit

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import org.linus.core.ui.common.AnimatedLoadingView
import org.linus.core.ui.common.BaseTopAppBar
import org.linus.core.ui.theme.*
import org.linus.core.utils.extension.dateFor
import org.linus.du.R
import org.linus.du.feature.customer.ui.add_customer.*
import java.time.LocalDate
import java.util.*


@Composable
fun CustomerEditScreen(
    viewModel: CustomerEditViewModel,
    onBackClick: () -> Unit
) {
    val screenState = viewModel.screenState.collectAsState()
    Scaffold(
        topBar = { BaseTopAppBar(
            title = getAppBarTitle(state = screenState.value),
            onBackClick = onBackClick
        ) }
    ) {
        if (screenState.value.isLoading) {
            AnimatedLoadingView(visible = true)
        } else {
            CustomerEditContentView(viewModel = viewModel, onBackClick = onBackClick)
        }
    }
}

@Composable
private fun getAppBarTitle(state: EditScreenStateHolder): String {
    return if (state.customer == null) {
        " "
    } else {
        "${state.customer.name} ${state.customer.id}"
    }
}

private fun getLevelString(type: Int?) = when (type) {
    1 -> "客诉"
    2 -> "普通"
    3-> "超V"
    else -> ""
}

@Composable
private fun CustomerEditContentView(
    viewModel: CustomerEditViewModel,
    onBackClick: () -> Unit
) {
    val state = viewModel.screenState.collectAsState()
    Box() {
        EditContentView(
            viewModel = viewModel,
            state = viewModel.screenState.collectAsState(),
            onBackClick = onBackClick,
            onSaveClick = { viewModel.obtainEvent(EditScreenEvent.SaveEvent) }
        )
        if (state.value.showAddReturnVisitDialog) {
            ReturnVisitAddingView(
                screenState = state,
                onCancel = { viewModel.obtainEvent(EditScreenEvent.OnAddReturnVisitCancelEvent) },
                onSelectDate = { viewModel.obtainEvent(EditScreenEvent.OnSelectDateEvent)},
                onReturnVisitTitleChanged = {
                    viewModel.obtainEvent(EditScreenEvent.OnReturnVisitTitleInputEvent(it))
                },
                onAddReturnVisit = {
                    viewModel.obtainEvent(EditScreenEvent.OnAddReturnVisitConfirmEvent(state.value.currentAddingReturnVisit))
                }
            )
        }
        if (state.value.showDatePickerDialog) {
            DatePickerView(
                onDateConfirmed = { time, humanReadableTime ->
                    EditScreenEvent.OnReturnVisitDateConfirmedEvent(time, humanReadableTime).let {
                        viewModel.obtainEvent(it)
                    }
                },
            )
        }
        if (state.value.isLoading) {
            AnimatedLoadingView(visible = true)
        }
        if (state.value.finishWithSuccess) {
            onBackClick()
        }
    }
}

@Composable
private fun DatePickerView(
    onDateConfirmed: (Long, String) -> Unit,
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    calendar.time = Date()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            val humanReadableDate = "$year 年 ${month + 1} 月 $dayOfMonth 日"
            val ld = LocalDate.of(year, month + 1, dayOfMonth)
            onDateConfirmed(dateFor(ld).time, humanReadableDate)
        },
        year,
        month,
        day
    ).show()
}


@Composable
private fun ReturnVisitAddingView(
    screenState: State<EditScreenStateHolder>,
    onCancel: () -> Unit,
    onReturnVisitTitleChanged: (String) -> Unit,
    onSelectDate: () -> Unit,
    onAddReturnVisit: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onCancel,
        title = { Spacer(modifier = Modifier.height(8.dp))},
        text = {
            Column {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = screenState.value.currentAddingReturnVisit.title,
                    onValueChange = onReturnVisitTitleChanged,
                    label = { Text("回访简述") },
                    maxLines = 1,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Ocean300,
                        focusedLabelColor = Ocean300,
                        cursorColor = Ocean300,
                        errorBorderColor = Red500,
                        errorLabelColor = Red500,
                        errorCursorColor = Red500
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (screenState.value.currentAddingReturnVisit.timeStamp == 0L) {
                        Text("点击右侧按纽选择日期")
                    } else {
                        Text("回访日期: ${screenState.value.currentAddingReturnVisit.humanReadableTime}")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    IconButton(onClick = onSelectDate) {
                        Icon(
                            painterResource(id = R.drawable.ic_date_picker),
                            tint = Ocean300,
                            contentDescription = "选择日期"
                        )
                    }
                }
            }
        },
        buttons = {
            Row(
                modifier = Modifier.padding(all = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                TextButton(modifier = Modifier
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
                    onClick = onCancel) {
                    Text(text = "算了")
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 6.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.primary,
                        disabledBackgroundColor = Gray300,
                        contentColor = Color.White),
                    shape = RoundedCornerShape(4.dp),
                    onClick = onAddReturnVisit ) {
                    Text(text = "添加")
                }
            }
        }
    )
}


@Composable
private fun EditContentView(
    viewModel: CustomerEditViewModel,
    state: State<EditScreenStateHolder>,
    onSaveClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(start = 16.dp, end = 16.dp, top = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {

            VipLevelView(
                screenState = state,
                onVipLevelSelected = { viewModel.obtainEvent(EditScreenEvent.VipLevelSelectedEvent(it))}
            )
            Spacer(modifier = Modifier.height(8.dp))
            RecordView(
                screenState = state,
                onRecordInput = { viewModel.obtainEvent(EditScreenEvent.RecordInputEvent(it)) }
            )
            Spacer(modifier = Modifier.height(8.dp))
            RecordDescriptionView(
                description = state.value.recordDesc,
                onValueChanged = {
                    viewModel.obtainEvent(EditScreenEvent.RecordDescInputEvent(it))
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            AddReturnVisitButtonView(
                onAddReturnVisit = {viewModel.obtainEvent(EditScreenEvent.OnAddReturnVisitButtonClickedEvent)}
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (state.value.returnVisitItems.isNotEmpty()) {
                ReturnVisitListView(
                    returnVisitItems = state.value.returnVisitItems,
                    onRemoveReturnVisit = {
                        viewModel.obtainEvent(EditScreenEvent.RemoveReturnVisitItemEvent(it))
                    }
                )
            }
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
private fun ReturnVisitListView(
    returnVisitItems: List<ReturnVisit>,
    onRemoveReturnVisit: (ReturnVisit) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(0.dp, 200.dp)
    ) {
        items(returnVisitItems) { item: ReturnVisit ->
            ReturnVisitItemView(item, onRemoveReturnVisit)
        }
    }
}

@Composable
private fun ReturnVisitItemView(
    item: ReturnVisit,
    onRemoveReturnVisit: (ReturnVisit) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_bookmark),
            contentDescription = null,
            tint = Green,
            modifier = Modifier.size(width = 24.dp, height = 24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(item.title, style = MaterialTheme.typography.body1)
            Spacer(modifier = Modifier.height(6.dp))
            Text(item.humanReadableTime, style = MaterialTheme.typography.body2)
        }
        Spacer(modifier = Modifier.width(16.dp))
        IconButton(
            onClick = { onRemoveReturnVisit.invoke(item) }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_remove),
                tint = Red500,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}


@Composable
private fun AddReturnVisitButtonView(
    onAddReturnVisit: () -> Unit
) {
    Button(
        onClick = onAddReturnVisit,
        modifier = Modifier.size(width = 180.dp, height = 40.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Ocean300,
            contentColor = Color.White),
        elevation = ButtonDefaults.elevation(
            defaultElevation = 4.dp,
            pressedElevation = 6.dp,
            disabledElevation = 4.dp,
            hoveredElevation = 6.dp,
            focusedElevation = 6.dp),
        shape = RoundedCornerShape(4.dp),
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_attachment),
            contentDescription = null,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(text = stringResource(id = R.string.add_return_visit))
    }
}

/**
 * 门诊记录UI
 */
@Composable
private fun RecordView(
    screenState: State<EditScreenStateHolder>,
    onRecordInput: (String) -> Unit
) {
    val vipRecordState = rememberRecordsState()
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box {
            RecordOutlinedTextField(
                record = screenState.value.record,
                state = vipRecordState,
                onRecordInput = onRecordInput
            )
            RecordDropdownMenuView(state = vipRecordState, onLevelSelected = onRecordInput)
        }
    }
}

@Composable
private fun RecordDropdownMenuView(
    state: VipRecordStateHolder,
    onLevelSelected: (String) -> Unit
) {
    DropdownMenu(
        modifier = Modifier.width( with(LocalDensity.current) { state.size.width.toDp()} ),
        expanded = state.opened,
        onDismissRequest = { state.onOpened(false) }
    ) {
        state.records.forEachIndexed { index, record ->
            DropdownMenuItem(onClick = {
                state.onSelectedIndex(index)
                state.onOpened(false)
                onLevelSelected(record)
            }) {
                Text(record)
            }
        }
    }
}



@Composable
private fun RecordOutlinedTextField(
    record: String,
    state: VipRecordStateHolder,
    onRecordInput: (String) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier
            .focusable(true)
            .clickable { state.onOpened(true) }
            .fillMaxWidth()
            .onFocusChanged { if (it.isFocused) state.onOpened(true) }
            .onGloballyPositioned { state.onSize(it.size.toSize()) },
        value = record,
        onValueChange = onRecordInput,
        label = { Text(stringResource(id = R.string.record_items)) },
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
private fun VipLevelView(
    screenState: State<EditScreenStateHolder>,
    onVipLevelSelected: (String) -> Unit
) {
    val vipLevelState = rememberLevelStateHolder()
    with(screenState.value.customer!!) {
        when (this.type) {
            3 -> {
                vipLevelState.onSelectedIndex(0)
            }
            2 -> {
                vipLevelState.onSelectedIndex(1)
            }
            else -> {
                vipLevelState.onSelectedIndex(2)
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box {
            VipLevelOutlinedTextField(state = vipLevelState)
            VipLevelDropdownMenuView(state = vipLevelState, onLevelSelected = onVipLevelSelected)
        }
    }
}

@Composable
private fun VipLevelOutlinedTextField(
    state: VipLevelStateHolder,
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
private fun BottomButtonsView(
    onCancelClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    Column {
        Divider(modifier = Modifier.height(1.dp),
            color = Gray200
        )
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
                Text(text = "保存")
            }
        }
    }
}
