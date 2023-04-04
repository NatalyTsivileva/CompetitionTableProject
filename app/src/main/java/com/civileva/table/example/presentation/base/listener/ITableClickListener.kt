package com.civileva.table.example.presentation.base.listener

import com.civileva.table.example.data.ITableCell

interface ITableClickListener<T : Comparable<T>, C : ITableCell<T>> {
	fun onClick(cell: ITableCell<T>)
}