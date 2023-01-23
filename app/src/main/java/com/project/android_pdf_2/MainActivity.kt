package com.project.android_pdf_2

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private var pageHeight = 1120
    private var pagewidth = 792
    private var verticallyWidth = 100f
    private var inspectionListVerticallyWidth = 100f
    private var bmp: Bitmap? = null
    private var scaledbmp: Bitmap? = null
    private var inspectionList : ArrayList<String> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bmp = BitmapFactory.decodeResource(resources, R.drawable.ic_dummy)
        bmp = BitmapFactory.decodeResource(resources, R.drawable.ic_dummy)
        scaledbmp = Bitmap.createScaledBitmap(bmp!!, 100, 100, false)

        for (i in 0..20){
            inspectionList.add("")
        }

        findViewById<Button>(R.id.idBtnGeneratePDF).setOnClickListener {
            generatePdf()
        }

    }

    private fun generatePdfTest() {
        val pdfDocument = PdfDocument()
        for (i in 0..5) {
            val myPageInfo = PageInfo.Builder(pagewidth, pageHeight, i).create()
            val myPage = pdfDocument.startPage(myPageInfo)
            pdfDocument.finishPage(myPage)
        }
        @SuppressLint("SimpleDateFormat") val formattedDate =
            SimpleDateFormat("dd-MM-yyyy HH_mm_ss")
        val date = Date()
        val fileNameWithDate = formattedDate.format(date)
        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
            "ReportFile - $fileNameWithDate.pdf"
        )
        try {
            pdfDocument.writeTo(FileOutputStream(file))
            Toast.makeText(
                this@MainActivity, "PDF file generated successfully.", Toast.LENGTH_SHORT
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

    var pageNumber: Int = 0
    private fun generatePdf() {
        val pdfDocument = PdfDocument()
        var pdfSize = inspectionList.size/5
        for (i in 0..pdfSize) {
            val textPaint = Paint()
            val imagePaint = Paint()
            val myPageInfo = PageInfo.Builder(pagewidth, pageHeight, i).create()
            var myPage = pdfDocument.startPage(myPageInfo)
            val canvas = myPage.canvas
            //Page No.
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            textPaint.textSize = 16f
            textPaint.color = ContextCompat.getColor(this, R.color.black)
            textPaint.textAlign = Paint.Align.CENTER
            canvas.drawText("2 of 2", 80f, 40f, textPaint)

            for (i in 0..5) {

                if (i > 0) {
                    inspectionListVerticallyWidth += 20
                }

                if (inspectionListVerticallyWidth > 1000) {
                    inspectionListVerticallyWidth = 80f
                    //continue
                    //Log.e("inspectionListVerticallyWidth", "--------$inspectionListVerticallyWidth")
                } else {
                    //Image
                    if (inspectionList.size>0){
                        createInspectionCell(canvas, imagePaint, textPaint)
                    }
                }
            }

            //Footer
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            textPaint.textSize = 14f
            textPaint.color = ContextCompat.getColor(this, R.color.black)
            textPaint.textAlign = Paint.Align.CENTER
            canvas.drawText(
                "@2022 Inspection Audit",
                (canvas.width / 2).toFloat(),
                1080f,
                textPaint
            )

            pdfDocument.finishPage(myPage)

        }


        @SuppressLint("SimpleDateFormat") val formattedDate =
            SimpleDateFormat("dd-MM-yyyy HH_mm_ss")
        val date = Date()
        val fileNameWithDate = formattedDate.format(date)
        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
            "ReportFile - $fileNameWithDate.pdf"
        )
        try {
            pdfDocument.writeTo(FileOutputStream(file))
            Toast.makeText(
                this@MainActivity, "PDF file generated successfully.", Toast.LENGTH_SHORT
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

    private fun generateNextPagePdf() {
        val pdfDocument = PdfDocument()
        val textPaint = Paint()
        val imagePaint = Paint()

        val myPageInfo = PageInfo.Builder(pagewidth, pageHeight, pageNumber).create()
        val myPage = pdfDocument.startPage(myPageInfo)
        val canvas = myPage.canvas
        //Page No.
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        textPaint.textSize = 16f
        textPaint.color = ContextCompat.getColor(this, R.color.black)
        textPaint.textAlign = Paint.Align.CENTER
        canvas.drawText("2 of 2", 80f, 40f, textPaint)


        for (i in 0..5) {

            if (i > 0) {
                inspectionListVerticallyWidth += 20
            }

            if (inspectionListVerticallyWidth > 1000) {

            } else {
                //Image
                createInspectionCell(canvas, imagePaint, textPaint)
            }
        }

        //Footer
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        textPaint.textSize = 14f
        textPaint.color = ContextCompat.getColor(this, R.color.black)
        textPaint.textAlign = Paint.Align.CENTER
        canvas.drawText("@2022 Inspection Audit", (canvas.width / 2).toFloat(), 1080f, textPaint)

        pdfDocument.finishPage(myPage)

        @SuppressLint("SimpleDateFormat") val formattedDate =
            SimpleDateFormat("dd-MM-yyyy HH_mm_ss")
        val date = Date()
        val fileNameWithDate = formattedDate.format(date)
        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
            "ReportFile - $fileNameWithDate.pdf"
        )
        try {
            pdfDocument.writeTo(FileOutputStream(file))
            Toast.makeText(
                this@MainActivity, "PDF file generated successfully.", Toast.LENGTH_SHORT
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

    private fun createInspectionCell(
        canvas: Canvas,
        imagePaint: Paint,
        textPaint: Paint
    ) {
        val bmp = BitmapFactory.decodeResource(resources, R.drawable.ic_dummy)
        val scaledbmp = Bitmap.createScaledBitmap(bmp, 120, 160, false)
        canvas.drawBitmap(
            scaledbmp,
            (scaledbmp.width / 2).toFloat(),
            inspectionListVerticallyWidth,
            imagePaint
        )
        generateCanvasInspectionNameText(textPaint, canvas, scaledbmp, "Inspection #04")
        generateCanvasText(textPaint, canvas, scaledbmp, "Title Name")
        generateCanvasText(textPaint, canvas, scaledbmp, "Location : 1st Floor")
        generateCanvasText(textPaint, canvas, scaledbmp, "Date raised : 2-jan-2023")
        generateCanvasText(textPaint, canvas, scaledbmp, "Action Date : 2-jan-2023")
        generateCanvasText(textPaint, canvas, scaledbmp, "Assign To : 2-jan-2023")
        generateCanvasText(textPaint, canvas, scaledbmp, "Status : Closed")
        generateCanvasText(textPaint, canvas, scaledbmp, "Description : Message")

        //Draw line
        inspectionListVerticallyWidth += 20
        canvas.drawLine(
            24f,
            inspectionListVerticallyWidth,
            780f,
            inspectionListVerticallyWidth,
            imagePaint
        )
        if (inspectionList.size>0){
            inspectionList.removeAt(0)
        }
    }

    private fun generateCanvasText(
        textPaint: Paint,
        canvas: Canvas,
        scaledbmp: Bitmap,
        text: String
    ) {
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        textPaint.textSize = 14f
        textPaint.color = ContextCompat.getColor(this, R.color.black)
        textPaint.textAlign = Paint.Align.LEFT
        inspectionListVerticallyWidth += 20
        canvas.drawText(
            text,
            (scaledbmp.width + 80).toFloat(),
            inspectionListVerticallyWidth,
            textPaint
        )
    }

    private fun generateCanvasInspectionNameText(
        textPaint: Paint,
        canvas: Canvas,
        scaledbmp: Bitmap,
        text: String
    ) {
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        textPaint.textSize = 14f
        textPaint.color = ContextCompat.getColor(this, R.color.black)
        textPaint.textAlign = Paint.Align.LEFT
        inspectionListVerticallyWidth += 10
        canvas.drawText(
            text,
            (scaledbmp.width + 80).toFloat(),
            inspectionListVerticallyWidth,
            textPaint
        )
    }

    fun generatePDF(view: View?) {
        val pdfDocument = PdfDocument()
        val textPaint = Paint()
        val imagePaint = Paint()
        val rect = Rect()
        val myPageInfo = PageInfo.Builder(pagewidth, pageHeight, 1).create()
        val myPage = pdfDocument.startPage(myPageInfo)
        val canvas = myPage.canvas
        //Page No.
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        textPaint.textSize = 16f
        textPaint.color = ContextCompat.getColor(this, R.color.black)
        textPaint.textAlign = Paint.Align.CENTER
        canvas.drawText("1 of 2", 80f, 40f, textPaint)
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
        verticallyWidth += 40
        canvas.drawText("Vijay Nagar", (canvas.width / 2).toFloat(), verticallyWidth, textPaint)

        //Total Inspection Count
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        textPaint.textSize = 16f
        textPaint.color = ContextCompat.getColor(this, R.color.black)
        textPaint.textAlign = Paint.Align.CENTER
        verticallyWidth += 40
        canvas.drawText("12", (canvas.width / 2).toFloat(), verticallyWidth, textPaint)

        //Date
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        textPaint.textSize = 16f
        textPaint.color = ContextCompat.getColor(this, R.color.black)
        textPaint.textAlign = Paint.Align.CENTER
        verticallyWidth += 40
        canvas.drawText("06-Jan-2023", (canvas.width / 2).toFloat(), verticallyWidth, textPaint)

        //Client Name
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        textPaint.textSize = 16f
        textPaint.color = ContextCompat.getColor(this, R.color.black)
        textPaint.textAlign = Paint.Align.CENTER
        verticallyWidth += 40
        canvas.drawText(
            "Client Name : Sonu", (canvas.width / 2).toFloat(), verticallyWidth, textPaint
        )

        //Draw Project image
        verticallyWidth += 60
        canvas.drawBitmap(
            scaledbmp!!,
            (canvas.width / 2 - scaledbmp!!.width / 2).toFloat(),
            verticallyWidth,
            imagePaint
        )

        //Company Name
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        textPaint.textSize = 16f
        textPaint.color = ContextCompat.getColor(this, R.color.black)
        textPaint.textAlign = Paint.Align.CENTER
        verticallyWidth += 160
        canvas.drawText("IDA", (canvas.width / 2).toFloat(), verticallyWidth, textPaint)

        //
        val bmp = BitmapFactory.decodeResource(resources, R.drawable.sign)
        val scaledbmp = Bitmap.createScaledBitmap(bmp, 100, 100, false)
        verticallyWidth += 60
        canvas.drawBitmap(
            scaledbmp,
            (canvas.width / 2 - scaledbmp.width / 2).toFloat(),
            verticallyWidth,
            imagePaint
        )

        //Assign To Name
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        textPaint.textSize = 16f
        textPaint.color = ContextCompat.getColor(this, R.color.black)
        textPaint.textAlign = Paint.Align.CENTER
        verticallyWidth += 160
        canvas.drawText("Monu", (canvas.width / 2).toFloat(), verticallyWidth, textPaint)

        //Assign To Name
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        textPaint.textSize = 16f
        textPaint.color = ContextCompat.getColor(this, R.color.black)
        textPaint.textAlign = Paint.Align.CENTER
        verticallyWidth += 90
        canvas.drawText(
            "Total #1 Inspection", (canvas.width / 2).toFloat(), verticallyWidth, textPaint
        )
        //
        val bounds = Rect()
        val circlePaint = Paint()
        circlePaint.color = Color.RED
        circlePaint.isAntiAlias = true
        imagePaint.getTextBounds("0", 0, "0".length, bounds)
        verticallyWidth += 90
        canvas.drawCircle(
            (canvas.width / 2).toFloat() - 120,
            verticallyWidth,
            (bounds.width() + 25).toFloat(),
            circlePaint
        )
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        textPaint.textSize = 18f
        textPaint.color = ContextCompat.getColor(this, R.color.black)
        textPaint.textAlign = Paint.Align.CENTER
        canvas.drawText("1", (canvas.width / 2).toFloat() - 120, verticallyWidth + 8, textPaint)

        //
        circlePaint.color = Color.BLUE
        canvas.drawCircle(
            (canvas.width / 2).toFloat() - 40,
            verticallyWidth,
            (bounds.width() + 25).toFloat(),
            circlePaint
        )
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        textPaint.textSize = 18f
        textPaint.color = ContextCompat.getColor(this, R.color.black)
        textPaint.textAlign = Paint.Align.CENTER
        canvas.drawText("1", (canvas.width / 2).toFloat() - 40, verticallyWidth + 8, textPaint)

        //
        circlePaint.color = Color.YELLOW
        canvas.drawCircle(
            (canvas.width / 2).toFloat() + 40,
            verticallyWidth,
            (bounds.width() + 25).toFloat(),
            circlePaint
        )
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        textPaint.textSize = 18f
        textPaint.color = ContextCompat.getColor(this, R.color.black)
        textPaint.textAlign = Paint.Align.CENTER
        canvas.drawText("1", (canvas.width / 2).toFloat() + 40, verticallyWidth + 8, textPaint)

        //
        circlePaint.color = Color.GREEN
        canvas.drawCircle(
            (canvas.width / 2).toFloat() + 120,
            verticallyWidth,
            (bounds.width() + 25).toFloat(),
            circlePaint
        )
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        textPaint.textSize = 18f
        textPaint.color = ContextCompat.getColor(this, R.color.black)
        textPaint.textAlign = Paint.Align.CENTER
        canvas.drawText("1", (canvas.width / 2).toFloat() + 120, verticallyWidth + 8, textPaint)
        //Footer
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        textPaint.textSize = 14f
        textPaint.color = ContextCompat.getColor(this, R.color.black)
        textPaint.textAlign = Paint.Align.CENTER
        canvas.drawText("@2022 Inspection Audit", (canvas.width / 2).toFloat(), 1080f, textPaint)
        pdfDocument.finishPage(myPage)
        @SuppressLint("SimpleDateFormat") val formattedDate =
            SimpleDateFormat("dd-MM-yyyy HH_mm_ss")
        val date = Date()
        val fileNameWithDate = formattedDate.format(date)
        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
            "ReportFile - $fileNameWithDate.pdf"
        )
        try {
            pdfDocument.writeTo(FileOutputStream(file))
            Toast.makeText(
                this@MainActivity, "PDF file generated successfully.", Toast.LENGTH_SHORT
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