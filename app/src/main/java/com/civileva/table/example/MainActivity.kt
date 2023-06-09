package com.civileva.table.example

import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.civileva.table.example.data.CellInteger
import com.civileva.table.example.data.Sorting
import com.civileva.table.example.data.Table
import com.civileva.table.example.presentation.base.legends.ILegendPanel
import com.civileva.table.example.presentation.implementations.adapter.CompetitionLegendTableAdapter
import com.civileva.table.example.presentation.implementations.adapter.listeners.InputCellClickListener
import com.civileva.table.example.presentation.widget.LegendHeaderTableView
import com.civileva.table.example.utils.TableAdapterUtils
import com.civileva.table.example.utils.TableUtils
import com.civileva.table.test.R

class MainActivity : AppCompatActivity() {
	private var tableSize = 7
	private var tableView: LegendHeaderTableView? = null
	private var tableAdapter: CompetitionLegendTableAdapter? = null
	private var tableData: Table<Int, CellInteger>? = null

	private val cellListener = object : InputCellClickListener {
		override fun onDataInput(cell: CellInteger, data: Int) {
			tableData?.updateCellData(cell, data)
			val cursor = tableData?.getCursor(cell.rowNumber)
			if (cursor != null)
				if (cursor.isRowFullFilled) {
					updateScore(rowNumber = cursor.rowNumber + 1, score = cursor.dataSum.toString())
					updatePlaces()
				} else {
					clearPlaces()
					updateScore(rowNumber = cursor.rowNumber + 1, score = "")
				}
		}
	}

	private fun updateScore(rowNumber: Int, score: Any) {
		tableAdapter?.updateLegendPanel(
			panelId = ILegendPanel.RIGHT_SCORE,
			viewIndex = rowNumber,
			data = score
		)
	}

	private fun updatePlaces() {
		val places = tableData
			?.sortTableRows(Sorting.Direction.DESC)
			?: emptyList()

		places.forEach {
			tableAdapter?.updateLegendPanel(
				ILegendPanel.RIGHT_PLACE,
				it.cursor.rowNumber + 1,
				(it.order + 1).toString()
			)
		}
	}

	private fun clearPlaces() {
		tableAdapter
			?.getLegendPanelViewsHolder(ILegendPanel.RIGHT_PLACE)
			?.getPanelViews()
			?.forEachIndexed { index, view ->
				if (index != 0) {
					tableAdapter?.updateLegendPanel(ILegendPanel.RIGHT_PLACE, index, " ")
				}
			}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		initTable(tableSize)
	}

	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		menuInflater.inflate(R.menu.main_menu, menu)
		return true
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		return when (item.itemId) {
			R.id.menuLegendFull -> {
				initTable(tableSize)
				true
			}

			R.id.menuLegendOnlyIteration -> {
				initTable(
					size = tableSize,
					excludePanelsIds = listOf(
						ILegendPanel.LEFT_COMPETITION,
						ILegendPanel.RIGHT_SCORE,
						ILegendPanel.RIGHT_PLACE,
						ILegendPanel.GAME
					)
				)
				true
			}

			R.id.menuTable4x4 -> {
				tableSize = 4
				initTable(tableSize)
				true
			}

			R.id.menuTable7x7 -> {
				tableSize = 7
				initTable(tableSize)
				true
			}

			R.id.menuTable10x10 -> {
				tableSize = 10
				initTable(tableSize)
				true
			}

			R.id.menuTableDelete -> {
				tableView?.clear()
				true
			}

			R.id.menuRules->{
				showRulesDialog()
				true
			}
			else -> super.onOptionsItemSelected(item)
		}
	}

	private fun initTable(
		size: Int,
		excludePanelsIds: List<Int> = emptyList()
	) {
		tableView = findViewById(R.id.tableView)
		tableView?.clear()

		with(TableUtils.createIntegerTable(size)) {

			var legendPanelHolders = TableAdapterUtils.createLegendsHolderMap(this@MainActivity, this)
			legendPanelHolders = legendPanelHolders.filter { !excludePanelsIds.contains(it.key.id) }

			val cellHolders = TableUtils.createCellHolders(this, this@MainActivity)


			tableView?.apply {
				val compAdapter = CompetitionLegendTableAdapter(
					this@with,
					cellListener,
					cellHolders,
					legendPanelHolders
				)
				setLegendAdapter(compAdapter)
				setCellAdapter(compAdapter)
				tableAdapter = compAdapter
			}

			tableData = this
		}


	}


	private fun showRulesDialog(){
		val okText = getString(R.string.dialog_btn_ok)
		val rules = getString(R.string.menu_rules)
		val dialogText = getString(R.string.menu_rules_text)
		 AlertDialog.Builder(this,R.style.DialogStyle)
			 .setTitle(rules)
			 .setMessage(dialogText)
			 .setPositiveButton(okText) { p0, p1 -> p0?.dismiss() }
			 .show()
	}
}