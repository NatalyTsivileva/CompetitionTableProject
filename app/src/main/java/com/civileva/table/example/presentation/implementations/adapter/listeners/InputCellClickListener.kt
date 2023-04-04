package com.civileva.table.example.presentation.implementations.adapter.listeners

import com.civileva.table.example.data.CellInteger
import com.civileva.table.example.data.ITableCell
import com.civileva.table.example.presentation.base.listener.ITableClickListener

interface InputCellClickListener:ITableClickListener<Int, CellInteger> {

	override fun onClick(cell: ITableCell<Int>) {
		//nothing
	}

	fun onDataInput(cell: CellInteger, data: Int)
}