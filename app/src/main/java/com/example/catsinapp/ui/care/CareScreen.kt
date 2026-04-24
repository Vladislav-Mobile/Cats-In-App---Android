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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.catsinapp.data.DataSource
import com.example.catsinapp.data.model.CareCategory
import com.example.catsinapp.data.model.CareExtraBlock
import com.example.catsinapp.debug.AppLogger
import com.example.catsinapp.ui.components.HomeHeader
import com.example.catsinapp.ui.theme.GreenDark
import com.example.catsinapp.ui.theme.TextPrimary
import com.example.catsinapp.ui.theme.TextSecondary

private val YellowCard  = Color(0xFFF5F7D0)
private val IconBgColor = Color(0xFFF5E6C8)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CareScreen() {
    val categories       = DataSource.getCareCategories()
    var selectedCategory by remember { mutableStateOf<CareCategory?>(null) }
    val screenHeightDp   = LocalConfiguration.current.screenHeightDp

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        HomeHeader()
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            Text("Care Tips", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text("Everything you need to know for your pet's long and happy life",
                fontSize = 14.sp, color = TextSecondary, lineHeight = 20.sp)
        }
        Spacer(Modifier.height(8.dp))
        LazyVerticalGrid(
            columns               = GridCells.Fixed(3),
            modifier              = Modifier.fillMaxSize(),
            contentPadding        = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement   = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { category ->
                CareCategoryItem(category = category, onClick = {
                    // ЛОГ: открытие Bottom Sheet
                    AppLogger.careBottomSheetOpened(
                        categoryId    = category.id,
                        categoryTitle = category.title
                    )
                    selectedCategory = category
                })
            }
        }
    }

    selectedCategory?.let { cat ->
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = { selectedCategory = null },
            sheetState       = sheetState,
            containerColor   = Color.White,
            shape            = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            dragHandle       = { BottomSheetDefaults.DragHandle() }
        ) {
            val sheetHeight = (screenHeightDp * 0.75f).dp
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = sheetHeight)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 40.dp)
            ) {
                Row(
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    modifier              = Modifier.padding(bottom = 16.dp)
                ) {
                    Box(modifier = Modifier.size(52.dp).clip(CircleShape).background(Color(0xFFC8E6A0)),
                        contentAlignment = Alignment.Center) {
                        Icon(painterResource(cat.iconRes), null,
                            tint = Color(0xFF3A5C00), modifier = Modifier.size(26.dp))
                    }
                    Text(cat.title, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                }
                Box(modifier = Modifier.fillMaxWidth()
                    .background(YellowCard, RoundedCornerShape(16.dp)).padding(16.dp)) {
                    Text(cat.description, fontSize = 14.sp, color = TextPrimary, lineHeight = 22.sp)
                }
                Spacer(Modifier.height(16.dp))
                when (val extra = cat.extraBlock) {
                    is CareExtraBlock.Table        -> TableBlock(extra)
                    is CareExtraBlock.NumberedList -> NumberedListBlock(extra)
                    null                           -> Unit
                }
                cat.imageRes?.let { imgRes ->
                    Spacer(Modifier.height(16.dp))
                    Image(painterResource(imgRes), cat.title, contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth().height(200.dp).clip(RoundedCornerShape(16.dp)))
                }
            }
        }
    }
}

@Composable
private fun CareCategoryItem(category: CareCategory, onClick: () -> Unit) {
    Column(modifier = Modifier.clip(RoundedCornerShape(16.dp)).background(Color.White)
        .clickable { onClick() }.padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Box(modifier = Modifier.size(64.dp).clip(CircleShape).background(IconBgColor),
            contentAlignment = Alignment.Center) {
            Icon(painterResource(category.iconRes), category.title,
                tint = Color(0xFF6B3A00), modifier = Modifier.size(30.dp))
        }
        Text(category.title, fontSize = 12.sp, color = TextPrimary,
            fontWeight = FontWeight.Medium, textAlign = TextAlign.Center)
    }
}

@Composable
private fun TableBlock(table: CareExtraBlock.Table) {
    Column(modifier = Modifier.fillMaxWidth()) {
        if (table.title.isNotBlank()) {
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(bottom = 10.dp)) {
                Text("⏱", fontSize = 14.sp)
                Text(table.title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            }
        }
        if (table.hasHeader) {
            Row(modifier = Modifier.fillMaxWidth()
                .background(Color(0xFFEEEEE8), RoundedCornerShape(8.dp))
                .padding(horizontal = 12.dp, vertical = 6.dp)) {
                Text("FOOD TYPE",  fontSize = 11.sp, fontWeight = FontWeight.Bold,
                    color = TextSecondary, modifier = Modifier.weight(2f))
                Text("FREQUENCY", fontSize = 11.sp, fontWeight = FontWeight.Bold,
                    color = TextSecondary, modifier = Modifier.weight(2f))
                Text("PORTION",   fontSize = 11.sp, fontWeight = FontWeight.Bold,
                    color = TextSecondary, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
            }
            Spacer(Modifier.height(4.dp))
        }
        table.rows.forEach { row ->
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(8.dp).background(GreenDark, CircleShape))
                Spacer(Modifier.width(10.dp))
                Text(row.label, fontSize = 13.sp, fontWeight = FontWeight.Bold,
                    color = TextPrimary, modifier = Modifier.weight(2f))
                row.middle?.let { Text(it, fontSize = 13.sp, color = TextSecondary, modifier = Modifier.weight(2f)) }
                Text(row.value, fontSize = 13.sp, fontWeight = FontWeight.Bold,
                    color = GreenDark, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
            }
            HorizontalDivider(color = Color(0xFFEEEEE8))
        }
    }
}

@Composable
private fun NumberedListBlock(list: CareExtraBlock.NumberedList) {
    Column(modifier = Modifier.fillMaxWidth()
        .background(YellowCard, RoundedCornerShape(16.dp)).padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)) {
        list.items.forEach { item ->
            Column {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("${item.number}.", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text(item.title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                }
                Text("  ${item.text}", fontSize = 13.sp, color = TextSecondary, lineHeight = 18.sp)
            }
        }
    }
}