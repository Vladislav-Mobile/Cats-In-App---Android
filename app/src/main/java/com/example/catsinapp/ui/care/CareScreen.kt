package com.example.catsinapp.ui.care

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.catsinapp.data.DataSource
import com.example.catsinapp.data.model.CareCategory
import com.example.catsinapp.data.model.CareExtraBlock
import com.example.catsinapp.ui.components.HomeHeader
import com.example.catsinapp.ui.theme.GreenDark
import com.example.catsinapp.ui.theme.TextPrimary
import com.example.catsinapp.ui.theme.TextSecondary

private val YellowCard   = Color(0xFFF5F7D0)
private val IconBgColor  = Color(0xFFF5E6C8)   // бежево-оранжевый фон иконок

@Composable
fun CareScreen() {
    val categories = DataSource.getCareCategories()
    var selectedCategory by remember { mutableStateOf<CareCategory?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header
        HomeHeader()

        // Title
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            Text(
                text       = "Care Tips",
                fontSize   = 26.sp,
                fontWeight = FontWeight.Bold,
                color      = TextPrimary
            )
            Text(
                text     = "Everything you need to know for your pet's long and happy life",
                fontSize = 14.sp,
                color    = TextSecondary,
                lineHeight = 20.sp
            )
        }

        Spacer(Modifier.height(8.dp))

        // 3-column grid
        LazyVerticalGrid(
            columns      = GridCells.Fixed(3),
            modifier     = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement   = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { category ->
                CareCategoryItem(
                    category = category,
                    onClick  = { selectedCategory = category }
                )
            }
        }
    }

    // Bottom Sheet
    selectedCategory?.let { cat ->
        CareDetailBottomSheet(
            category  = cat,
            onDismiss = { selectedCategory = null }
        )
    }
}

// ─────────────────────────────────────────────────────────
// Плитка в гриде — иконка + название
// ─────────────────────────────────────────────────────────
@Composable
private fun CareCategoryItem(category: CareCategory, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Круглая иконка с бежевым фоном
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(IconBgColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter            = painterResource(category.iconRes),
                contentDescription = category.title,
                tint               = Color(0xFF6B3A00),   // тёмно-коричневый
                modifier           = Modifier.size(30.dp)
            )
        }
        Text(
            text      = category.title,
            fontSize  = 12.sp,
            color     = TextPrimary,
            fontWeight = FontWeight.Medium,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

// ─────────────────────────────────────────────────────────
// Bottom Sheet — детальный контент категории
// ─────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CareDetailBottomSheet(
    category: CareCategory,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest    = onDismiss,
        containerColor      = Color.White,
        shape               = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        dragHandle          = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 40.dp)
        ) {
            // Header — иконка + заголовок
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFC8E6A0)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter            = painterResource(category.iconRes),
                        contentDescription = null,
                        tint               = Color(0xFF3A5C00),
                        modifier           = Modifier.size(26.dp)
                    )
                }
                Text(
                    text       = category.title,
                    fontSize   = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color      = TextPrimary
                )
            }

            // Основной текст
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(YellowCard, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Text(
                    text       = category.description,
                    fontSize   = 14.sp,
                    color      = TextPrimary,
                    lineHeight = 22.sp
                )
            }

            Spacer(Modifier.height(16.dp))

            // Extra block
            when (val extra = category.extraBlock) {
                is CareExtraBlock.Table       -> TableBlock(extra)
                is CareExtraBlock.NumberedList -> NumberedListBlock(extra)
                null                          -> Unit
            }

            // Фото внизу
            category.imageRes?.let { imgRes ->
                Spacer(Modifier.height(16.dp))
                Image(
                    painter            = painterResource(imgRes),
                    contentDescription = category.title,
                    contentScale       = ContentScale.Crop,
                    modifier           = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp))
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────
// Table block — Walks (Duration Guide) / Food (Frequency)
// ─────────────────────────────────────────────────────────
@Composable
private fun TableBlock(table: CareExtraBlock.Table) {
    Column(modifier = Modifier.fillMaxWidth()) {
        if (table.title.isNotBlank()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(bottom = 10.dp)
            ) {
                Text("⏱", fontSize = 14.sp)
                Text(
                    text       = table.title,
                    fontSize   = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color      = TextPrimary
                )
            }
        }

        // Header row (Food: FOOD TYPE / FREQUENCY / PORTION)
        if (table.hasHeader) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFEEEEE8), RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text("FOOD TYPE", fontSize = 11.sp, fontWeight = FontWeight.Bold,
                    color = TextSecondary, modifier = Modifier.weight(2f))
                Text("FREQUENCY", fontSize = 11.sp, fontWeight = FontWeight.Bold,
                    color = TextSecondary, modifier = Modifier.weight(2f))
                Text("PORTION", fontSize = 11.sp, fontWeight = FontWeight.Bold,
                    color = TextSecondary, modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.End)
            }
            Spacer(Modifier.height(4.dp))
        }

        // Rows
        table.rows.forEach { row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Bullet dot
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(GreenDark, CircleShape)
                )
                Spacer(Modifier.width(10.dp))

                Text(
                    text       = row.label,
                    fontSize   = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color      = TextPrimary,
                    modifier   = Modifier.weight(2f)
                )

                row.middle?.let { mid ->
                    Text(
                        text     = mid,
                        fontSize = 13.sp,
                        color    = TextSecondary,
                        modifier = Modifier.weight(2f)
                    )
                }

                Text(
                    text       = row.value,
                    fontSize   = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color      = GreenDark,
                    modifier   = Modifier.weight(1f),
                    textAlign  = androidx.compose.ui.text.style.TextAlign.End
                )
            }
            HorizontalDivider(color = Color(0xFFEEEEE8))
        }
    }
}

// ─────────────────────────────────────────────────────────
// Numbered list block — Health (5 пунктов)
// ─────────────────────────────────────────────────────────
@Composable
private fun NumberedListBlock(list: CareExtraBlock.NumberedList) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(YellowCard, RoundedCornerShape(16.dp))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        list.items.forEach { item ->
            Column {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text       = "${item.number}.",
                        fontSize   = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color      = TextPrimary
                    )
                    Text(
                        text       = item.title,
                        fontSize   = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color      = TextPrimary
                    )
                }
                Text(
                    text      = "  ${item.text}",
                    fontSize  = 13.sp,
                    color     = TextSecondary,
                    lineHeight = 18.sp
                )
            }
        }
    }
}