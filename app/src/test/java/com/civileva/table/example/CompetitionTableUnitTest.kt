package com.civileva.table.example

import com.civileva.table.example.data.Sorting
import com.civileva.table.example.data.Table
import com.civileva.table.example.utils.TableUtils
import org.junit.Assert
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class CompetitionTableUnitTest {

	@Test
	fun `when fill all row by scores(=1) - while row number=3 and table size=7 - should return correct Aggregate(sum=6)`() {
		val size = 7
		val rowNumber = 3
		val expectedRowSum = 6

		val table = TableUtils.createIntegerTable(size)

		for (columnIndex: Int in 0 until size) {
			val index = (rowNumber * size) + columnIndex
			table.updateCellData(table.getCell(index), 1)
		}

		val cursor = table.getCursor(rowNumber)

		Assert.assertTrue(cursor.isRowFullFilled)
	 	Assert.assertEquals(expectedRowSum, cursor.dataSum)
	}

	/**
	 * [  *   5   0  ]
	 * [  3   *   4  ]
	 * [  2   3   *  ]
	 */
	@Test
	fun `when filled all table - while size=3 - should return places 0,0,1`() {
		val size = 3

		val table = TableUtils.createIntegerTable(size)


		var cell = table.getCell(1)
		table.updateCellData(cell, 5)

		cell = table.getCell(2)
		table.updateCellData(cell, 0)

		cell = table.getCell(3)
		table.updateCellData(cell, 3)

		cell = table.getCell(5)
		table.updateCellData(cell, 4)

		cell = table.getCell(6)
		table.updateCellData(cell, 2)

		cell = table.getCell(7)
		table.updateCellData(cell, 3)

		val rewards = table.sortTableRows(Sorting.Direction.ASC)
		rewards?.forEach {
			println("ряд=${it.cursor.rowNumber}, сумма очков=${it.cursor.dataSum}, место=${it.order}")
		}
		Assert.assertEquals(0,rewards?.get(0)?.order)
		Assert.assertEquals(0,rewards?.get(1)?.order)
		Assert.assertEquals(1,rewards?.get(2)?.order)
	}

	/**
	 * [  *  -1   0  ]
	 * [  3   *   4  ]
	 * [  2   3   *  ]
	 */
	@Test
	fun `when filled all table - while size=3 and one of rows not filled - should return null`() {
		val size = 3


		val table = TableUtils.createIntegerTable(size)

		var cell = table.getCell(1)
		table.updateCellData(cell, Table.UNDEFINED_SCORE)

		cell = table.getCell(2)
		table.updateCellData(cell, 0)

		cell = table.getCell(3)
		table.updateCellData(cell, 3)

		cell = table.getCell(5)
		table.updateCellData(cell, 4)

		cell = table.getCell(6)
		table.updateCellData(cell, 2)

		cell = table.getCell(7)
		table.updateCellData(cell, 3)

		val rewards = table.sortTableRows(Sorting.Direction.ASC)
		Assert.assertEquals(null, rewards)
	}


}