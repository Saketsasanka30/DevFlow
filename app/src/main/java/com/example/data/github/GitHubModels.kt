package com.example.data.github

data class GitHubRepoDto(
    val id: Long,
    val name: String,
    val full_name: String,
    val description: String?,
    val stargazers_count: Int,
    val forks_count: Int,
    val default_branch: String,
    val open_issues_count: Int,
    val language: String?,
    val private: Boolean,
    val html_url: String
)

data class GitHubCommitDto(
    val sha: String,
    val commit: CommitDetailDto,
    val author: AuthorDto?
)

data class CommitDetailDto(
    val message: String,
    val author: CommitAuthorDetailDto
)

data class CommitAuthorDetailDto(
    val name: String,
    val date: String
)

data class AuthorDto(
    val login: String,
    val avatar_url: String?
)

data class GitHubBranchDto(
    val name: String,
    val protected: Boolean
)

data class GitHubPrDto(
    val id: Long,
    val number: Int,
    val title: String,
    val state: String,
    val user: AuthorDto,
    val created_at: String,
    val body: String?
)

data class GitHubIssueDto(
    val id: Long,
    val number: Int,
    val title: String,
    val state: String,
    val user: AuthorDto,
    val comments: Int,
    val created_at: String,
    val body: String?
)

data class GitHubContributorDto(
    val login: String,
    val avatar_url: String,
    val contributions: Int
)

data class GitHubReleaseDto(
    val id: Long,
    val tag_name: String,
    val name: String?,
    val body: String?,
    val published_at: String,
    val prerelease: Boolean
)
