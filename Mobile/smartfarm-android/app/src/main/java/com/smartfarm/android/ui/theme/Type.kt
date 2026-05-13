package com.smartfarm.android.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.smartfarm.android.R

val HanumanFontFamily = FontFamily(
    Font(R.font.hanuman_regular, FontWeight.Normal),
    Font(R.font.hanuman_bold,    FontWeight.Bold),
    Font(R.font.hanuman_bold,    FontWeight.SemiBold),
    Font(R.font.hanuman_bold,    FontWeight.Medium)
)

val InterFontFamily = FontFamily(
    Font(R.font.inter_regular,  FontWeight.Normal),
    Font(R.font.inter_medium,   FontWeight.Medium),
    Font(R.font.inter_semibold, FontWeight.SemiBold),
    Font(R.font.inter_bold,     FontWeight.Bold)
)

fun smartFarmTypography(language: String): Typography {
    val family = if (language == "km") HanumanFontFamily else InterFontFamily
    return Typography(
        displayLarge  = TextStyle(fontFamily = family, fontWeight = FontWeight.Bold,     fontSize = 57.sp, lineHeight = 64.sp),
        displayMedium = TextStyle(fontFamily = family, fontWeight = FontWeight.Bold,     fontSize = 45.sp, lineHeight = 52.sp),
        headlineLarge = TextStyle(fontFamily = family, fontWeight = FontWeight.Bold,     fontSize = 32.sp, lineHeight = 40.sp),
        headlineMedium= TextStyle(fontFamily = family, fontWeight = FontWeight.SemiBold, fontSize = 28.sp, lineHeight = 36.sp),
        headlineSmall = TextStyle(fontFamily = family, fontWeight = FontWeight.SemiBold, fontSize = 24.sp, lineHeight = 32.sp),
        titleLarge    = TextStyle(fontFamily = family, fontWeight = FontWeight.SemiBold, fontSize = 22.sp, lineHeight = 28.sp),
        titleMedium   = TextStyle(fontFamily = family, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, lineHeight = 24.sp),
        titleSmall    = TextStyle(fontFamily = family, fontWeight = FontWeight.Medium,   fontSize = 14.sp, lineHeight = 20.sp),
        bodyLarge     = TextStyle(fontFamily = family, fontWeight = FontWeight.Normal,   fontSize = 16.sp, lineHeight = 24.sp),
        bodyMedium    = TextStyle(fontFamily = family, fontWeight = FontWeight.Normal,   fontSize = 14.sp, lineHeight = 20.sp),
        bodySmall     = TextStyle(fontFamily = family, fontWeight = FontWeight.Normal,   fontSize = 12.sp, lineHeight = 16.sp),
        labelLarge    = TextStyle(fontFamily = family, fontWeight = FontWeight.Medium,   fontSize = 14.sp, lineHeight = 20.sp),
        labelMedium   = TextStyle(fontFamily = family, fontWeight = FontWeight.Medium,   fontSize = 12.sp, lineHeight = 16.sp),
        labelSmall    = TextStyle(fontFamily = family, fontWeight = FontWeight.Medium,   fontSize = 11.sp, lineHeight = 16.sp)
    )
}
