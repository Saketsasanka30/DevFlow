package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey val id: String,
    val key: String,
    val name: String,
    val description: String,
    val repoName: String,
    val stars: Int,
    val forks: Int,
    val status: String, // Active, Maintenance, Archived
    val techStack: String, // Kotlin, Compose, Ktor, Room, TypeScript
    val healthScore: Int, // 0 - 100
    val createdAt: String
)

@Entity(tableName = "repositories")
data class RepositoryEntity(
    @PrimaryKey val id: String,
    val projectId: String,
    val name: String,
    val owner: String,
    val defaultBranch: String,
    val isPrivate: Boolean,
    val description: String,
    val language: String,
    val stars: Int,
    val forks: Int,
    val openIssues: Int,
    val openPrs: Int,
    val updatedAt: String
)

@Entity(tableName = "commits")
data class CommitEntity(
    @PrimaryKey val id: String,
    val repoId: String,
    val hash: String,
    val shortHash: String,
    val message: String,
    val author: String,
    val authorAvatar: String,
    val branch: String,
    val timestamp: String,
    val additions: Int,
    val deletions: Int,
    val verified: Boolean
)

@Entity(tableName = "branches")
data class BranchEntity(
    @PrimaryKey val id: String,
    val repoId: String,
    val name: String,
    val isDefault: Boolean,
    val isProtected: Boolean,
    val lastCommitHash: String,
    val lastCommitDate: String
)

@Entity(tableName = "pull_requests")
data class PullRequestEntity(
    @PrimaryKey val id: String,
    val repoId: String,
    val number: Int,
    val title: String,
    val author: String,
    val authorAvatar: String,
    val sourceBranch: String,
    val targetBranch: String,
    val status: String, // OPEN, MERGED, CLOSED
    val additions: Int,
    val deletions: Int,
    val reviewStatus: String, // APPROVED, CHANGES_REQUESTED, PENDING
    val ciStatus: String, // SUCCESS, RUNNING, FAILED
    val createdAt: String,
    val summary: String,
    val changedFilesCount: Int
)

@Entity(tableName = "issues")
data class IssueEntity(
    @PrimaryKey val id: String,
    val repoId: String,
    val number: Int,
    val title: String,
    val description: String,
    val author: String,
    val status: String, // OPEN, IN_PROGRESS, CLOSED
    val priority: String, // CRITICAL, HIGH, MEDIUM, LOW
    val labels: String, // Comma separated: bug, ui, performance
    val assignee: String,
    val commentsCount: Int,
    val createdAt: String
)

@Entity(tableName = "releases")
data class ReleaseEntity(
    @PrimaryKey val id: String,
    val repoId: String,
    val tag: String,
    val name: String,
    val author: String,
    val publishedAt: String,
    val changelog: String,
    val isPrerelease: Boolean,
    val downloadCount: Int
)

@Entity(tableName = "deployments")
data class DeploymentEntity(
    @PrimaryKey val id: String,
    val projectId: String,
    val environment: String, // Production, Staging, Preview, Dev
    val status: String, // SUCCESS, BUILDING, FAILED, QUEUED
    val commitHash: String,
    val commitMessage: String,
    val buildNumber: Int,
    val durationSeconds: Int,
    val deployedAt: String,
    val deployedBy: String,
    val url: String,
    val logs: String
)

@Entity(tableName = "environments")
data class EnvironmentEntity(
    @PrimaryKey val id: String,
    val projectId: String,
    val name: String, // Production, Staging, Development
    val branch: String,
    val status: String, // Healthy, Degrading, Offline
    val url: String,
    val cpuUsagePct: Int,
    val memoryUsagePct: Int,
    val replicaCount: Int,
    val autoDeploy: Boolean,
    val lastDeployTime: String
)

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey val id: String,
    val projectId: String,
    val title: String,
    val description: String,
    val status: String, // BACKLOG, TODO, IN_PROGRESS, REVIEW, DONE
    val priority: String, // HIGH, MEDIUM, LOW
    val assignee: String,
    val dueDate: String,
    val storyPoints: Int
)

@Entity(tableName = "documents")
data class DocEntity(
    @PrimaryKey val id: String,
    val projectId: String,
    val title: String,
    val category: String, // Architecture, Guide, Runbook, API
    val content: String,
    val author: String,
    val updatedAt: String
)

@Entity(tableName = "api_docs")
data class ApiDocEntity(
    @PrimaryKey val id: String,
    val projectId: String,
    val method: String, // GET, POST, PUT, DELETE, PATCH
    val path: String,
    val summary: String,
    val description: String,
    val headersJson: String,
    val requestBodySample: String,
    val responseBodySample: String,
    val statusCodes: String
)

@Entity(tableName = "team_members")
data class TeamMemberEntity(
    @PrimaryKey val id: String,
    val name: String,
    val role: String, // Lead Architect, Senior Frontend, SRE, Core Dev
    val email: String,
    val githubHandle: String,
    val commitsCount: Int,
    val openPrsCount: Int,
    val isOnline: Boolean,
    val avatarInitials: String
)

@Entity(tableName = "activity_logs")
data class ActivityLogEntity(
    @PrimaryKey val id: String,
    val projectId: String,
    val type: String, // COMMIT, PR, DEPLOY, ISSUE, TASK
    val user: String,
    val action: String,
    val target: String,
    val timestamp: String
)
