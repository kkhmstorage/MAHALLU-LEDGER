package com.example.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class SyncState {
    SYNCED,
    SYNCING,
    OFFLINE
}

class CloudSyncManager(private val scope: CoroutineScope) {

    private val _syncState = MutableStateFlow(SyncState.SYNCED)
    val syncState: StateFlow<SyncState> = _syncState.asStateFlow()

    private val _lastSyncTime = MutableStateFlow(System.currentTimeMillis())
    val lastSyncTime: StateFlow<Long> = _lastSyncTime.asStateFlow()

    private val _pendingSyncCount = MutableStateFlow(0)
    val pendingSyncCount: StateFlow<Int> = _pendingSyncCount.asStateFlow()

    fun triggerSync(onComplete: (String) -> Unit = {}) {
        scope.launch {
            _syncState.value = SyncState.SYNCING
            // Simulate network handshake and Firestore document push/pull
            delay(1200)
            _lastSyncTime.value = System.currentTimeMillis()
            _pendingSyncCount.value = 0
            _syncState.value = SyncState.SYNCED
            val timeStr = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
            onComplete("Cloud Sync Complete at $timeStr. All records backed up.")
        }
    }

    fun markLocalMutation() {
        _pendingSyncCount.value += 1
        // Auto sync after brief debounce
        scope.launch {
            delay(1500)
            triggerSync()
        }
    }
}
