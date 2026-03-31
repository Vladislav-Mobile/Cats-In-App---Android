package com.example.catsinapp.data.model

data class CareCategory(
    val id: String,
    val title: String,
    val iconRes: Int,
    val description: String,
    val imageRes: Int? = null,       // фото внизу bottom sheet (nullable)
    val extraBlock: CareExtraBlock? = null
)

sealed class CareExtraBlock {
    data class Table(
        val title: String,
        val hasHeader: Boolean = false,
        val rows: List<TableRow>
    ) : CareExtraBlock()

    data class NumberedList(
        val items: List<NumberedItem>
    ) : CareExtraBlock()
}

data class TableRow(
    val label: String,
    val middle: String? = null,
    val value: String
)

data class NumberedItem(
    val number: Int,
    val title: String,
    val text: String
)