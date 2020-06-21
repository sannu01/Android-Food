package com.internshala.assignment.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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

class SignupActivity : AppCompatActivity() {
    lateinit var toolbar:Toolbar
    lateinit var name:EditText
    lateinit var email:EditText
    lateinit var phone:EditText
    lateinit var address:EditText
    lateinit var pass:EditText
    lateinit var cnfpass:EditText
    lateinit var btnsignup:Button
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        title="Sign Up"
        toolbar=findViewById(R.id.toolbar)
        setupactiobar()
        name=findViewById(R.id.enname)
        email=findViewById(R.id.enmail)
        phone=findViewById(R.id.enmobile)
        address=findViewById(R.id.enaddress)
        pass=findViewById(R.id.enpass)
        cnfpass=findViewById(R.id.encnfpass)
        btnsignup=findViewById(R.id.btnsignup)
        sharedPreferences=getSharedPreferences(getString(R.string.shared_preference), Context.MODE_PRIVATE)

        btnsignup.setOnClickListener {
            val enname=name.text.toString()
            val enmail=email.text.toString()
            val enphone=phone.text.toString()
            val enaddress=address.text.toString()
            val enpass=pass.text.toString()
            val encnfpass=cnfpass.text.toString()
            if (enname.length<3){
                name.setError("Minimuch Required length is 3")
            }else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(enmail).matches()){
                email.setError("Invalid Email")
            }else if (enphone.length<10){
                phone.setError("Invalid Mobile Number")
            }else if(enaddress.length<3){
                address.setError("Enter valid Address")
            }else if(enpass.length<6){
                pass.setError("Minimum length should be 6")
            }else if(encnfpass!=enpass){
                cnfpass.setError("Password Dosen't Match")
            }else{

                val queue= Volley.newRequestQueue(this)
                val url="http://13.235.250.119/v2/register/fetch_result/"

                val jsonParams= JSONObject()
                jsonParams.put("name",enname)
                jsonParams.put("email",enmail)
                jsonParams.put("mobile_number",enphone)
                jsonParams.put("address",enaddress)
                jsonParams.put("password",enpass)


                if(GlobalVars.isNetworkConnected){
                    val jsonRequest =
                        object: JsonObjectRequest(Method.POST,url,jsonParams, Response.Listener {

                            try {
                                val dataObject=it.getJSONObject("data")
                                var success=dataObject.getBoolean("success")
                                println(success)
                                if(success){
                                    val infoobject=dataObject.getJSONObject("data")
                                    val id=infoobject.getString("user_id")
                                    savepreferences(id,enname,enphone,enmail,enaddress)
                                    val login= Intent(this,MainActivity::class.java)
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
                    queue.add(jsonRequest)
                }else{
                    Toast.makeText(this,"Couldn't Connect to Internet", Toast.LENGTH_SHORT).show()
                }


            }

        }



    }
    fun setupactiobar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title="Sign Up"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed();
        return true;
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
