package com.example.getlocation

import android.os.AsyncTask
import java.io.DataOutputStream
import java.net.Socket

class MessageSender:AsyncTask<String,Int,Boolean>(){


    override fun doInBackground(vararg p0: String?): Boolean? {
        val ip = p0[0]
        val port = p0[1]!!
        try {
            val s = Socket(ip, port.toInt())
            s.tcpNoDelay = true
            val dataOut = DataOutputStream(s.getOutputStream())
            dataOut.writeUTF(p0[2]!!)
            dataOut.flush()
            dataOut.close()
        }catch (e:Exception){
        }
        return true
    }

}
