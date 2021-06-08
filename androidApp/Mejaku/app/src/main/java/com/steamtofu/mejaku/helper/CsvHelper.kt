package com.steamtofu.mejaku.helper

import android.util.Log
import com.steamtofu.mejaku.entity.student.StudentScores
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

object CsvHelper {

    private var studentScores: StudentScores? = null

    fun read(inputStream: InputStream): ArrayList<StudentScores?>{
        val resultList = ArrayList<StudentScores?>()

        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        try {
            //step over header
            bufferedReader.readLine()
            var csvRow = bufferedReader.readLine()

            while ( csvRow != null){
                val row = csvRow.split(",")

                studentScores = StudentScores(
                    row[0],
                    row[1],
                    row[2],
                    row[3].toFloat(),
                    row[4].toFloat(),
                    row[5].toFloat(),
                    row[6].toFloat()
                )

                resultList.add(studentScores)
                csvRow = bufferedReader.readLine()
            }
        } catch (e: IOException){
            Log.e("CSV HELPER", "Error read data: $e")
        }
        finally {
            try {
                Log.d("CSV HELPER", "read: $resultList")
                inputStream.close()
            }catch (e: IOException){
                Log.e("CSV HELPER", "Error close input stream: $e")
            }
        }

        return resultList
    }

}