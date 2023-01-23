package com.project.android_pdf_2

import android.app.Dialog
import android.view.View
import android.view.Window
import com.google.android.material.snackbar.Snackbar

/**
 * Created by rahul on 19-12-2022
 */

object AppProgressDialog {
    fun snack(container: View?, msg: String?, title: String?, listener: View.OnClickListener?) {
        val snackBar = Snackbar
            .make(container!!, msg!!, Snackbar.LENGTH_INDEFINITE)
            .setAction(title, listener)
        snackBar.show()
    }

    fun show(mProgressDialog: Dialog) {
        try {
            if (mProgressDialog.isShowing) return
            mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            mProgressDialog.setContentView(R.layout.layout_progress_bar)
            mProgressDialog.setCancelable(false)
            if (mProgressDialog.window != null) mProgressDialog.window!!.setBackgroundDrawableResource(
                android.R.color.transparent
            )
            mProgressDialog.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun hide(mProgressDialog: Dialog?) {
        try {
            if (mProgressDialog != null && mProgressDialog.isShowing) {
                mProgressDialog.dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}