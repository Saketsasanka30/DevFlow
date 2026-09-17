package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ai.AiMessage
import com.example.data.ai.AiPromptType
import com.example.data.ai.GeminiDevAssistant
import com.example.data.github.GitHubIntegration
import com.example.data.local.ActivityLogEntity
import com.example.data.local.ApiDocEntity
import com.example.data.local.DeploymentEntity
import com.example.data.local.DevFlowDatabase
import com.example.data.local.DocEntity
import com.example.data.local.EnvironmentEntity
import com.example.data.local.IssueEntity
import com.example.data.local.ProjectEntity
import com.example.data.local.PullRequestEntity
import com.example.data.local.RepositoryEntity
import com.example.data.local.TaskEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class DevFlowModule(val displayName: String, val category: String) {
    DASHBOARD("Dashboard", "Workspace"),
    PROJECTS("Projects", "Workspace"),
    REPOSITORIES("Repositories", "Code"),
    ISSUES("Issues", "Code"),
    PULL_REQUESTS("Pull Requests", "Code"),
    CODE_ACTIVITY("Code Activity", "Code"),
    CI_CD("CI/CD", "Deploy"),
    DEPLOYMENTS("Deployments", "Deploy"),
    ENVIRONMENTS("Environments", "Deploy"),
    API_DOCS("API Documentation", "Documentation"),
    TASKS("Tasks", "Planning"),
    DOCUMENTATION("Documentation", "Documentation"),
    TEAM("Team", "Organization"),
    ANALYTICS("Analytics & Health", "Analytics"),
    SETTINGS("Settings", "System")
}

enum class ProjectWorkspaceTab(val label: String) {
    OVERVIEW("Overview"),
    TASKS("Tasks"),
    REPOSITORY("Repository"),
    DEPLOYMENTS("Deployments"),
    DOCUMENTATION("Documentation"),
    ACTIVITY("Activity"),
    ANALYTICS("Analytics")
}

data class CodeHealthMetrics(
    val commitActivityCount: Int = 142,
    val prCycleTimeHours: Double = 3.2,
    val issueResolutionDays: Double = 1.4,
    val deploymentFrequencyPerDay: Double = 4.8,
    val buildSuccessRatePct: Double = 96.4
)

class DevFlowViewModel(application: Application) : AndroidViewModel(application) {
    private val database = DevFlowDatabase.getDatabase(application, viewModelScope)
    private val dao = database.devFlowDao()

    val gitHubIntegration = GitHubIntegration(dao)
    val aiAssistant = GeminiDevAssistant()

    // Navigation state
    private val _currentModule = MutableStateFlow(DevFlowModule.DASHBOARD)
    val currentModule: StateFlow<DevFlowModule> = _currentModule.asStateFlow()

    private val _currentProjectTab = MutableStateFlow(ProjectWorkspaceTab.OVERVIEW)
    val currentProjectTab: StateFlow<ProjectWorkspaceTab> = _currentProjectTab.asStateFlow()

    // Active project selection
    private val _selectedProjectId = MutableStateFlow("proj-1")
    val selectedProjectId: StateFlow<String> = _selectedProjectId.asStateFlow()

    // Active repository selection
    private val _selectedRepoId = MutableStateFlow("repo-1")
    val selectedRepoId: StateFlow<String> = _selectedRepoId.asStateFlow()

    // UI overlays
    private val _isCommandPaletteOpen = MutableStateFlow(false)
    val isCommandPaletteOpen: StateFlow<Boolean> = _isCommandPaletteOpen.asStateFlow()

    private val _isShortcutsDialogOpen = MutableStateFlow(false)
    val isShortcutsDialogOpen: StateFlow<Boolean> = _isShortcutsDialogOpen.asStateFlow()

    private val _isTerminalOpen = MutableStateFlow(false)
    val isTerminalOpen: StateFlow<Boolean> = _isTerminalOpen.asStateFlow()

    private val _isAiAssistantOpen = MutableStateFlow(false)
    val isAiAssistantOpen: StateFlow<Boolean> = _isAiAssistantOpen.asStateFlow()

    // Dark Mode Theme
    private val _isDarkTheme = MutableStateFlow(true)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    // Code Health Metrics
    private val _codeHealth = MutableStateFlow(CodeHealthMetrics())
    val codeHealth: StateFlow<CodeHealthMetrics> = _codeHealth.asStateFlow()

    // Data Flows from Room
    val projects = dao.getAllProjects().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val repositories = dao.getAllRepositories().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val commits = dao.getRecentCommits(30).stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val branches = dao.getBranchesForRepo("repo-1").stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val pullRequests = dao.getAllPullRequests().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val issues = dao.getAllIssues().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val releases = dao.getReleasesForRepo("repo-1").stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val deployments = dao.getAllDeployments().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val environments = dao.getAllEnvironments().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val tasks = dao.getAllTasks().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val documents = dao.getAllDocs().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val apiDocs = dao.getAllApiDocs().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val teamMembers = dao.getAllTeamMembers().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val activityLogs = dao.getRecentActivityLogs(25).stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Terminal interactive state
    private val _terminalLogs = MutableStateFlow(
        listOf(
            "DevFlow Developer OS v2.4.0 (x86_64-android-linux)",
            "Type 'help' for available commands or 'ai <prompt>' to query DevFlow AI.",
            "Connected to cluster: prod-us-east-1 [k8s v1.30.2] | Namespace: devflow-prod",
            "git: (main) f8a92bc - feat(palette): enhance fuzzy search and quick actions"
        )
    )
    val terminalLogs: StateFlow<List<String>> = _terminalLogs.asStateFlow()

    // AI Assistant Messages
    private val _aiMessages = MutableStateFlow(
        listOf(
            AiMessage(
                sender = "AI",
                content = "👋 Hello! I am **DevFlow AI**, your developer assistant.\n\nI can analyze repositories, summarize PRs, identify potential bugs, explain compiler or CI/CD errors, generate documentation, or suggest unit tests. What are we building today?",
                timestamp = "Online",
                promptType = null
            )
        )
    )
    val aiMessages: StateFlow<List<AiMessage>> = _aiMessages.asStateFlow()

    private val _isAiGenerating = MutableStateFlow(false)
    val isAiGenerating: StateFlow<Boolean> = _isAiGenerating.asStateFlow()

    // API Tester interactive state
    private val _apiTestResponse = MutableStateFlow<String?>(null)
    val apiTestResponse: StateFlow<String?> = _apiTestResponse.asStateFlow()

    private val _isApiTestLoading = MutableStateFlow(false)
    val isApiTestLoading: StateFlow<Boolean> = _isApiTestLoading.asStateFlow()

    // Selected items for detail drawers
    private val _selectedDeployment = MutableStateFlow<DeploymentEntity?>(null)
    val selectedDeployment: StateFlow<DeploymentEntity?> = _selectedDeployment.asStateFlow()

    private val _selectedPullRequest = MutableStateFlow<PullRequestEntity?>(null)
    val selectedPullRequest: StateFlow<PullRequestEntity?> = _selectedPullRequest.asStateFlow()

    private val _selectedIssue = MutableStateFlow<IssueEntity?>(null)
    val selectedIssue: StateFlow<IssueEntity?> = _selectedIssue.asStateFlow()

    fun navigateTo(module: DevFlowModule) {
        _currentModule.value = module
    }

    fun setProjectWorkspaceTab(tab: ProjectWorkspaceTab) {
        _currentProjectTab.value = tab
    }

    fun selectProject(projectId: String) {
        _selectedProjectId.value = projectId
    }

    fun selectRepo(repoId: String) {
        _selectedRepoId.value = repoId
    }

    fun toggleCommandPalette(open: Boolean? = null) {
        _isCommandPaletteOpen.value = open ?: !_isCommandPaletteOpen.value
    }

    fun toggleShortcutsDialog(open: Boolean? = null) {
        _isShortcutsDialogOpen.value = open ?: !_isShortcutsDialogOpen.value
    }

    fun toggleTerminal(open: Boolean? = null) {
        _isTerminalOpen.value = open ?: !_isTerminalOpen.value
    }

    fun toggleAiAssistant(open: Boolean? = null) {
        _isAiAssistantOpen.value = open ?: !_isAiAssistantOpen.value
    }

    fun toggleTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
    }

    fun selectDeployment(deployment: DeploymentEntity?) {
        _selectedDeployment.value = deployment
    }

    fun selectPullRequest(pr: PullRequestEntity?) {
        _selectedPullRequest.value = pr
    }

    fun selectIssue(issue: IssueEntity?) {
        _selectedIssue.value = issue
    }

    // Terminal Commands
    fun executeTerminalCommand(cmd: String) {
        val trimmed = cmd.trim()
        if (trimmed.isEmpty()) return

        val currentList = _terminalLogs.value.toMutableList()
        currentList.add("$ $trimmed")

        when {
            trimmed.equals("help", ignoreCase = true) -> {
                currentList.add("Available commands:")
                currentList.add("  git status       - Check working tree and active branch")
                currentList.add("  git log -n 3     - Show latest 3 commits")
                currentList.add("  deploy <env>     - Trigger deployment (e.g. 'deploy prod')")
                currentList.add("  docker ps        - List active container pods")
                currentList.add("  ai <query>       - Query DevFlow AI developer assistant")
                currentList.add("  clear            - Clear terminal buffer")
                currentList.add("  health           - Show code health & DORA metrics")
            }
            trimmed.equals("clear", ignoreCase = true) -> {
                currentList.clear()
                currentList.add("DevFlow Terminal cleared. Ready.")
            }
            trimmed.startsWith("git status", ignoreCase = true) -> {
                currentList.add("On branch main")
                currentList.add("Your branch is up to date with 'origin/main'.")
                currentList.add("nothing to commit, working tree clean")
            }
            trimmed.startsWith("git log", ignoreCase = true) -> {
                currentList.add("commit f8a92bc491 (HEAD -> main, origin/main)")
                currentList.add("Author: Elena Rostova <elena@devflow.io>")
                currentList.add("Date:   Wed Sep 16 23:40:12 2026 -0700")
                currentList.add("    feat(palette): enhance fuzzy search and quick action execution")
                currentList.add("")
                currentList.add("commit 4e1178a230")
                currentList.add("Author: Marcus Vance <marcus@devflow.io>")
                currentList.add("    perf(compose): optimize recomposition loops in heatmap")
            }
            trimmed.startsWith("docker ps", ignoreCase = true) -> {
                currentList.add("CONTAINER ID   IMAGE                          STATUS         PORTS      NAMES")
                currentList.add("a8f10b48c1e2   ghcr.io/devflow/core:v2.4.0    Up 18 hours    8080/tcp   devflow-core-1")
                currentList.add("92dc41ab78c1   ghcr.io/devflow/gateway:latest Up 2 days      443/tcp    gateway-router")
            }
            trimmed.startsWith("deploy", ignoreCase = true) -> {
                val targetEnv = if (trimmed.contains("prod")) "Production" else "Staging"
                currentList.add("🚀 Dispatching deployment for $targetEnv...")
                triggerNewDeployment(targetEnv)
                currentList.add("✅ Deployment queued. Build pipeline initiated.")
            }
            trimmed.startsWith("ai ", ignoreCase = true) -> {
                val aiQuery = trimmed.removePrefix("ai ").trim()
                currentList.add("🤖 Invoking DevFlow AI: \"$aiQuery\"...")
                askAiAssistant(AiPromptType.FREE_CHAT, aiQuery)
                currentList.add("Response sent to AI Assistant drawer.")
            }
            trimmed.equals("health", ignoreCase = true) -> {
                currentList.add("📊 DORA & Code Health:")
                currentList.add("  - Build Success Rate: ${_codeHealth.value.buildSuccessRatePct}%")
                currentList.add("  - PR Cycle Time: ${_codeHealth.value.prCycleTimeHours}h")
                currentList.add("  - Deployment Frequency: ${_codeHealth.value.deploymentFrequencyPerDay}/day")
                currentList.add("  - Issue Resolution: ${_codeHealth.value.issueResolutionDays} days")
            }
            else -> {
                currentList.add("zsh: command not found: $trimmed. Type 'help' for commands.")
            }
        }

        _terminalLogs.value = currentList
    }

    // AI Assistant Ask Action
    fun askAiAssistant(type: AiPromptType, query: String = "") {
        val userText = if (query.isNotBlank()) query else type.title
        val currentMsgList = _aiMessages.value.toMutableList()
        currentMsgList.add(
            AiMessage(
                sender = "USER",
                content = userText,
                timestamp = "Just now",
                promptType = type
            )
        )
        _aiMessages.value = currentMsgList
        _isAiGenerating.value = true

        val contextInfo = buildString {
            append("Selected Project: ${_selectedProjectId.value}\n")
            append("Selected Repo: ${_selectedRepoId.value}\n")
            _selectedPullRequest.value?.let { pr ->
                append("Active PR #${pr.number}: ${pr.title}\nStatus: ${pr.status}, CI: ${pr.ciStatus}\nSummary: ${pr.summary}\n")
            }
            _selectedDeployment.value?.let { dep ->
                append("Active Deployment: ${dep.environment} build #${dep.buildNumber} (${dep.status})\nLogs:\n${dep.logs.take(300)}\n")
            }
        }

        viewModelScope.launch {
            val responseText = aiAssistant.queryAssistant(type, userText, contextInfo)
            val updatedList = _aiMessages.value.toMutableList()
            updatedList.add(
                AiMessage(
                    sender = "AI",
                    content = responseText,
                    timestamp = "Just now",
                    promptType = type
                )
            )
            _aiMessages.value = updatedList
            _isAiGenerating.value = false
        }
    }

    // Trigger Deployment
    fun triggerNewDeployment(environment: String = "Staging") {
        viewModelScope.launch {
            val newDep = DeploymentEntity(
                id = "dep-${System.currentTimeMillis()}",
                projectId = _selectedProjectId.value,
                environment = environment,
                status = "BUILDING",
                commitHash = "f8a92bc",
                commitMessage = "Manual trigger from DevFlow Console",
                buildNumber = (845..999).random(),
                durationSeconds = 25,
                deployedAt = "Just now",
                deployedBy = "Active User",
                url = "https://$environment.devflow.io".lowercase(),
                logs = "[00:00.00] 🚀 Dispatched manual deployment build for $environment\n[00:02.10] 🔨 Compiling dependencies and running automated tests...\n[00:14.30] ✅ Build verified. Packaging artifacts."
            )
            dao.insertDeployment(newDep)
            dao.insertActivityLog(
                ActivityLogEntity(
                    id = "act-${System.currentTimeMillis()}",
                    projectId = _selectedProjectId.value,
                    type = "DEPLOY",
                    user = "You",
                    action = "triggered manual deployment to",
                    target = environment,
                    timestamp = "Just now"
                )
            )
        }
    }

    // Rollback Deployment
    fun rollbackDeployment(targetBuild: Int) {
        viewModelScope.launch {
            val rollbackDep = DeploymentEntity(
                id = "dep-${System.currentTimeMillis()}",
                projectId = _selectedProjectId.value,
                environment = "Production",
                status = "SUCCESS",
                commitHash = "f8a92bc",
                commitMessage = "Rollback to stable build #$targetBuild",
                buildNumber = (1000..1200).random(),
                durationSeconds = 18,
                deployedAt = "Just now",
                deployedBy = "SRE Admin (Rollback)",
                url = "https://app.devflow.io",
                logs = "[00:00.00] ⚠️ Emergency Rollback Executed\n[00:01.00] Reverted traffic to previous stable build #$targetBuild\n[00:05.20] Ingress health checks confirmed (100% healthy)."
            )
            dao.insertDeployment(rollbackDep)
        }
    }

    // Create New Issue
    fun createIssue(title: String, description: String, priority: String, labels: String) {
        viewModelScope.launch {
            val count = (issues.value.size + 1)
            val newIssue = IssueEntity(
                id = "iss-${System.currentTimeMillis()}",
                repoId = _selectedRepoId.value,
                number = 280 + count,
                title = title,
                description = description,
                author = "you",
                status = "OPEN",
                priority = priority,
                labels = labels,
                assignee = "Unassigned",
                commentsCount = 0,
                createdAt = "Just now"
            )
            dao.insertIssue(newIssue)
            dao.insertActivityLog(
                ActivityLogEntity(
                    id = "act-${System.currentTimeMillis()}",
                    projectId = _selectedProjectId.value,
                    type = "ISSUE",
                    user = "You",
                    action = "created new issue",
                    target = "#${newIssue.number}: $title",
                    timestamp = "Just now"
                )
            )
        }
    }

    // Create New Task
    fun createTask(title: String, description: String, priority: String, storyPoints: Int) {
        viewModelScope.launch {
            val newTask = TaskEntity(
                id = "tsk-${System.currentTimeMillis()}",
                projectId = _selectedProjectId.value,
                title = title,
                description = description,
                status = "TODO",
                priority = priority,
                assignee = "You",
                dueDate = "Sprint End",
                storyPoints = storyPoints
            )
            dao.insertTask(newTask)
        }
    }

    // Update Task Status
    fun updateTaskStatus(task: TaskEntity, newStatus: String) {
        viewModelScope.launch {
            dao.updateTask(task.copy(status = newStatus))
        }
    }

    // Test API Endpoint
    fun testApiEndpoint(apiDoc: ApiDocEntity) {
        _isApiTestLoading.value = true
        _apiTestResponse.value = null
        viewModelScope.launch {
            kotlinx.coroutines.delay(450) // Realistic network round-trip simulation
            _isApiTestLoading.value = false
            _apiTestResponse.value = """
HTTP/1.1 200 OK
Date: Wed, 16 Sep 2026 23:55:00 GMT
Content-Type: application/json; charset=utf-8
X-DevFlow-Trace-Id: tr-9921b-4f
X-RateLimit-Remaining: 4982

${apiDoc.responseBodySample}
            """.trimIndent()
        }
    }

    // GitHub Sync
    fun syncWithGitHub(owner: String = "devflow", repo: String = "devflow-android") {
        viewModelScope.launch {
            gitHubIntegration.syncRemoteRepository(owner, repo, _selectedProjectId.value)
        }
    }

    fun saveGitHubToken(token: String) {
        gitHubIntegration.configureToken(token)
    }

    fun saveGeminiApiKey(apiKey: String) {
        aiAssistant.userCustomApiKey = apiKey
    }
}
