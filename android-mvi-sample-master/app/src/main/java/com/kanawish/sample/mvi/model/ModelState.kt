package com.kanawish.sample.mvi.model

import java.util.*

/**
 * ModelState holds all the states we track in the Model.
 */
data class ModelState(
        val tasks: List<Task>,
        val filter: FilterType,
        val syncState: SyncState
)

/**
 * Task Model
 */
data class Task(
        val id: String = UUID.randomUUID().toString(),
        val lastUpdate: Long,
        val title: String = "New Task",
        val description: String = "",
        val completed: Boolean = false
)

/**
 * Used with the filter spinner in the tasks list.
 */
enum class FilterType(val predicate: (Task) -> Boolean) {
    ANY({ _ -> true }),            // Do not filter tasks.
    ACTIVE({ task -> !task.completed }),     // Filters only the active (not completed yet) tasks.
    COMPLETE({ task -> task.completed })        // Filters only the completed tasks.
}

/**
 * Tasks Sync State
 *
 * Represents the app's current synchronization state.
 */
sealed class SyncState {
    object IDLE : SyncState() {
        override fun toString(): String = "IDLE"
    }

    data class PROCESS(val type: Type) : SyncState() {
        enum class Type {
            REFRESH, CREATE, UPDATE
        }
    }

    data class ERROR(val throwable: Throwable) : SyncState()
}

/*
    State Diagram for SyncState

    @startuml
    [*] --> IDLE
    IDLE --> PROCESS : refresh tasks
    IDLE --> PROCESS : create task
    IDLE --> PROCESS : update task

    PROCESS --> [*] : success
    PROCESS --> ERROR : failed
    ERROR-->[*]
    @enduml
 */
