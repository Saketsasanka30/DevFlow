package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        ProjectEntity::class,
        RepositoryEntity::class,
        CommitEntity::class,
        BranchEntity::class,
        PullRequestEntity::class,
        IssueEntity::class,
        ReleaseEntity::class,
        DeploymentEntity::class,
        EnvironmentEntity::class,
        TaskEntity::class,
        DocEntity::class,
        ApiDocEntity::class,
        TeamMemberEntity::class,
        ActivityLogEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class DevFlowDatabase : RoomDatabase() {
    abstract fun devFlowDao(): DevFlowDao
    abstract fun projectTaskDao(): ProjectTaskDao

    companion object {
        @Volatile
        private var INSTANCE: DevFlowDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): DevFlowDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DevFlowDatabase::class.java,
                    "devflow_os_database"
                )
                    .addCallback(DevFlowDatabaseCallback(scope))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DevFlowDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialDatabase(database.devFlowDao())
                    }
                }
            }

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                // Ensure initial data exists if tables are empty
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialDatabase(database.devFlowDao())
                    }
                }
            }
        }

        suspend fun populateInitialDatabase(dao: DevFlowDao) {
            dao.insertProjects(SeedData.projects)
            dao.insertRepositories(SeedData.repositories)
            dao.insertBranches(SeedData.branches)
            dao.insertCommits(SeedData.commits)
            dao.insertPullRequests(SeedData.pullRequests)
            dao.insertIssues(SeedData.issues)
            dao.insertReleases(SeedData.releases)
            dao.insertDeployments(SeedData.deployments)
            dao.insertEnvironments(SeedData.environments)
            dao.insertTasks(SeedData.tasks)
            dao.insertDocs(SeedData.docs)
            dao.insertApiDocs(SeedData.apiDocs)
            dao.insertTeamMembers(SeedData.teamMembers)
            dao.insertActivityLogs(SeedData.activityLogs)
        }
    }
}
