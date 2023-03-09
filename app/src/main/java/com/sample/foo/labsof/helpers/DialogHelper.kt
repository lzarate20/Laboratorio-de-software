package com.sample.foo.labsof.helpers

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class DialogHelper {
    companion object{
        fun espera(view:Context): AlertDialog {
            var builder: android.app.AlertDialog.Builder =
                android.app.AlertDialog.Builder(view)
            builder.setTitle("Espere")
            builder.setMessage("Por favor espere un momento")
            builder.setCancelable(false)
            var bCreate = builder.create()
            return bCreate
        }
        fun dialogo(
            view: Context,
            titulo:String, sms:String, ok:Boolean, cancel:Boolean,
            okButton: () -> Unit,
            cancelButton:()->Unit?){
            var builder= android.app.AlertDialog.Builder(view)
            builder.setTitle(titulo)
            builder.setMessage(sms)
            if(ok){
            builder.setPositiveButton("Ok",
                DialogInterface.OnClickListener { dialog, which ->
                    okButton()
                })
            }
            if(cancel){
                builder.setNegativeButton("Cancelar",
                    DialogInterface.OnClickListener { dialog, which ->
                        cancelButton()
                    })
            }
            builder.create().show()

        }
    }
}