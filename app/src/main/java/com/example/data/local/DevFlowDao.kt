package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DevFlowDao {
    // Projects
    @Query("SELECT * FROM projects ORDER BY name ASC")
    fun getAllProjects(): Flow<List<ProjectEntity>>

    @Query("SELECT * FROM projects WHERE id = :id LIMIT 1")
    suspend fun getProjectById(id: String): ProjectEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProjects(projects: List<ProjectEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: ProjectEntity)

    // Repositories
    @Query("SELECT * FROM repositories WHERE projectId = :projectId")
    fun getRepositoriesForProject(projectId: String): Flow<List<RepositoryEntity>>

    @Query("SELECT * FROM repositories ORDER BY stars DESC")
    fun getAllRepositories(): Flow<List<RepositoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepositories(repositories: List<RepositoryEntity>)

    // Commits
    @Query("SELECT * FROM commits WHERE repoId = :repoId ORDER BY timestamp DESC")
    fun getCommitsForRepo(repoId: String): Flow<List<CommitEntity>>

    @Query("SELECT * FROM commits ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentCommits(limit: Int = 20): Flow<List<CommitEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCommits(commits: List<CommitEntity>)

    // Branches
    @Query("SELECT * FROM branches WHERE repoId = :repoId")
    fun getBranchesForRepo(repoId: String): Flow<List<BranchEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBranches(branches: List<BranchEntity>)

    // Pull Requests
    @Query("SELECT * FROM pull_requests WHERE repoId = :repoId ORDER BY createdAt DESC")
    fun getPullRequestsForRepo(repoId: String): Flow<List<PullRequestEntity>>

    @Query("SELECT * FROM pull_requests ORDER BY createdAt DESC")
    fun getAllPullRequests(): Flow<List<PullRequestEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPullRequests(prs: List<PullRequestEntity>)

    @Update
    suspend fun updatePullRequest(pr: PullRequestEntity)

    // Issues
    @Query("SELECT * FROM issues WHERE repoId = :repoId ORDER BY number DESC")
    fun getIssuesForRepo(repoId: String): Flow<List<IssueEntity>>

    @Query("SELECT * FROM issues ORDER BY createdAt DESC")
    fun getAllIssues(): Flow<List<IssueEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIssues(issues: List<IssueEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIssue(issue: IssueEntity)

    @Update
    suspend fun updateIssue(issue: IssueEntity)

    // Releases
    @Query("SELECT * FROM releases WHERE repoId = :repoId ORDER BY publishedAt DESC")
    fun getReleasesForRepo(repoId: String): Flow<List<ReleaseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReleases(releases: List<ReleaseEntity>)

    // Deployments
    @Query("SELECT * FROM deployments WHERE projectId = :projectId ORDER BY deployedAt DESC")
    fun getDeploymentsForProject(projectId: String): Flow<List<DeploymentEntity>>

    @Query("SELECT * FROM deployments ORDER BY deployedAt DESC")
    fun getAllDeployments(): Flow<List<DeploymentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeployments(deployments: List<DeploymentEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeployment(deployment: DeploymentEntity)

    // Environments
    @Query("SELECT * FROM environments WHERE projectId = :projectId")
    fun getEnvironmentsForProject(projectId: String): Flow<List<EnvironmentEntity>>

    @Query("SELECT * FROM environments")
    fun getAllEnvironments(): Flow<List<EnvironmentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEnvironments(environments: List<EnvironmentEntity>)

    // Tasks
    @Query("SELECT * FROM tasks WHERE projectId = :projectId")
    fun getTasksForProject(projectId: String): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks")
    fun getAllTasks(): Flow<List<TaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<TaskEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)

    @Update
    suspend fun updateTask(task: TaskEntity)

    // Documents
    @Query("SELECT * FROM documents WHERE projectId = :projectId")
    fun getDocsForProject(projectId: String): Flow<List<DocEntity>>

    @Query("SELECT * FROM documents")
    fun getAllDocs(): Flow<List<DocEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDocs(docs: List<DocEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDoc(doc: DocEntity)

    // API Docs
    @Query("SELECT * FROM api_docs WHERE projectId = :projectId")
    fun getApiDocsForProject(projectId: String): Flow<List<ApiDocEntity>>

    @Query("SELECT * FROM api_docs")
    fun getAllApiDocs(): Flow<List<ApiDocEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApiDocs(apiDocs: List<ApiDocEntity>)

    // Team Members
    @Query("SELECT * FROM team_members")
    fun getAllTeamMembers(): Flow<List<TeamMemberEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeamMembers(members: List<TeamMemberEntity>)

    // Activity Logs
    @Query("SELECT * FROM activity_logs WHERE projectId = :projectId ORDER BY timestamp DESC")
    fun getActivityLogsForProject(projectId: String): Flow<List<ActivityLogEntity>>

    @Query("SELECT * FROM activity_logs ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentActivityLogs(limit: Int = 30): Flow<List<ActivityLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivityLogs(logs: List<ActivityLogEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivityLog(log: ActivityLogEntity)
}
