package com.sample.foo.labsof

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import java.time.*

@RequiresApi(Build.VERSION_CODES.O)
class FechaDialog() : DialogFragment() {
    private var day: Int
    private var month: Int
    private var year: Int
    private var observer: DatePickerDialog.OnDateSetListener? = null
    private var minDate: Long? = null
    private var maxDate: Long? = null

    init {
        LocalDate.now().let { now ->

            year = now.year
            month = now.monthValue
            day = now.dayOfMonth
        }
    }


    companion object {
        const val YEAR_ARG = "args.year"
        const val MONTH_ARG = "args.month"
        const val DAY_ARG = "args.day"
        const val MINDATE_ARG = "args.day"
        const val MAXDATE_ARG = "args.day"

        fun newInstance(
            year: Int, month: Int, day: Int, minDate: Long? = null, maxDate: Long? = null,
            observer: DatePickerDialog.OnDateSetListener
        ): FechaDialog {
            val datePicker = FechaDialog()
            datePicker.arguments = Bundle().apply {
                val adjustMonth = month - 1

                putInt(YEAR_ARG, year)
                putInt(MONTH_ARG, adjustMonth)
                putInt(DAY_ARG, day)
                if (minDate != null) {
                    putLong(MINDATE_ARG, minDate)
                }
                if (maxDate != null) {
                    putLong(MAXDATE_ARG, maxDate)
                }
            }
            datePicker.observer = observer
            return datePicker
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { args ->
            year = args.getInt(YEAR_ARG)
            month = args.getInt(MONTH_ARG)
            day = args.getInt(DAY_ARG)
            minDate = args.getLong(MINDATE_ARG)
            maxDate = args.getLong(MAXDATE_ARG)

        }

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog =
            DatePickerDialog(requireContext(), R.style.DialogTheme, observer, year, month, day)
        dialog.datePicker.minDate = minDate!!
        dialog.datePicker.minDate = maxDate!!

        return dialog
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
    }
}