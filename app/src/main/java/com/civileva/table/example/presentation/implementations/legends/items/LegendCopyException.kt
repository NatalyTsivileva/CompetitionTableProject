package com.civileva.table.example.presentation.implementations.legends.items

class LegendCopyException(
	expectedItemCount:Int,
	actualItemCount:Int
): IndexOutOfBoundsException("items count in legend=$expectedItemCount, new data list count=${actualItemCount}")