package com.project.android_pdf_2

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity() {
    private var pageHeight = 1120
    private var pagewidth = 792
    private var bmp: Bitmap? = null
    private var scaledbmp: Bitmap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bmp = BitmapFactory.decodeResource(resources, R.drawable.ic_dummy)
        scaledbmp = Bitmap.createScaledBitmap(bmp!!, 100, 100, false)
    }

    fun generatePDF(view: View?) {
        val text : String = "NRK Biz Park"
        val pdfDocument = PdfDocument()
        val textPaint = Paint()
        val myPageInfo = PageInfo.Builder(pagewidth, pageHeight, 1).create()
        val myPage = pdfDocument.startPage(myPageInfo)
        val canvas = myPage.canvas
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        textPaint.textSize = 16f
        textPaint.color = ContextCompat.getColor(this, R.color.black)
        textPaint.textAlign = Paint.Align.CENTER
        canvas.drawText(text, (canvas.width / 2).toFloat(), 15f, textPaint)
        pdfDocument.finishPage(myPage)
        @SuppressLint("SimpleDateFormat")
        val formattedDate = SimpleDateFormat("dd-MM-yyyy HH_mm_ss")
        val date = Date()
        val fileNameWithDate = formattedDate.format(date)
        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
            "ReportFile - $fileNameWithDate.pdf"
        )
        try {
            pdfDocument.writeTo(FileOutputStream(file))
            Toast.makeText(
                this@MainActivity,
                "PDF file generated successfully.",
                Toast.LENGTH_SHORT
            ).show()
            val u = FileProvider.getUriForFile(this, this.applicationInfo.packageName, file)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = u
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            startActivity(intent)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        pdfDocument.close()
    }

}