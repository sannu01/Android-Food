package com.internshala.assignment.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.internshala.assignment.R
import com.internshala.assignment.util.GlobalVars
import org.json.JSONObject
import java.util.HashMap

class ForgotActivity : AppCompatActivity() {
    lateinit var toolbar: Toolbar
    lateinit var btnnext:Button
    lateinit var mobile:EditText
    lateinit var email:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot)

        toolbar=findViewById(R.id.toolbar)
        setupactiobar()
        btnnext=findViewById(R.id.btnnext)
        mobile=findViewById(R.id.enforgotmobile)
        email=findViewById(R.id.enforgotemail)

        btnnext.setOnClickListener {
            val phone=mobile.text.toString()
            val mail=email.text.toString()
            if(phone.length<10){
                mobile.setError("Invalid Mobile Number")
            }else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
                email.setError("Invalid Email")
            }
            else
            {
                val queue= Volley.newRequestQueue(this)
                val url="http://13.235.250.119/v2/forgot_password/fetch_result/"

                val jsonParams= JSONObject()
                jsonParams.put("mobile_number",phone)
                jsonParams.put("email",mail)

                if(GlobalVars.isNetworkConnected){
                    val jsnoRequest =
                        object: JsonObjectRequest(Method.POST,url,jsonParams, Response.Listener {

                            try {
                                val dataObject=it.getJSONObject("data")
                                var success=dataObject.getBoolean("success")
                                println(success)
                                if(success){


                                    val intent=Intent(this,ResetActivity::class.java)
                                    intent.putExtra("phone",phone)
                                    startActivity(intent)
                                    finish()


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
    fun setupactiobar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title="Reset Password"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed();
        return true;
    }
}
