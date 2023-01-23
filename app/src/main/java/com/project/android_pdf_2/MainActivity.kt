package com.project.android_pdf_2

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity() {
    private var pageHeight = 1120
    private var pagewidth = 792
    private var verticallyWidth = 100f
    private var inspectionListVerticallyWidth = 100f
    private var inspectionList: ArrayList<String> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dialog = Dialog(this)

        findViewById<Button>(R.id.idBtnGeneratePDF).setOnClickListener {
            if (findViewById<EditText>(R.id.etListItem).text.trim().toString().isNotEmpty()) {
                val size = findViewById<EditText>(R.id.etListItem).text.trim().toString().toInt()
                for (i in 0 until size) {
                    inspectionList.add("")
                }
                createPdfTask()
            }
        }

    }

    private var dialog: Dialog? = null
    var mPdf: File? = null
    private fun createPdfTask() {
        lifecycleScope.executeAsyncTask(
            onPreExecute = {
                AppProgressDialog.show(dialog!!)
            },
            doInBackground = {
                generatePdf()
                mPdf
            },
            onPostExecute = { file ->
                file?.let {
                    Toast.makeText(
                        this@MainActivity, "PDF file generated successfully.", Toast.LENGTH_SHORT
                    ).show()
                    val u = FileProvider.getUriForFile(this, this.applicationInfo.packageName, it)
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = u
                    intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    startActivity(intent)
                }
                AppProgressDialog.hide(dialog!!)
                resetDialog()
            }
        )
    }

    private fun resetDialog() {
        dialog = null
        dialog = Dialog(this@MainActivity)
        pdfDocument = PdfDocument()
        verticallyWidth = 100f
        inspectionListVerticallyWidth = 100f
    }

    private var pdfDocument = PdfDocument()
    private fun generatePdf() {
        var pdfPageSize = if (inspectionList.size > 5) {
            if (inspectionList.size % 5 > 0) {
                inspectionList.size / 5 + 1
            } else {
                inspectionList.size / 5
            }
        } else {
            if (inspectionList.isNotEmpty()) {
                1
            } else {
                0
            }
        }
        for (i in 0..pdfPageSize) {
            if (i == 0) {
                generatePdfFrontPage(pdfPageSize + 1)
            } else {
                val textPaint = Paint()
                val imagePaint = Paint()
                val myPageInfo = PageInfo.Builder(pagewidth, pageHeight, i + 1).create()
                var myPage = pdfDocument.startPage(myPageInfo)
                val canvas = myPage.canvas
                //Page No.
                textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                textPaint.textSize = 16f
                textPaint.color = ContextCompat.getColor(this, R.color.black)
                textPaint.textAlign = Paint.Align.CENTER
                canvas.drawText("${i + 1} of ${pdfPageSize + 1}", 80f, 40f, textPaint)
                for (ii in 0..5) {
                    if (ii > 0) {
                        inspectionListVerticallyWidth += 20
                    }
                    if (inspectionListVerticallyWidth > 1000) {
                        inspectionListVerticallyWidth = 80f
                    } else {
                        if (inspectionList.size > 0) {
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
            mPdf = file
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
        val scaledbmp = Bitmap.createScaledBitmap(bmp, 140, 160, false)
        canvas.drawBitmap(
            scaledbmp,
            60f,
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
        if (inspectionList.size > 0) {
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
            (scaledbmp.width + 100).toFloat(),
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
            (scaledbmp.width + 100).toFloat(),
            inspectionListVerticallyWidth,
            textPaint
        )
    }

    private fun generatePdfFrontPage(pageSize: Int) {
        val textPaint = Paint()
        val imagePaint = Paint()
        val myPageInfo = PageInfo.Builder(pagewidth, pageHeight, 1).create()
        val myPage = pdfDocument.startPage(myPageInfo)
        val canvas = myPage.canvas
        //Page No.
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        textPaint.textSize = 16f
        textPaint.color = ContextCompat.getColor(this, R.color.black)
        textPaint.textAlign = Paint.Align.CENTER
        canvas.drawText("1 of $pageSize", 80f, 40f, textPaint)
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
        val bmpImage = BitmapFactory.decodeResource(resources, R.drawable.ic_dummy)
        val scaledbmpImage = Bitmap.createScaledBitmap(bmpImage, 100, 100, false)
        verticallyWidth += 60
        canvas.drawBitmap(
            scaledbmpImage,
            (canvas.width / 2 - scaledbmpImage!!.width / 2).toFloat(),
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
    }

}