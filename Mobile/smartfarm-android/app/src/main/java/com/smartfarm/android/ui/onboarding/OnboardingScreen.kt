package com.smartfarm.android.ui.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

private data class OnboardingPage(
    val icon: ImageVector,
    val title: String,
    val subtitle: String,
    val color: Color
)

private val primaryGreen = Color(0xFF2E6E43)

private val pages = listOf(
    OnboardingPage(
        icon = Icons.Default.Eco,
        title = "ស្វាគមន៍មកកាន់\nSmartFarm",
        subtitle = "ជំនួយការគ្រប់គ្រងកសិដ្ឋាន\nសម្រាប់កសិករខ្មែរ",
        color = primaryGreen
    ),
    OnboardingPage(
        icon = Icons.Default.AttachMoney,
        title = "ហិរញ្ញវត្ថុ & ប្រតិទិន",
        subtitle = "តាមដានចំណូល ចំណាយ\nនិងសកម្មភាពកសិកម្មប្រចាំថ្ងៃ",
        color = Color(0xFF1565C0)
    ),
    OnboardingPage(
        icon = Icons.Default.Security,
        title = "ឯកជន & Offline",
        subtitle = "ទិន្នន័យទាំងអស់ត្រូវបានរក្សាទុក\nក្នុងឧបករណ៍របស់អ្នក",
        color = Color(0xFFE65100)
    )
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(onComplete: () -> Unit) {
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { index ->
            OnboardingPageContent(page = pages[index])
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 52.dp)
                .padding(horizontal = 32.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                pages.indices.forEach { i ->
                    val isSelected = i == pagerState.currentPage
                    Box(
                        modifier = Modifier
                            .height(8.dp)
                            .width(if (isSelected) 24.dp else 8.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) primaryGreen else Color.LightGray)
                    )
                }
            }

            Button(
                onClick = {
                    if (pagerState.currentPage < pages.size - 1) {
                        scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                    } else {
                        onComplete()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryGreen),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = if (pagerState.currentPage < pages.size - 1) "បន្ទាប់" else "ចាប់ផ្ដើម",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun OnboardingPageContent(page: OnboardingPage) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(140.dp)
                .clip(CircleShape)
                .background(page.color.copy(alpha = 0.12f))
        ) {
            Icon(
                imageVector = page.icon,
                contentDescription = null,
                tint = page.color,
                modifier = Modifier.size(72.dp)
            )
        }
        Spacer(Modifier.height(32.dp))
        Text(
            text = page.title,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            lineHeight = 36.sp
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = page.subtitle,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
            lineHeight = 24.sp
        )
    }
}
