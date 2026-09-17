package com.example.data.local

object SeedData {
    val projects = listOf(
        ProjectEntity(
            id = "proj-1",
            key = "DF-CORE",
            name = "DevFlow Core",
            description = "Unified developer operating system workspace, plugin architecture and reactive engine.",
            repoName = "devflow/devflow-android",
            stars = 3420,
            forks = 482,
            status = "Active",
            techStack = "Kotlin, Jetpack Compose, Room, Coroutines",
            healthScore = 98,
            createdAt = "2026-01-15"
        ),
        ProjectEntity(
            id = "proj-2",
            key = "GATEWAY",
            name = "Cloud API Gateway",
            description = "High-performance edge router, rate limiting and Zero-Trust service mesh interface.",
            repoName = "devflow/gateway-core",
            stars = 1840,
            forks = 215,
            status = "Active",
            techStack = "Rust, Tokio, gRPC, eBPF",
            healthScore = 94,
            createdAt = "2026-02-01"
        ),
        ProjectEntity(
            id = "proj-3",
            key = "TELEMETRY",
            name = "Data Mesh Telemetry",
            description = "Distributed event ingestion pipeline for DORA developer health metrics and trace analytics.",
            repoName = "devflow/telemetry-worker",
            stars = 920,
            forks = 114,
            status = "Active",
            techStack = "TypeScript, ClickHouse, Apache Kafka",
            healthScore = 91,
            createdAt = "2026-03-10"
        ),
        ProjectEntity(
            id = "proj-4",
            key = "SENTINEL",
            name = "Auth Sentinel",
            description = "Enterprise identity provider, passkeys, webhooks and RBAC permission evaluations.",
            repoName = "devflow/auth-service",
            stars = 1290,
            forks = 160,
            status = "Active",
            techStack = "Kotlin, Ktor, PostgreSQL, Redis",
            healthScore = 96,
            createdAt = "2026-02-20"
        )
    )

    val repositories = listOf(
        RepositoryEntity(
            id = "repo-1",
            projectId = "proj-1",
            name = "devflow-android",
            owner = "devflow",
            defaultBranch = "main",
            isPrivate = false,
            description = "The unified developer operating system client for mobile and workstation.",
            language = "Kotlin",
            stars = 3420,
            forks = 482,
            openIssues = 14,
            openPrs = 6,
            updatedAt = "10 minutes ago"
        ),
        RepositoryEntity(
            id = "repo-2",
            projectId = "proj-2",
            name = "gateway-core",
            owner = "devflow",
            defaultBranch = "main",
            isPrivate = true,
            description = "Ultra-low latency reverse proxy with TLS termination and traffic shadowing.",
            language = "Rust",
            stars = 1840,
            forks = 215,
            openIssues = 8,
            openPrs = 3,
            updatedAt = "45 minutes ago"
        ),
        RepositoryEntity(
            id = "repo-3",
            projectId = "proj-3",
            name = "telemetry-worker",
            owner = "devflow",
            defaultBranch = "main",
            isPrivate = false,
            description = "DORA metrics aggregator, cycle-time calculator and event webhook consumer.",
            language = "TypeScript",
            stars = 920,
            forks = 114,
            openIssues = 5,
            openPrs = 2,
            updatedAt = "2 hours ago"
        ),
        RepositoryEntity(
            id = "repo-4",
            projectId = "proj-4",
            name = "auth-service",
            owner = "devflow",
            defaultBranch = "main",
            isPrivate = true,
            description = "OIDC Provider with JWT signature verification and passkey registration.",
            language = "Kotlin",
            stars = 1290,
            forks = 160,
            openIssues = 4,
            openPrs = 1,
            updatedAt = "Yesterday"
        )
    )

    val branches = listOf(
        BranchEntity("b-1", "repo-1", "main", true, true, "f8a92bc", "10m ago"),
        BranchEntity("b-2", "repo-1", "feature/command-palette-v2", false, false, "4e1178a", "2h ago"),
        BranchEntity("b-3", "repo-1", "fix/ci-pipeline-cache", false, false, "92d05c1", "5h ago"),
        BranchEntity("b-4", "repo-1", "refactor/room-indexes", false, false, "10b89cf", "1d ago"),
        BranchEntity("b-5", "repo-2", "main", true, true, "83a710e", "45m ago"),
        BranchEntity("b-6", "repo-2", "feat/http3-quic", false, false, "34d92a7", "3h ago")
    )

    val commits = listOf(
        CommitEntity("c-1", "repo-1", "f8a92bc491", "f8a92bc", "feat(palette): enhance fuzzy search and quick action execution", "Elena Rostova", "ER", "main", "12m ago", 142, 28, true),
        CommitEntity("c-2", "repo-1", "4e1178a230", "4e1178a", "perf(compose): optimize recomposition loops in code activity heatmap", "Marcus Vance", "MV", "main", "1h ago", 65, 12, true),
        CommitEntity("c-3", "repo-1", "92d05c18bb", "92d05c1", "fix(deployments): handle streaming ANSI escape codes in console viewer", "Sara Chen", "SC", "main", "3h ago", 88, 34, true),
        CommitEntity("c-4", "repo-1", "10b89cf912", "10b89cf", "ci(github-actions): integrate automated Roborazzi visual regression suite", "David Kim", "DK", "main", "5h ago", 210, 45, true),
        CommitEntity("c-5", "repo-1", "76ea491df0", "76ea491", "docs(api): update OpenAPI 3.1 definitions for deployment rollout hooks", "Elena Rostova", "ER", "main", "Yesterday", 54, 8, false),
        CommitEntity("c-6", "repo-2", "83a710e412", "83a710e", "feat(proxy): add zero-copy memory buffers for high throughput sockets", "Alex Rivera", "AR", "main", "45m ago", 310, 89, true)
    )

    val pullRequests = listOf(
        PullRequestEntity(
            id = "pr-1",
            repoId = "repo-1",
            number = 142,
            title = "feat(ci): Add live streaming deployment logs with ANSI syntax highlight",
            author = "sarachen",
            authorAvatar = "SC",
            sourceBranch = "feat/ansi-stream-logs",
            targetBranch = "main",
            status = "OPEN",
            additions = 340,
            deletions = 45,
            reviewStatus = "APPROVED",
            ciStatus = "SUCCESS",
            createdAt = "2 hours ago",
            summary = "Implements WebSocket and SSE based live streaming console outputs with interactive autoscroll, terminal pause/resume, and export options.",
            changedFilesCount = 8
        ),
        PullRequestEntity(
            id = "pr-2",
            repoId = "repo-1",
            number = 143,
            title = "perf(database): Add Room composite indices for project activity queries",
            author = "marcusvance",
            authorAvatar = "MV",
            sourceBranch = "refactor/room-indices",
            targetBranch = "main",
            status = "OPEN",
            additions = 112,
            deletions = 24,
            reviewStatus = "PENDING",
            ciStatus = "RUNNING",
            createdAt = "4 hours ago",
            summary = "Reduces cold-start DB query times by 42% on developer dashboards with multi-entity joins and filtered indexes.",
            changedFilesCount = 5
        ),
        PullRequestEntity(
            id = "pr-3",
            repoId = "repo-1",
            number = 140,
            title = "fix(security): sanitize shell arguments in terminal execution runner",
            author = "davidkim",
            authorAvatar = "DK",
            sourceBranch = "fix/runner-sanitization",
            targetBranch = "main",
            status = "MERGED",
            additions = 85,
            deletions = 32,
            reviewStatus = "APPROVED",
            ciStatus = "SUCCESS",
            createdAt = "Yesterday",
            summary = "Enforces strict parameter tokenization and whitelist checks against injection vectors in internal terminal task runners.",
            changedFilesCount = 3
        ),
        PullRequestEntity(
            id = "pr-4",
            repoId = "repo-2",
            number = 89,
            title = "feat(http3): Implement QUIC protocol fallback handler",
            author = "alexrivera",
            authorAvatar = "AR",
            sourceBranch = "feat/quic-fallback",
            targetBranch = "main",
            status = "OPEN",
            additions = 540,
            deletions = 98,
            reviewStatus = "CHANGES_REQUESTED",
            ciStatus = "FAILED",
            createdAt = "3 hours ago",
            summary = "Enables QUIC 0-RTT handshakes with fallback to TLS 1.3 upon UDP degradation.",
            changedFilesCount = 12
        )
    )

    val issues = listOf(
        IssueEntity(
            id = "iss-1",
            repoId = "repo-1",
            number = 284,
            title = "Terminal console drops WebSocket packets under heavy burst logging",
            description = "When running high verbosity builds (e.g. Gradle debug with 10k lines/sec), message buffer fills up leading to dropped frames.",
            author = "elena",
            status = "IN_PROGRESS",
            priority = "CRITICAL",
            labels = "bug, terminal, streaming",
            assignee = "Sara Chen",
            commentsCount = 6,
            createdAt = "1 day ago"
        ),
        IssueEntity(
            id = "iss-2",
            repoId = "repo-1",
            number = 285,
            title = "Command palette fuzzy search takes >150ms on 10,000+ symbol indexes",
            description = "Need to memoize the trie index and offload scoring to a background coroutine dispatcher.",
            author = "marcusvance",
            status = "OPEN",
            priority = "HIGH",
            labels = "performance, ui",
            assignee = "Marcus Vance",
            commentsCount = 3,
            createdAt = "2 days ago"
        ),
        IssueEntity(
            id = "iss-3",
            repoId = "repo-1",
            number = 281,
            title = "Support GitHub Enterprise Server custom URL in OAuth integration",
            description = "Enterprise customers hosting private GitHub instances need configurable endpoint URLs for API and OAuth endpoints.",
            author = "security_lead",
            status = "OPEN",
            priority = "MEDIUM",
            labels = "integration, enhancement",
            assignee = "David Kim",
            commentsCount = 8,
            createdAt = "4 days ago"
        ),
        IssueEntity(
            id = "iss-4",
            repoId = "repo-1",
            number = 279,
            title = "Dark theme contrast ratio audit on code diff blocks",
            description = "Ensure +green and -red diff colors achieve WCAG 2.1 AA 4.5:1 ratio against slate background.",
            author = "a11y_bot",
            status = "CLOSED",
            priority = "LOW",
            labels = "a11y, theme",
            assignee = "Elena Rostova",
            commentsCount = 2,
            createdAt = "Last week"
        )
    )

    val releases = listOf(
        ReleaseEntity(
            id = "rel-1",
            repoId = "repo-1",
            tag = "v2.4.0",
            name = "DevFlow 2.4 - The Reactive Developer OS",
            author = "Elena Rostova",
            publishedAt = "2026-03-01",
            changelog = "• Multi-project workspaces with synchronized state\n• Real-time DORA code health metrics\n• Built-in AI assistant for PR reviews and bug detection\n• High-performance interactive OpenAPI tester\n• Command palette with Cmd+K shortcuts",
            isPrerelease = false,
            downloadCount = 14200
        ),
        ReleaseEntity(
            id = "rel-2",
            repoId = "repo-1",
            tag = "v2.3.2",
            name = "v2.3.2 Patch - Hotfix for Terminal ANSI parser",
            author = "Sara Chen",
            publishedAt = "2026-02-14",
            changelog = "• Fix ANSI 256-color truecolor rendering\n• Mitigate memory leak during 24h background telemetry streaming\n• Improved keyboard navigation in Kanban boards",
            isPrerelease = false,
            downloadCount = 8900
        )
    )

    val deployments = listOf(
        DeploymentEntity(
            id = "dep-1",
            projectId = "proj-1",
            environment = "Production",
            status = "SUCCESS",
            commitHash = "f8a92bc",
            commitMessage = "feat(palette): enhance fuzzy search and quick action execution",
            buildNumber = 842,
            durationSeconds = 64,
            deployedAt = "15m ago",
            deployedBy = "CI Automation (ReleaseBot)",
            url = "https://app.devflow.io",
            logs = """
[00:00.00] 🚀 Initializing deploy runner for production (cluster: prod-us-east-1)...
[00:02.14] 📦 Pulling commit f8a92bc491 (branch: main)
[00:08.31] 🔨 Running Gradle build & asset compilation: ./gradlew assembleRelease
[00:24.50] ✅ Build succeeded in 16.19s (dex optimization: 0.8s)
[00:26.10] 🧪 Executing test suite: 184 unit tests, 42 robolectric tests - 100% PASSED
[00:38.20] 🛡️ SAST & Dependency vulnerability check: 0 vulnerabilities found
[00:44.80] 🚢 Packaging OCI container image: ghcr.io/devflow/core:v2.4.0-842
[00:52.40] 🔄 Rolling out Kubernetes Deployment (replicas: 12/12 healthy)
[00:58.10] 🌐 Ingress route updated. Traffic shifted: 100% to canary v2.4.0
[01:04.00] ✨ Deployment verified: Health check 200 OK (latency: 14ms)
            """.trimIndent()
        ),
        DeploymentEntity(
            id = "dep-2",
            projectId = "proj-1",
            environment = "Staging",
            status = "SUCCESS",
            commitHash = "4e1178a",
            commitMessage = "perf(compose): optimize recomposition loops in code activity heatmap",
            buildNumber = 843,
            durationSeconds = 48,
            deployedAt = "1h ago",
            deployedBy = "Marcus Vance",
            url = "https://staging.devflow.io",
            logs = """
[00:00.00] 🚀 Deploying to Staging (cluster: stage-us-central)...
[00:05.10] 📦 Fetching origin/refactor/room-indices
[00:15.30] 🔨 Compiling binaries with KSP and Jetpack Room
[00:32.40] 🧪 Running integration contract tests (PASS)
[00:45.10] 🚢 Rolling update completed (replicas: 3/3 healthy)
[00:48.00] ✅ Staging ready at https://staging.devflow.io
            """.trimIndent()
        ),
        DeploymentEntity(
            id = "dep-3",
            projectId = "proj-1",
            environment = "Preview (PR #142)",
            status = "BUILDING",
            commitHash = "7bc3921",
            commitMessage = "feat(ci): Add live streaming deployment logs",
            buildNumber = 844,
            durationSeconds = 32,
            deployedAt = "Just now",
            deployedBy = "GitHub Actions",
            url = "https://pr-142.preview.devflow.io",
            logs = """
[00:00.00] 🚀 Spawning ephemeral preview environment for PR #142...
[00:04.20] 📦 Checking out PR ref refs/pull/142/merge
[00:12.80] 🔨 Running Vite & Gradle parallel compilation...
[00:28.40] ⏳ Provisioning ephemeral preview domain: pr-142.preview.devflow.io
[00:32.00] 🔄 Syncing secrets and mounting mock databases...
            """.trimIndent()
        ),
        DeploymentEntity(
            id = "dep-4",
            projectId = "proj-2",
            environment = "Production",
            status = "FAILED",
            commitHash = "91a82f3",
            commitMessage = "fix(proxy): reallocate ring buffer size to 64MB",
            buildNumber = 412,
            durationSeconds = 41,
            deployedAt = "4h ago",
            deployedBy = "Alex Rivera",
            url = "https://gw.devflow.io",
            logs = """
[00:00.00] 🚀 Deploying Gateway Core to Production...
[00:03.10] 📦 Cargo build --release --target x86_64-unknown-linux-musl
[00:25.80] 🧪 Running benchmark latency harness...
[00:39.10] ❌ PANIC: memory allocation error during eBPF socket map allocation
[00:40.50] ⚠️ Rollback initiated: Automatically reverted to build #411 (f8a92bc)
[00:41.00] 🛑 Deployment terminated with exit code 1
            """.trimIndent()
        )
    )

    val environments = listOf(
        EnvironmentEntity(
            id = "env-1",
            projectId = "proj-1",
            name = "Production",
            branch = "main",
            status = "Healthy",
            url = "https://app.devflow.io",
            cpuUsagePct = 28,
            memoryUsagePct = 54,
            replicaCount = 12,
            autoDeploy = true,
            lastDeployTime = "15m ago"
        ),
        EnvironmentEntity(
            id = "env-2",
            projectId = "proj-1",
            name = "Staging",
            branch = "staging",
            status = "Healthy",
            url = "https://staging.devflow.io",
            cpuUsagePct = 14,
            memoryUsagePct = 38,
            replicaCount = 3,
            autoDeploy = true,
            lastDeployTime = "1h ago"
        ),
        EnvironmentEntity(
            id = "env-3",
            projectId = "proj-1",
            name = "Development",
            branch = "develop",
            status = "Degrading",
            url = "https://dev.devflow.io",
            cpuUsagePct = 78,
            memoryUsagePct = 82,
            replicaCount = 2,
            autoDeploy = false,
            lastDeployTime = "6h ago"
        )
    )

    val tasks = listOf(
        TaskEntity("tsk-1", "proj-1", "Integrate GitHub OAuth token refresh flow", "Handle expired personal tokens and prompt seamless re-auth in workspace settings.", "IN_PROGRESS", "HIGH", "Sara Chen", "Tomorrow", 5),
        TaskEntity("tsk-2", "proj-1", "Implement multi-window support for Android tablets", "Provide side-by-side split pane for Code vs Terminal logs on large displays.", "TODO", "MEDIUM", "Marcus Vance", "Friday", 3),
        TaskEntity("tsk-3", "proj-1", "Cache DORA metrics in Room database for offline review", "Allow developers to view historical sprint cycle time charts while traveling.", "DONE", "LOW", "David Kim", "Completed", 2),
        TaskEntity("tsk-4", "proj-1", "AI Assistant: Code snippet test generator", "Generate unit test assertions from highlighted Composable or ViewModel code.", "REVIEW", "HIGH", "Elena Rostova", "Today", 8),
        TaskEntity("tsk-5", "proj-1", "Add OpenAPI 3.1 Swagger spec import parser", "Support uploading YAML or JSON OpenAPI schemas directly into the API Docs explorer.", "BACKLOG", "MEDIUM", "Alex Rivera", "Next Week", 5)
    )

    val docs = listOf(
        DocEntity(
            id = "doc-1",
            projectId = "proj-1",
            title = "DevFlow System Architecture",
            category = "Architecture",
            author = "Elena Rostova",
            updatedAt = "2026-03-12",
            content = """
# DevFlow Architecture Overview

DevFlow is designed as a modular **Developer Operating System (DevOS)** combining telemetry, code repositories, CI/CD pipelines, and AI intelligence into a reactive workspace.

### Core Pillars
1. **Local-First Reactive Persistence**:
   - Room Database engine with indexed entities for zero-latency UI switching.
   - Coroutine StateFlow streaming for reactive updates across all 15 modules.

2. **Integration Gateway Layer**:
   - Bi-directional GitHub integration supporting Commits, PRs, Issues, and Releases.
   - Webhook ingest and mock simulation modes for sandboxed development.

3. **Autonomous AI Developer Assistant**:
   - Integrated with Gemini models for contextual PR reviews, AST bug triage, and test synthesis.
   - Terminal error explanation with actionable patch recommendations.
            """.trimIndent()
        ),
        DocEntity(
            id = "doc-2",
            projectId = "proj-1",
            title = "Deployment Runbook & Rollback Procedures",
            category = "Runbook",
            author = "Sara Chen",
            updatedAt = "2026-03-08",
            content = """
# Production Rollback Runbook

### Automatic Circuit Breaking
If error rate exceeds 1.5% within 120 seconds of deployment:
1. Traffic immediately reroutes to prior stable revision (Green cluster).
2. Incident payload is dispatched to SRE on-call Slack channel.

### Manual Rollback via DevFlow OS
- Open **Deployments** module -> Select target environment.
- Click **Rollback** on prior successful build card.
- Confirm target SHA and inspect Kubernetes rollout events.
            """.trimIndent()
        )
    )

    val apiDocs = listOf(
        ApiDocEntity(
            id = "api-1",
            projectId = "proj-1",
            method = "GET",
            path = "/api/v1/projects/{projectId}/metrics",
            summary = "Fetch DORA and Code Health Metrics",
            description = "Returns commit frequency, lead time for changes, MTTR, and build success rate for specified project timeframe.",
            headersJson = "{\"Authorization\": \"Bearer <token>\", \"Accept\": \"application/json\"}",
            requestBodySample = "None",
            responseBodySample = "{\n  \"status\": \"success\",\n  \"data\": {\n    \"buildSuccessRate\": 0.964,\n    \"prCycleTimeHours\": 3.2,\n    \"deploymentFrequency\": \"4.8/day\",\n    \"issueResolutionDays\": 1.4\n  }\n}",
            statusCodes = "200 OK, 401 Unauthorized, 404 Not Found"
        ),
        ApiDocEntity(
            id = "api-2",
            projectId = "proj-1",
            method = "POST",
            path = "/api/v1/deployments/trigger",
            summary = "Trigger Environment Deployment",
            description = "Dispatches a new CI build and rolls out artifacts to specified environment.",
            headersJson = "{\"Authorization\": \"Bearer <token>\", \"Content-Type\": \"application/json\"}",
            requestBodySample = "{\n  \"environment\": \"Production\",\n  \"branch\": \"main\",\n  \"commit\": \"f8a92bc\",\n  \"skipTests\": false\n}",
            responseBodySample = "{\n  \"deploymentId\": \"dep-845\",\n  \"status\": \"QUEUED\",\n  \"estimatedDuration\": \"65s\",\n  \"trackingUrl\": \"https://app.devflow.io/deployments/dep-845\"\n}",
            statusCodes = "202 Accepted, 400 Bad Request, 403 Forbidden"
        ),
        ApiDocEntity(
            id = "api-3",
            projectId = "proj-1",
            method = "GET",
            path = "/api/v1/repositories/{repo}/pulls",
            summary = "List Pull Requests with CI Status",
            description = "Returns active, merged, and closed PRs with associated CI build status badges and review approvals.",
            headersJson = "{\"Authorization\": \"Bearer <token>\"}",
            requestBodySample = "None",
            responseBodySample = "[\n  {\n    \"number\": 142,\n    \"title\": \"feat(ci): Add live streaming deployment logs\",\n    \"author\": \"sarachen\",\n    \"status\": \"OPEN\",\n    \"ciStatus\": \"SUCCESS\"\n  }\n]",
            statusCodes = "200 OK, 404 Repo Not Found"
        )
    )

    val teamMembers = listOf(
        TeamMemberEntity("tm-1", "Elena Rostova", "Lead Architect", "elena@devflow.io", "elena-rostova", 342, 14, true, "ER"),
        TeamMemberEntity("tm-2", "Sara Chen", "Staff SRE & Platform", "sara@devflow.io", "sarachen", 218, 9, true, "SC"),
        TeamMemberEntity("tm-3", "Marcus Vance", "Senior Frontend Engineer", "marcus@devflow.io", "marcusvance", 189, 7, false, "MV"),
        TeamMemberEntity("tm-4", "David Kim", "Security & Core Systems", "david@devflow.io", "davidkim", 145, 5, true, "DK"),
        TeamMemberEntity("tm-5", "Alex Rivera", "Distributed Systems Specialist", "alex@devflow.io", "alexrivera", 164, 4, false, "AR")
    )

    val activityLogs = listOf(
        ActivityLogEntity("act-1", "proj-1", "DEPLOY", "ReleaseBot", "deployed build #842 to", "Production", "15m ago"),
        ActivityLogEntity("act-2", "proj-1", "COMMIT", "Elena Rostova", "pushed commit f8a92bc to", "main", "18m ago"),
        ActivityLogEntity("act-3", "proj-1", "PR", "Sara Chen", "opened PR #142 (ANSI logs) on", "devflow-android", "2h ago"),
        ActivityLogEntity("act-4", "proj-1", "ISSUE", "Elena Rostova", "assigned issue #284 to", "Sara Chen", "3h ago"),
        ActivityLogEntity("act-5", "proj-1", "TASK", "Marcus Vance", "moved task 'Tablet split-pane' to", "In Progress", "5h ago")
    )
}
