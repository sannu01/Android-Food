package com.internshala.assignment.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.internshala.assignment.R
import com.internshala.assignment.util.GlobalVars
import org.json.JSONObject
import java.util.HashMap

class ResetActivity : AppCompatActivity() {
    var phone:String?=null
    lateinit var otp:EditText
    lateinit var password:EditText
    lateinit var cnfpass:EditText
    lateinit var btnsubmit:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset)

        if(intent!=null) {
            phone = intent.getStringExtra("phone")
        }
        else{
            onBackPressed()
        }

        otp=findViewById(R.id.enotp)
        password=findViewById(R.id.enresetpass)
        cnfpass=findViewById(R.id.enresetconfpass)
        btnsubmit=findViewById(R.id.btnreset)

        btnsubmit.setOnClickListener {
            val enotp=otp.text.toString()
            val pass=password.text.toString()
            val cnfpasswd=cnfpass.text.toString()

            if(enotp.length<4){
                otp.setError("Check Your OTP")
            }else if (pass.length<6){
                password.setError("Minimum Length 6")
            }else if(pass!=cnfpasswd){
                cnfpass.setError("Password Dosen't Match")
            }
            else{

                val queue= Volley.newRequestQueue(this)
                val url="http://13.235.250.119/v2/reset_password/fetch_result/"

                val jsonParams= JSONObject()
                jsonParams.put("mobile_number",phone)
                jsonParams.put("password",pass)
                jsonParams.put("otp",enotp)

                if(GlobalVars.isNetworkConnected){
                    val jsnoRequest =
                        object: JsonObjectRequest(Method.POST,url,jsonParams, Response.Listener {

                            try {
                                val dataObject=it.getJSONObject("data")
                                var success=dataObject.getBoolean("success")
                                println(success)
                                if(success){
                                    val message=dataObject.getString("successMessage")
                                    Toast.makeText(this,message,Toast.LENGTH_LONG).show()
                                    val login= Intent(this,LoginActivity::class.java)
                                    startActivity(login)
                                    finishAffinity()


                                }else{
                                    val message=dataObject.getString("errorMessage")
                                    Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
                                }

                            }catch (e:Exception){

                                Toast.makeText(this,"Some Error Occurred!!", Toast.LENGTH_SHORT).show()

                            }

                        }, Response.ErrorListener {

                            Toast.makeText(this,"Some Error Occurred!!", Toast.LENGTH_SHORT).show()


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
                    Toast.makeText(this,"Couldn't Connect to Internet", Toast.LENGTH_SHORT).show()
                }


            }
        }


    }
}
