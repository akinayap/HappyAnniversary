package a.w.ha

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.activity_ar.*
import pl.droidsonroids.gif.GifImageView
import java.io.IOException
import java.util.*

class ARActivity : AppCompatActivity() {
    var choosing: Boolean = false
    var messages: List<String> = ArrayList()
    var currMessage = 0

    private val CAMERA_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_CODE)
        }

        LoadMessages()
        setContentView(R.layout.activity_ar)

        val otterImg = findViewById<AppCompatImageView>(R.id.otter_img)
        val tigerImg = findViewById<AppCompatImageView>(R.id.tiger_img)
        val tv = findViewById<Typewriter>(R.id.typeText)
        tv.setCharacterDelay(150)

        otterImg.visibility = View.VISIBLE
        tigerImg.visibility = View.GONE
        val msg = messages.get(currMessage).substring(2)
        tv.animateText(msg)

        tv.setOnClickListener {
            if(!choosing)
                NextLine()
        }

        val skipbtn = findViewById<Button>(R.id.skip)
        skipbtn.setOnClickListener{
            currMessage = 24
            NextLine()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_CODE -> if (grantResults.size <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "Camera Required!!!", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }


    private inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
        beginTransaction().func().commit()
    }

    private fun LoadMessages() {
        var text: String
        try {
            val inputStream = assets.open("present_messages.txt")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            text = String(buffer)
            messages = Arrays.asList(*text.split("\\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
            Log.e("size of messages", messages.size.toString())

        } catch (e: IOException) {
            e.printStackTrace()
        }

        Toast.makeText(applicationContext, "The file read operation is finished successfully.",
                Toast.LENGTH_SHORT).show()
    }

    private fun NextLine() {
        val otterImg = findViewById<AppCompatImageView>(R.id.otter_img)
        val tigerImg = findViewById<AppCompatImageView>(R.id.tiger_img)

        val heartImg = findViewById<GifImageView>(R.id.bigheart)

        val gifImg = findViewById<GifImageView>(R.id.gif_msg)
        gifImg.visibility = View.GONE

        //val bigImg = findViewById<GifImageView>(R.id.big_gif)

        val choiceBox = findViewById<ConstraintLayout>(R.id.choice_box)
        choiceBox.visibility = View.GONE

        val fragContainer = findViewById<FrameLayout>(R.id.fragmentContainer)

        var yesVal: Int
        var noVal: Int


        val tv = findViewById<Typewriter>(R.id.typeText)
        if(tv.nextText()){
            ++currMessage
            if(currMessage < messages.size){
                val sender = messages.get(currMessage).get(0)
                val msg = messages.get(currMessage).substring(2)

                if(sender == 'A'){
                    otterImg.visibility = View.GONE
                    tigerImg.visibility = View.GONE
                    //bigImg.visibility = View.GONE
                    fragContainer.visibility = View.VISIBLE
                    supportFragmentManager.inTransaction { replace(R.id.fragmentContainer, ArVideoFragment()) }
                    NextLine()
                }
                else if (sender == 'B'){
                    otterImg.visibility = View.VISIBLE
                    tigerImg.visibility = View.GONE
                    heartImg.visibility = View.VISIBLE
                    heartImg.animate()
                    tv.animateText(msg)
                }
                else if (sender == 'C') {
                    choosing = true
                    otterImg.visibility = View.GONE
                    tigerImg.visibility = View.VISIBLE
                    tv.text = ""
                    choiceBox.visibility = View.VISIBLE

                    yesVal = (((msg[0].toInt()-48) * 10) + (msg[1].toInt()-48)) - 1
                    noVal = (((msg[3].toInt()-48) * 10) + (msg[4].toInt()-48)) - 1
                    val yesbtn = findViewById<Button>(R.id.yesBtn)
                    val nobtn = findViewById<Button>(R.id.noBtn)
                    yesbtn.setOnClickListener{
                        currMessage = yesVal
                        choosing = false
                        NextLine()
                    }
                    nobtn.setOnClickListener{
                        currMessage = noVal
                        choosing = false
                        NextLine()
                    }

                }
                else if (sender == 'G') {
                    otterImg.visibility = View.VISIBLE
                    tigerImg.visibility = View.GONE
                    tv.text = ""

                    gifImg.visibility = View.VISIBLE
                    var imgId = resources.getIdentifier(msg.substring(0, msg.length-1),"drawable",packageName)
                    gif_msg.setImageResource(imgId)

                }
                else if (sender == 'J') {
                    otterImg.visibility = View.GONE
                    tigerImg.visibility = View.GONE
                    tv.text = ""
                    currMessage = (((msg[0].toInt()-48) * 10) + (msg[1].toInt()-48)) - 1
                    NextLine()
                }
                else if(sender == '0'){
                    otterImg.visibility = View.VISIBLE
                    tigerImg.visibility = View.GONE
                    tv.animateText(msg)
                }
                else{
                    otterImg.visibility = View.GONE
                    tigerImg.visibility = View.VISIBLE
                    tv.animateText(msg)
                }
            }
        }
    }
}
