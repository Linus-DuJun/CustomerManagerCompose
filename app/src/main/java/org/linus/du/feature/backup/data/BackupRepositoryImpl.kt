package org.linus.du.feature.backup.data

import android.util.Log
import org.linus.core.data.db.dao.CustomerDao
import org.linus.core.data.db.dao.ReturnVisitDao
import org.linus.core.data.db.dao.SubjectDao
import org.linus.du.feature.backup.domain.repository.BackupRepository
import javax.inject.Inject


class BackupRepositoryImpl @Inject constructor(
    private val customerDao: CustomerDao,
    private val subjectDao: SubjectDao,
    private val returnVisitDao: ReturnVisitDao
) : BackupRepository {
    override suspend fun backup() {
        Log.i("dujun", "backup")
    }

    override suspend fun restore() {
        Log.i("dujun", "restore")
    }
}