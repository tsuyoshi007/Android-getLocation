package com.example.getlocation

import android.content.Context
import android.os.AsyncTask
import android.widget.Toast
import java.io.DataOutputStream
import java.lang.ref.WeakReference
import java.net.Socket

class MessageSender(context: Context):AsyncTask<String,Int,Boolean>(){
    private val mContext: WeakReference<Context>  = WeakReference(context)


    override fun doInBackground(vararg p0: String?): Boolean? {
        val ip = p0[0]
        try {
            val s = Socket(ip,6666)
            val dataOut = DataOutputStream(s.getOutputStream())
            dataOut.writeUTF(p0[1]!!)
            dataOut.flush()
            dataOut.close()
        }catch (e:Exception){
            Toast.makeText(mContext.get(),"Error", Toast.LENGTH_SHORT).show()
        }
        return true
    }

}