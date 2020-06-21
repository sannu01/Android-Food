package com.internshala.assignment.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.internshala.assignment.R
import com.internshala.assignment.util.CheckNetwork
import com.internshala.assignment.util.GlobalVars
import org.json.JSONObject
import java.util.*

class LoginActivity : AppCompatActivity() {
    lateinit var signup:TextView
    lateinit var forgot:TextView
    lateinit var mobileNumber:EditText
    lateinit var password:EditText
    lateinit var btnLogin:Button
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        sharedPreferences=getSharedPreferences(getString(R.string.shared_preference),Context.MODE_PRIVATE)
        val isLoggedIn=sharedPreferences.getBoolean("isLoggedIn",false)

        if(isLoggedIn){
            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }


        signup=findViewById(R.id.txtsignup)
        forgot=findViewById(R.id.txtforgotpass)
        btnLogin=findViewById(R.id.btnlogin)
        mobileNumber=findViewById(R.id.enmobile)
        password=findViewById(R.id.enpass)
        val network = CheckNetwork()
        network.registerDefaultNetworkCallback(this as Context)

        signup.setOnClickListener {
            val register=Intent(this@LoginActivity,
                SignupActivity::class.java)
            startActivity(register)

        }
        forgot.setOnClickListener {
            val forgotpass=Intent(this@LoginActivity, ForgotActivity::class.java)
            startActivity(forgotpass)

        }
        btnLogin.setOnClickListener {
            val mobile=mobileNumber.text.toString()
            val pass=password.text.toString()
            if (mobile.length<10 || pass.length<4){
                Toast.makeText(this,"Invalid Phone or Password",Toast.LENGTH_SHORT).show()
            }else{
                val queue=Volley.newRequestQueue(this)
                val url="http://13.235.250.119/v2/login/fetch_result/"

                val jsonParams=JSONObject()
                jsonParams.put("mobile_number",mobile)
                jsonParams.put("password",pass)
                println(jsonParams)
                if(GlobalVars.isNetworkConnected){
                    val jsnoRequest =
                        object: JsonObjectRequest(Method.POST,url,jsonParams, Response.Listener {

                        try {
                            val dataObject=it.getJSONObject("data")
                            var success=dataObject.getBoolean("success")
                            println(success)
                            if(success){

                                val infoObject=dataObject.getJSONObject("data")
                                val name=infoObject.getString("name")
                                val phone=infoObject.getString("mobile_number")
                                val email= infoObject.getString("email")
                                val address=infoObject.getString("address")
                                val id=infoObject.getString("user_id")
                                savepreferences(id,name,phone,email,address)

                                val intent=Intent(this,MainActivity::class.java)
                                startActivity(intent)
                                finish()


                            }else{
                                var message=dataObject.getString("errorMessage")
                                Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
                            }

                        }catch (e:Exception){
                            Toast.makeText(this,"Some Error Occurred!!",Toast.LENGTH_SHORT).show()

                        }

                    }, Response.ErrorListener {

                        Toast.makeText(this,"Some Error Occurred!!",Toast.LENGTH_SHORT).show()


                    }){
                        override fun getHeaders(): MutableMap<String, String> {
                            val headers = HashMap<String, String>()
                            headers["context type"] = "application/json"
                            headers["token"] = "72c79d10f45224"
                            return headers
                        }
                    }
                    queue.add(jsnoRequest)
                }else{
                    Toast.makeText(this,"Couldn't Connect to Internet",Toast.LENGTH_SHORT).show()
                    val dialog = AlertDialog.Builder(this)
                    dialog.setTitle("Error")
                    dialog.setMessage("Internet connection is not Found")
                    dialog.setPositiveButton("Open Settings"){ _, _ ->
                        val settingsIntent= Intent(Settings.ACTION_WIRELESS_SETTINGS)
                        startActivity(settingsIntent)
                        finish()
                    }
                    dialog.setNegativeButton("Exit"){ _, _ ->
                        finishAffinity()
                    }
                    dialog.create()
                    dialog.show()
                }

            }

        }

    }



    fun savepreferences(id:String,name:String,phone:String,email:String,address:String)
    {
        sharedPreferences.edit().putBoolean("isLoggedIn",true).apply()
        sharedPreferences.edit().putString("id",id).apply()
        sharedPreferences.edit().putString("name",name).apply()
        sharedPreferences.edit().putString("phone",phone).apply()
        sharedPreferences.edit().putString("email",email).apply()
        sharedPreferences.edit().putString("address",address).apply()


    }



}
