package com.smartfarm.android.util

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.smartfarm.android.data.local.entity.EventEntry
import com.smartfarm.android.worker.ReminderWorker
import java.util.concurrent.TimeUnit

object NotificationScheduler {

    fun schedule(context: Context, event: EventEntry) {
        if (!event.hasReminder) return

        val now = System.currentTimeMillis()

        // 1-day-before notification
        val dayBeforeMillis = event.dateMillis - TimeUnit.DAYS.toMillis(1)
        enqueue(context, event, dayBeforeMillis, now, suffix = "before", body = "ថ្ងៃស្អែក: ${event.title}")

        // On-the-day notification (at event time)
        enqueue(context, event, event.dateMillis, now, suffix = "day", body = event.title)
    }

    private fun enqueue(context: Context, event: EventEntry, fireAt: Long, now: Long, suffix: String, body: String) {
        val delay = fireAt - now
        if (delay <= 0) return

        val data = workDataOf(
            ReminderWorker.KEY_TITLE to event.title,
            ReminderWorker.KEY_BODY to body
        )
        val request = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .addTag("event_${event.id}_$suffix")
            .build()

        WorkManager.getInstance(context).enqueue(request)
    }

    fun cancel(context: Context, event: EventEntry) {
        WorkManager.getInstance(context).cancelAllWorkByTag("event_${event.id}_before")
        WorkManager.getInstance(context).cancelAllWorkByTag("event_${event.id}_day")
    }
}
