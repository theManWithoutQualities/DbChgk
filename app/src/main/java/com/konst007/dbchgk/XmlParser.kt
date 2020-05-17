package com.konst007.dbchgk

import android.util.Xml
import com.konst007.dbchgk.model.Question
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream
import java.lang.IllegalStateException

class XmlParser {

    fun parse(inputStream: InputStream): List<Question  > {
        inputStream.use { inputStream ->
            val parser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream, null)
            parser.nextTag()
            return readFeed(parser)
        }
    }

    private fun readFeed(parser: XmlPullParser): List<Question> {
        val questions = mutableListOf<Question>()
        parser.require(XmlPullParser.START_TAG, null, "search")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            if (parser.name == "question") {
                questions.add(readQuestion(parser))
            } else {
                skip(parser)
            }
        }
        return questions
    }

    private fun readQuestion(parser: XmlPullParser): Question {
        parser.require(XmlPullParser.START_TAG, null, "question")
        var text = ""
        var answer = ""
        var comment = ""
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            when (parser.name) {
                "Question" -> text = readSimpleTag(parser, "Question")
                "Answer" -> answer = readSimpleTag(parser, "Answer")
                "Comments" -> comment = readSimpleTag(parser, "Comments")
                else -> skip(parser)
            }
        }
        return Question(text, answer, comment)
    }

    private fun readSimpleTag(parser: XmlPullParser, name: String): String {
        parser.require(XmlPullParser.START_TAG, null, name)
        val text = readText(parser)
        parser.require(XmlPullParser.END_TAG, null, name)
        return text
    }

    private fun readText(parser: XmlPullParser): String {
        var result = ""
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.text
            parser.nextTag()
        }
        return result
    }

    private fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.START_TAG -> depth++
                XmlPullParser.END_TAG -> depth--
            }
        }
    }
}