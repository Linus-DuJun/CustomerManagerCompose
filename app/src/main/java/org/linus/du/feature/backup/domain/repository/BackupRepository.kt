package org.linus.du.feature.backup.domain.repository

interface BackupRepository {
    suspend fun backup()
    suspend fun restore()
}