package com.smartfarm.android.util

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import androidx.core.content.FileProvider
import com.smartfarm.android.data.local.entity.FinanceEntry
import com.smartfarm.android.data.local.entity.FinanceType
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

object ExportManager {

    fun exportCsv(context: Context, entries: List<FinanceEntry>): Uri {
        val fmt = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val sb = StringBuilder("date,type,amount_khr,category,note,title\n")
        entries.forEach { e ->
            val date = fmt.format(Date(e.dateMillis))
            val safeTitle = e.title.replace(",", ";")
            val safeNote = e.note.replace(",", ";")
            sb.append("$date,${e.type},${e.amount.toLong()},${e.category},$safeNote,$safeTitle\n")
        }
        val file = File(context.cacheDir, "smartfarm_finance.csv")
        file.writeText(sb.toString(), Charsets.UTF_8)
        return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    }

    fun exportPdf(context: Context, entries: List<FinanceEntry>, income: Double, expense: Double): Uri {
        val doc = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = doc.startPage(pageInfo)
        val canvas = page.canvas
        val paint = Paint().apply { isAntiAlias = true }

        // Title
        paint.textSize = 22f; paint.isFakeBoldText = true
        paint.color = android.graphics.Color.parseColor("#2E6E43")
        canvas.drawText("SmartFarm Report", 50f, 70f, paint)

        // Date
        paint.textSize = 11f; paint.isFakeBoldText = false
        paint.color = android.graphics.Color.GRAY
        canvas.drawText(SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date()), 50f, 95f, paint)

        // Divider
        paint.color = android.graphics.Color.LTGRAY; paint.strokeWidth = 1f
        canvas.drawLine(50f, 108f, 545f, 108f, paint)

        // Summary
        paint.color = android.graphics.Color.BLACK; paint.isFakeBoldText = true; paint.textSize = 13f
        canvas.drawText("Summary", 50f, 135f, paint)

        paint.isFakeBoldText = false; paint.textSize = 11f
        paint.color = android.graphics.Color.parseColor("#43A047")
        canvas.drawText("ចំណូល:  ${"%,.0f".format(income)} ៛", 62f, 157f, paint)
        paint.color = android.graphics.Color.parseColor("#E53935")
        canvas.drawText("ចំណាយ: ${"%,.0f".format(expense)} ៛", 62f, 175f, paint)
        paint.color = if (income >= expense) android.graphics.Color.parseColor("#43A047") else android.graphics.Color.parseColor("#E53935")
        canvas.drawText("ចំណេញ:  ${"%,.0f".format(income - expense)} ៛", 62f, 193f, paint)

        // Divider
        paint.color = android.graphics.Color.LTGRAY
        canvas.drawLine(50f, 205f, 545f, 205f, paint)

        // Table header
        paint.color = android.graphics.Color.BLACK; paint.isFakeBoldText = true; paint.textSize = 13f
        canvas.drawText("Transactions", 50f, 228f, paint)

        paint.textSize = 9f; paint.color = android.graphics.Color.GRAY
        val cols = floatArrayOf(50f, 110f, 200f, 300f, 390f)
        val headers = arrayOf("Date", "Type", "Amount (KHR)", "Category", "Title")
        headers.forEachIndexed { i, h -> canvas.drawText(h, cols[i], 248f, paint) }

        paint.color = android.graphics.Color.LTGRAY
        canvas.drawLine(50f, 254f, 545f, 254f, paint)

        // Rows
        paint.color = android.graphics.Color.DKGRAY; paint.isFakeBoldText = false
        val dateFmt = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
        var y = 268f
        for (e in entries.take(25)) {
            if (y > 810f) break
            val row = arrayOf(
                dateFmt.format(Date(e.dateMillis)),
                e.type.name,
                "%,.0f".format(e.amount),
                e.category,
                e.title.take(20)
            )
            row.forEachIndexed { i, s -> canvas.drawText(s, cols[i], y, paint) }
            y += 18f
        }

        doc.finishPage(page)
        val file = File(context.cacheDir, "smartfarm_report.pdf")
        FileOutputStream(file).use { doc.writeTo(it) }
        doc.close()
        return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    }

    fun share(context: Context, uri: Uri, mimeType: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = mimeType
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, null))
    }
}
