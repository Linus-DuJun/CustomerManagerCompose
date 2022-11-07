package org.linus.du.feature.backup.ui

sealed class BackupScreenEvent {
    object ExportDataEvent: BackupScreenEvent()
    object ImportDataEvent: BackupScreenEvent()
}