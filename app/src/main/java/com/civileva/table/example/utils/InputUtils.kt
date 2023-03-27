package com.civileva.table.example.utils

import android.text.Editable
import androidx.appcompat.widget.AppCompatEditText

object InputUtils {
	const val FAILED_INT_INPUT = -1

	fun safeInt(editText: AppCompatEditText): Int {
		return safeInt(editText.text)
	}

	fun safeInt(editable: Editable?): Int {
		return try {
			editable?.toString()?.toInt() ?: FAILED_INT_INPUT
		} catch (e: Exception) {
			FAILED_INT_INPUT
		}
	}
}