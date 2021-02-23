package com.example.KidFun3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        StrokeManager.download(this)
        val ZonaDibujo = findViewById<DrawingView>(R.id.zonaDibujo)
        val reconocer = findViewById<Button>(R.id.Verificar)
        val limpiar = findViewById<Button>(R.id.limpiar)
        val LaD = findViewById<ImageView>(R.id.ADibujar)
        //val Iniciar = findViewById<Button>(R.id.btnIniciar) //De algo servira despues
        val MsgT = findViewById<TextView>(R.id.txtWait)
        val letra = listOf("a","e","i","o","u")
        val urlGifs = listOf(
                "https://64.media.tumblr.com/0e2a4773ca385794fed1a687b077ed43/055a5199d4c375f1-7f/s500x750/4cef4aca4eedbb3cfea212a4cbb7cdfff1262c23.gif",
                "https://64.media.tumblr.com/a9f177d84ac4269399bf377d02cc5253/6e85310797b05054-6a/s500x750/19cf8e20f310fa2e3ca0a82533c721a592b2d822.gif",
                "https://64.media.tumblr.com/25833fc89fd4f621f968749a14a9d615/055a5199d4c375f1-96/s500x750/cb78e4599379ead37ca7054adf943c53921007f7.gif",
                "https://64.media.tumblr.com/7ddde17a52d7aa8e4074edf3813ba7fb/fc9cce0ae10db343-01/s500x750/4f32f1ef40c9f316461dc054a5949c066cedfe72.gif",
                "https://64.media.tumblr.com/ee8b54aaccc4861356bd2190f4abff69/fc9cce0ae10db343-7f/s500x750/e77b5f04ee6a96bd49d6b14b1918f0f66f93b226.gif"
        )

        val sel1 = letra.shuffled()
        val imgG = findIndex(letra,sel1[0])?.toInt()

        GlobalScope.launch(context = Dispatchers.Main) {
            println("Esperando... para recibir los datos")
            delay(1500)
            if(!sel1.isEmpty()){
                MsgT.setText("Dibuje Abajo")
                Glide.with(this@MainActivity).load(urlGifs[imgG!!]).into(LaD)
            }
        }



        Log.e("Interesante", "Letra: "+ sel1[0])
        Log.e("Interesante", "Interesante"+ findIndex(letra,sel1[0]))


        reconocer.setOnClickListener {
            //val dataD = StrokeManager.recognize(this, sel1[0]) //EVITAR REBOTE
            val data = StrokeManager.recognize(this, sel1[0])
            //Log.e("Interesante", "Bien o Mal: " + dataD)
            Log.e("Interesante", "Bien o Mal: " + data)
            if(data == 0){
                //TODO: Tiene un error no lo encuentro pero por lo mientras algo asi
                Toast.makeText(this,"Opps... No puedo reconocerlo, da click en reconocer de nuevo", Toast.LENGTH_SHORT).show()
            }
            else{
                if(data >= 10){
                    Log.e("Interesante", "Correcto")
                    Toast.makeText(this, "Correcto", Toast.LENGTH_SHORT).show()
                    Toast.makeText(this, "Vamos con el que sigue", Toast.LENGTH_SHORT).show()
                    val intent = intent
                    GlobalScope.launch(context = Dispatchers.Main) {
                        println("launched coroutine 1")
                        delay(1500)
                        finish()
                        startActivity(intent)
                    }

                }
                else{
                    Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show()
                    Log.e("Interesante", "Incorrecto")
                }
            }
        }



        limpiar.setOnClickListener {
            ZonaDibujo.clear()
            StrokeManager.clear()
        }
    }


    fun findIndex(arr: List<String>, item: String): Int? {
        return (arr.indices)
                .firstOrNull { i: Int -> item.equals(arr[i]) };
    }
}