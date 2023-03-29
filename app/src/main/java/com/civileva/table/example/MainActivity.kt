package com.civileva.table.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.civileva.table.example.data.CellInteger
import com.civileva.table.example.presentation.adapter.CompetitionTableAdapter
import com.civileva.table.example.utils.TableLegendUtils
import com.civileva.table.example.utils.TableUtils
import com.civileva.table.example.widget.TableView
import com.civileva.table.test.R

class MainActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		initTable()
	}

	private fun initTable() {
		val tableView: TableView<Int, CellInteger> = findViewById(R.id.tableView)

		val tableData = TableUtils.createIntegerTable(7)

		tableView.tableAdapter = CompetitionTableAdapter(
			applicationContext,
			tableView.attrs,
			tableData,
			TableLegendUtils.createCompetitionLegendPanel(applicationContext, tableData)
		)
	}
}