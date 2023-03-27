package com.civileva.table.example.presentation

import android.view.View

interface ITableAdapter<T> {
	fun getTableSize(): Int
	fun getViews(): Array<View>
	fun getData(): Array<T>

	fun getView(index: Int): View
	fun getData(index: Int): T
}

interface ICompetitionTableAdapter<T> : ITableAdapter<T> {

}