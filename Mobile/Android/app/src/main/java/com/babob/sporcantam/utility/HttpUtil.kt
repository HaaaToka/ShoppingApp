package com.babob.sporcantam.utility

import android.content.Context
import android.os.SystemClock
import android.util.Log
import org.json.JSONObject
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import java.io.BufferedReader


class HttpUtil {
    companion object {

        /**
         * Call it from AsyncTask or else the app will EXPLODE!
         */
        fun sendPost(urlParam: String, Url: String, sId:String): Boolean {
            Log.d("HTTP_UTIL", "Sending Post Request to: $Url")
            try {
                val url = URL(Url)
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                //conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8")
                conn.setRequestProperty("Accept", "application/json")
                conn.setRequestProperty("Cookie", "JSESSIONID=$sId")


                val postData = urlParam.byteInputStream(StandardCharsets.UTF_8 )
                val postDataLength = postData.readBytes().size

                conn.setRequestProperty("Content-Length", Integer.toString(postDataLength))
                conn.useCaches = false
                conn.doOutput = true
                conn.doInput = true
                conn.connectTimeout = 4000

                val os = DataOutputStream(conn.outputStream)
                //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                Log.i("URLPARAM", "Sending UrlParam (POST)")
                Log.i("URLPARAM", urlParam)
                os.writeBytes(urlParam)
                os.flush()
                os.close()
                Log.i("STATUS", conn.responseCode.toString())
                Log.i("MSG", conn.responseMessage.toString())

                conn.disconnect()

            } catch (e: Exception) {
                Log.i("SENDERR", e.toString())
                return false
            }

            return true
        }

        fun sendPoststr(urlParam: String, Url: String, sId:String): String {
            Log.d("HTTP_UTIL", "Sending Post Request to: $Url")
            var returnValue = ""
            try {
                val url = URL(Url)
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                //conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8")
                conn.setRequestProperty("Accept", "application/json")
                conn.setRequestProperty("Cookie", "JSESSIONID=$sId")


                val postData = urlParam.byteInputStream(StandardCharsets.UTF_8 )
                val postDataLength = postData.readBytes().size

                conn.setRequestProperty("Content-Length", Integer.toString(postDataLength))
                conn.useCaches = false
                conn.doOutput = true
                conn.doInput = true
                conn.connectTimeout = 4000

                val os = DataOutputStream(conn.outputStream)
                //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                Log.i("URLPARAM", "Sending UrlParam (POST)")
                Log.i("URLPARAM", urlParam)
                os.writeBytes(urlParam)
                os.flush()
                os.close()
                Log.i("STATUS", conn.responseCode.toString())
                Log.i("MSG", conn.responseMessage.toString())


                val br = BufferedReader(InputStreamReader(conn.getInputStream()))
                val sb = StringBuilder()
                var line: String?
                line = br.readLine()
                while (line  != null) {
                    sb.append(line + "\n")
                    line = br.readLine()
                }
                returnValue = sb.toString()
                conn.disconnect()

            } catch (e: Exception) {
                Log.i("SENDERR", e.toString())
                return ""
            }

            return returnValue
        }
    }
}