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
    private var verticallyWidth = 50f
    private var bmp: Bitmap? = null
    private var scaledbmp: Bitmap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bmp = BitmapFactory.decodeResource(resources, R.drawable.ic_dummy)
        bmp = BitmapFactory.decodeResource(resources, R.drawable.ic_dummy)
        scaledbmp = Bitmap.createScaledBitmap(bmp!!, 100, 100, false)
    }

    fun generatePDF(view: View?) {
        val pdfDocument = PdfDocument()
        val textPaint = Paint()
        val imagePaint = Paint()
        val rect = Rect()
        val myPageInfo = PageInfo.Builder(pagewidth, pageHeight, 1).create()
        val myPage = pdfDocument.startPage(myPageInfo)
        val canvas = myPage.canvas
        //Title Heading
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        textPaint.textSize = 18f
        textPaint.color = ContextCompat.getColor(this, R.color.black)
        textPaint.textAlign = Paint.Align.CENTER
        canvas.drawText("NRK Biz Park", (canvas.width / 2).toFloat(), verticallyWidth, textPaint)
        //Location Name
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        textPaint.textSize = 16f
        textPaint.color = ContextCompat.getColor(this, R.color.black)
        textPaint.textAlign = Paint.Align.CENTER
        verticallyWidth += 30
        canvas.drawText("Vijay Nagar", (canvas.width / 2).toFloat(), verticallyWidth, textPaint)

        //Total Inspection Count
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        textPaint.textSize = 16f
        textPaint.color = ContextCompat.getColor(this, R.color.black)
        textPaint.textAlign = Paint.Align.CENTER
        verticallyWidth += 30
        canvas.drawText("12", (canvas.width / 2).toFloat(), verticallyWidth, textPaint)

        //Date
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        textPaint.textSize = 16f
        textPaint.color = ContextCompat.getColor(this, R.color.black)
        textPaint.textAlign = Paint.Align.CENTER
        verticallyWidth += 30
        canvas.drawText("06-Jan-2023", (canvas.width / 2).toFloat(), verticallyWidth, textPaint)

        //Client Name
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        textPaint.textSize = 16f
        textPaint.color = ContextCompat.getColor(this, R.color.black)
        textPaint.textAlign = Paint.Align.CENTER
        verticallyWidth += 30
        canvas.drawText("Client Name : Sonu", (canvas.width / 2).toFloat(), verticallyWidth, textPaint)

        //Draw Project image
        verticallyWidth +=50
        canvas.drawBitmap(scaledbmp!!,(canvas.width/2 - scaledbmp!!.width/2).toFloat(),verticallyWidth,imagePaint)

        //Company Name
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        textPaint.textSize = 16f
        textPaint.color = ContextCompat.getColor(this, R.color.black)
        textPaint.textAlign = Paint.Align.CENTER
        verticallyWidth += 150
        canvas.drawText("IDA", (canvas.width / 2).toFloat(), verticallyWidth, textPaint)

        //
        val bmp = BitmapFactory.decodeResource(resources, R.drawable.sign)
        val scaledbmp = Bitmap.createScaledBitmap(bmp, 100, 100, false)
        verticallyWidth +=50
        canvas.drawBitmap(scaledbmp,(canvas.width/2 - scaledbmp.width/2).toFloat(),verticallyWidth,imagePaint)

        //Assign To Name
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        textPaint.textSize = 16f
        textPaint.color = ContextCompat.getColor(this, R.color.black)
        textPaint.textAlign = Paint.Align.CENTER
        verticallyWidth += 150
        canvas.drawText("Monu", (canvas.width / 2).toFloat(), verticallyWidth, textPaint)

        //Assign To Name
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        textPaint.textSize = 16f
        textPaint.color = ContextCompat.getColor(this, R.color.black)
        textPaint.textAlign = Paint.Align.CENTER
        verticallyWidth += 80
        canvas.drawText("Total #1 Inspection", (canvas.width / 2).toFloat(), verticallyWidth, textPaint)
        //
        val bounds = Rect()
        val circlePaint = Paint()
        circlePaint.color = Color.RED
        circlePaint.isAntiAlias = true
        imagePaint.getTextBounds("0", 0, "0".length, bounds)
        verticallyWidth += 80
        canvas.drawCircle((canvas.width / 2).toFloat()-160, verticallyWidth, (bounds.width() + 25).toFloat(), circlePaint)
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        textPaint.textSize = 18f
        textPaint.color = ContextCompat.getColor(this, R.color.black)
        textPaint.textAlign = Paint.Align.CENTER
        canvas.drawText("1", (canvas.width / 2).toFloat()-160, verticallyWidth+8, textPaint)

        //
        circlePaint.color = Color.BLUE
        canvas.drawCircle((canvas.width / 2).toFloat()-80, verticallyWidth, (bounds.width() + 25).toFloat(), circlePaint)
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        textPaint.textSize = 18f
        textPaint.color = ContextCompat.getColor(this, R.color.black)
        textPaint.textAlign = Paint.Align.CENTER
        canvas.drawText("1", (canvas.width / 2).toFloat()-80, verticallyWidth+8, textPaint)

        //
        circlePaint.color = Color.YELLOW
        canvas.drawCircle((canvas.width / 2).toFloat(), verticallyWidth, (bounds.width() + 25).toFloat(), circlePaint)
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        textPaint.textSize = 18f
        textPaint.color = ContextCompat.getColor(this, R.color.black)
        textPaint.textAlign = Paint.Align.CENTER
        canvas.drawText("1", (canvas.width / 2).toFloat(), verticallyWidth+8, textPaint)

        //
        circlePaint.color = Color.GREEN
        canvas.drawCircle((canvas.width / 2).toFloat()+80, verticallyWidth, (bounds.width() + 25).toFloat(), circlePaint)
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        textPaint.textSize = 18f
        textPaint.color = ContextCompat.getColor(this, R.color.black)
        textPaint.textAlign = Paint.Align.CENTER
        canvas.drawText("1", (canvas.width / 2).toFloat()+80, verticallyWidth+8, textPaint)
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