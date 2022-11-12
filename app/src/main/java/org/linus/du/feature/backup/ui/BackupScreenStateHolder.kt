package org.linus.du.feature.backup.ui

data class BackupScreenStateHolder(
    val isExporting: Boolean = false,
    val isImporting: Boolean = false,
    val importSuccess: Boolean = false,
    val exportSuccess: Boolean = false
)