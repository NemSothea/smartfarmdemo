package com.smartfarm.android.util

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.smartfarm.android.data.local.entity.EventEntry
import com.smartfarm.android.data.local.entity.FinanceEntry
import com.smartfarm.android.data.local.entity.FinanceType
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

object BackupManager {

    fun exportJson(context: Context, entries: List<FinanceEntry>, events: List<EventEntry>): Uri {
        val root = JSONObject()
        root.put("version", 1)
        root.put("exportedAt", System.currentTimeMillis())

        val txArray = JSONArray()
        entries.forEach { e ->
            txArray.put(JSONObject().apply {
                put("title", e.title)
                put("amount", e.amount)
                put("type", e.type.name)
                put("category", e.category)
                put("note", e.note)
                put("dateMillis", e.dateMillis)
            })
        }
        root.put("transactions", txArray)

        val evArray = JSONArray()
        events.forEach { e ->
            evArray.put(JSONObject().apply {
                put("title", e.title)
                put("type", e.type)
                put("description", e.description)
                put("dateMillis", e.dateMillis)
                put("hasReminder", e.hasReminder)
            })
        }
        root.put("events", evArray)

        val file = File(context.cacheDir, "smartfarm_backup.json")
        file.writeText(root.toString(2))
        return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    }

    fun parseJson(json: String): Pair<List<FinanceEntry>, List<EventEntry>> {
        val root = JSONObject(json)

        val txArray = root.getJSONArray("transactions")
        val entries = (0 until txArray.length()).map { i ->
            val t = txArray.getJSONObject(i)
            FinanceEntry(
                title = t.getString("title"),
                amount = t.getDouble("amount"),
                type = FinanceType.valueOf(t.getString("type")),
                category = t.optString("category", ""),
                note = t.optString("note", ""),
                dateMillis = t.getLong("dateMillis")
            )
        }

        val evArray = root.getJSONArray("events")
        val events = (0 until evArray.length()).map { i ->
            val e = evArray.getJSONObject(i)
            EventEntry(
                title = e.getString("title"),
                type = e.optString("type", "ដំណាំ"),
                description = e.optString("description", ""),
                dateMillis = e.getLong("dateMillis"),
                hasReminder = e.optBoolean("hasReminder", false)
            )
        }

        return Pair(entries, events)
    }
}
