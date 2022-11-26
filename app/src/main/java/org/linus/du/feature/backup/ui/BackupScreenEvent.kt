package org.linus.du.feature.backup.ui

sealed class BackupScreenEvent {
    object ExportCustomerEvent: BackupScreenEvent()
    object ImportCustomerEvent: BackupScreenEvent()
    object ImportCustomerSuccess: BackupScreenEvent()
    object ExportCustomerSuccess: BackupScreenEvent()

    object ExportRecordEvent: BackupScreenEvent()
    object ImportRecordEvent: BackupScreenEvent()
    object ImportRecordSuccess: BackupScreenEvent()
    object ExportRecordSuccess: BackupScreenEvent()

    object ExportReturnVisitEvent: BackupScreenEvent()
    object ImportReturnVisitEvent: BackupScreenEvent()
    object ImportReturnVisitSuccess: BackupScreenEvent()
    object ExportReturnVisitSuccess: BackupScreenEvent()
}