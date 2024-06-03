package com.anshabunin.eventplanner.utils

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader

fun readJSONFromAssets(context: Context, path: String): String {
    return try {
        val file = context.assets.open("$path")
        val bufferedReader = BufferedReader(InputStreamReader(file))
        val stringBuilder = StringBuilder()
        bufferedReader.useLines { lines ->
            lines.forEach {
                stringBuilder.append(it)
            }
        }
        stringBuilder.toString()
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}

fun mapToList(jsonString: String): List<String> {
    val list = mutableListOf<String>()
    jsonString
        .split("{\"")[1]
        .split("}\"")[0]
        .split(": \"").mapIndexed { index, str ->
            if (index != 0) {
                str.split("\", ").mapIndexed { index, s ->
                    if (index == 0) list.add(s)
                }
            }
        }
    return list
}