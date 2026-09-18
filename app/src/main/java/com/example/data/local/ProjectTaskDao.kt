package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Task Statuses standard across agile sprints.
 */
enum class ProjectTaskStatus(val code: String, val displayName: String) {
    BACKLOG("BACKLOG", "Backlog"),
    TODO("TODO", "To Do"),
    IN_PROGRESS("IN_PROGRESS", "In Progress"),
    REVIEW("REVIEW", "In Review"),
    DONE("DONE", "Done"),
    BLOCKED("BLOCKED", "Blocked");

    companion object {
        fun fromCode(code: String): ProjectTaskStatus {
            return values().firstOrNull { it.code.equals(code, ignoreCase = true) } ?: TODO
        }
    }
}

/**
 * Task Priority levels.
 */
enum class ProjectTaskPriority(val code: String, val displayName: String, val level: Int) {
    LOW("LOW", "Low Priority", 1),
    MEDIUM("MEDIUM", "Medium Priority", 2),
    HIGH("HIGH", "High Priority", 3),
    CRITICAL("CRITICAL", "Critical Priority", 4);

    companion object {
        fun fromCode(code: String): ProjectTaskPriority {
            return values().firstOrNull { it.code.equals(code, ignoreCase = true) } ?: MEDIUM
        }
    }
}

/**
 * Data Access Object for Project Tasks supporting full sprint management,
 * status filtering, and priority stratification.
 */
@Dao
interface ProjectTaskDao {

    @Query("SELECT * FROM tasks ORDER BY id ASC")
    fun getAllTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE projectId = :projectId ORDER BY priority DESC, id ASC")
    fun getTasksForProject(projectId: String): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE status = :status ORDER BY priority DESC")
    fun getTasksByStatus(status: String): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE priority = :priority ORDER BY id ASC")
    fun getTasksByPriority(priority: String): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE projectId = :projectId AND status = :status ORDER BY priority DESC")
    fun getTasksByProjectAndStatus(projectId: String, status: String): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE id = :id LIMIT 1")
    suspend fun getTaskById(id: String): TaskEntity?

    @Query("SELECT COUNT(*) FROM tasks WHERE status != 'DONE'")
    fun getActiveTaskCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<TaskEntity>)

    @Update
    suspend fun updateTask(task: TaskEntity): Int

    @Query("UPDATE tasks SET status = :newStatus WHERE id = :id")
    suspend fun updateTaskStatus(id: String, newStatus: String): Int

    @Query("UPDATE tasks SET priority = :newPriority WHERE id = :id")
    suspend fun updateTaskPriority(id: String, newPriority: String): Int

    @Delete
    suspend fun deleteTask(task: TaskEntity): Int

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteTaskById(id: String): Int
}
