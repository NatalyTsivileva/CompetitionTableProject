package com.civileva.table.example.presentation.base.adapter

import com.civileva.table.example.data.ITableCell
import com.civileva.table.example.presentation.base.holders.ITableViewHolder

interface ITableAdapter<T : Comparable<T>, C : ITableCell<T>> {
	fun getTableSize(): Int
	fun getTableData(): List<C>
	fun getTableData(index: Int): C

	fun getTableHolders(): List<ITableViewHolder<T, C>>
	fun getTableHolder(cell: ITableCell<T>): ITableViewHolder<T, C>?

	fun releaseTableDataViewHolders()

}