package com.example.catsinapp.data.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Request
import org.json.JSONObject

/**
 * Запросы к GitHub API через OkHttp.
 * Все вызовы видны в Android Studio → App Inspection → Network Inspector.
 * WebView-трафик (браузер внутри приложения) в Network Inspector НЕ отображается —
 * WebView использует Chromium, а не OkHttp.
 */
object GitHubRepository {

    private const val REPO_API = "https://api.github.com/repos/Vladislav-Mobile/Cats-In-App---Android"
    private const val COMMITS_API = "$REPO_API/commits?per_page=5"
    private const val BRANCHES_API = "$REPO_API/branches"

    data class RepoStats(
        val name: String,
        val stars: Int,
        val forks: Int,
        val openIssues: Int,
        val defaultBranch: String,
        val language: String
    )

    /** Информация о репозитории — 1 запрос, виден в Network Inspector */
    suspend fun fetchRepoStats(): RepoStats? = withContext(Dispatchers.IO) {
        try {
            val req = Request.Builder()
                .url(REPO_API)
                .header("Accept", "application/vnd.github.v3+json")
                .build()
            val resp = NetworkClient.okHttpClient.newCall(req).execute()
            if (resp.isSuccessful) {
                val json = JSONObject(resp.body?.string() ?: return@withContext null)
                RepoStats(
                    name          = json.optString("name", ""),
                    stars         = json.optInt("stargazers_count", 0),
                    forks         = json.optInt("forks_count", 0),
                    openIssues    = json.optInt("open_issues_count", 0),
                    defaultBranch = json.optString("default_branch", "main"),
                    language      = json.optString("language", "Kotlin")
                )
            } else null
        } catch (e: Exception) { null }
    }

    /** Последние 5 коммитов — 2-й запрос, виден в Network Inspector */
    suspend fun fetchRecentCommits(): List<String> = withContext(Dispatchers.IO) {
        try {
            val req = Request.Builder()
                .url(COMMITS_API)
                .header("Accept", "application/vnd.github.v3+json")
                .build()
            val resp = NetworkClient.okHttpClient.newCall(req).execute()
            if (resp.isSuccessful) {
                val body = resp.body?.string() ?: return@withContext emptyList()
                val arr = org.json.JSONArray(body)
                (0 until minOf(arr.length(), 5)).map { i ->
                    arr.getJSONObject(i)
                        .getJSONObject("commit")
                        .optString("message", "")
                        .lines().first()
                }
            } else emptyList()
        } catch (e: Exception) { emptyList() }
    }

    /** Список веток — 3-й запрос, виден в Network Inspector */
    suspend fun fetchBranches(): List<String> = withContext(Dispatchers.IO) {
        try {
            val req = Request.Builder()
                .url(BRANCHES_API)
                .header("Accept", "application/vnd.github.v3+json")
                .build()
            val resp = NetworkClient.okHttpClient.newCall(req).execute()
            if (resp.isSuccessful) {
                val body = resp.body?.string() ?: return@withContext emptyList()
                val arr = org.json.JSONArray(body)
                (0 until arr.length()).map { i ->
                    arr.getJSONObject(i).optString("name", "")
                }
            } else emptyList()
        } catch (e: Exception) { emptyList() }
    }
}
