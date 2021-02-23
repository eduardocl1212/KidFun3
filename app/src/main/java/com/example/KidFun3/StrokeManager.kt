package com.example.KidFun3

import android.content.Context
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import com.google.mlkit.common.MlKitException
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.vision.digitalink.*
import com.example.KidFun3.StrokeManager.corr1 as corr11

object StrokeManager {
    private var inkBuilder = Ink.builder()
    private var strokeBuilder = Ink.Stroke.builder()
    private lateinit var model: DigitalInkRecognitionModel
    var corr1 = 0

    fun addNewTouchEvent(event: MotionEvent) {
        val action = event.actionMasked
        val x = event.x
        val y = event.y
        val t = System.currentTimeMillis()

        when (action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> strokeBuilder.addPoint(
                Ink.Point.create(
                    x,
                    y,
                    t
                )
            )
            MotionEvent.ACTION_UP -> {
                strokeBuilder.addPoint(Ink.Point.create(x, y, t))
                inkBuilder.addStroke(strokeBuilder.build())
                strokeBuilder = Ink.Stroke.builder()

            }
            else -> {
                // Action not relevant for ink construction
            }
        }
    }

    fun download(context: Context) {
        var modelIdentifier: DigitalInkRecognitionModelIdentifier? = null
        try {
            modelIdentifier =
                //DigitalInkRecognitionModelIdentifier.fromLanguageTag("zxx-Zsym-x-autodraw")
                DigitalInkRecognitionModelIdentifier.fromLanguageTag("es-ES")
        } catch (e: MlKitException) {
            // language tag failed to parse, handle error.
        }

        model =
            DigitalInkRecognitionModel.builder(modelIdentifier!!).build()

        val remoteModelManager = RemoteModelManager.getInstance()
        remoteModelManager.download(model, DownloadConditions.Builder().build())
            .addOnSuccessListener {
                Log.i("StrokeManager", "Model downloaded")
                Toast.makeText(context, "Estoy list@", Toast.LENGTH_SHORT).show();

            }
            .addOnFailureListener { e: Exception ->
                Log.e("StrokeManager", "Error while downloading a model: $e")
                Toast.makeText(context, "Opps.., ¿Tienes internet?", Toast.LENGTH_SHORT).show();
            }
    }

    fun recognize(context: Context, letra: String): Int {

        
        val recognizer: DigitalInkRecognizer =
            DigitalInkRecognition.getClient(
                DigitalInkRecognizerOptions.builder(model).build()
            )

        val ink = inkBuilder.build()
        recognizer.recognize(ink).addOnSuccessListener { result: RecognitionResult ->
            corr11 = 100    
            if (letra.equals(result.candidates[0].text)){
                    corr11 = 10
                    //Toast.makeText(context, "Has escrito: ${result.candidates[0].text}", Toast.LENGTH_SHORT).show()
                    //Toast.makeText(context, "Correcto!!!: " + corr1, Toast.LENGTH_LONG).show()
                }
                else{
                    //Toast.makeText(context, "Incorrecto!!!", Toast.LENGTH_LONG).show()
                    corr11 = -10
                    Toast.makeText(context, "Has escrito: ${result.candidates[0].text}", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e: Exception ->
                Log.e("StrokeManager", "Error during recognition: $e")
            }
        return corr11
    }

    fun clear() {
        inkBuilder = Ink.builder()
    }


}