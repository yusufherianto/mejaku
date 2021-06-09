package com.steamtofu.mejaku.helper

import android.content.Context
import com.steamtofu.mejaku.entity.student.StudentScores
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

object MlHelper {

    fun loadModeFile(context: Context): MappedByteBuffer {
        val fileDescriptor = context.assets.openFd("mejaku_model_v2.tflite")
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

     fun doInference(tflite: Interpreter,studentScores: StudentScores): Float {
        val inputVal = floatArrayOf(
            studentScores.quiz,
            studentScores.assignment1,
            studentScores.assignment2,
            studentScores.assignment3
        )
        val outputVal = Array(1) {
            FloatArray(
                1
            )
        }
        tflite.run(inputVal, outputVal)

        return outputVal[0][0]
    }

    fun doInferenceByYourself(tflite: Interpreter, floatArray: FloatArray): Float {

        val outputVal = Array(1) {
            FloatArray(
                1
            )
        }
        tflite.run(floatArray, outputVal)

        return outputVal[0][0]
    }
}