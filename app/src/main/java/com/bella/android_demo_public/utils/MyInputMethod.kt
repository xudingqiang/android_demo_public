package com.bella.android_demo_public.utils

import android.inputmethodservice.InputMethodService
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import com.bella.android_demo_public.R

class MyInputMethod :InputMethodService(),KeyboardView.OnKeyboardActionListener {
    private lateinit var keyboardView: KeyboardView
    private lateinit var keyboard: Keyboard
    private lateinit var candidateView: TextView
    private val wordSuggestions = mutableListOf<String>()

    override fun onCreateInputView(): View {
        keyboardView = layoutInflater.inflate(R.layout.input_keyboard, null) as KeyboardView
        keyboard = Keyboard(this, R.xml.qwerty_keyboard)
        keyboardView.keyboard = keyboard
        keyboardView.setOnKeyboardActionListener(this)
        return keyboardView
    }

    override fun onCreateCandidatesView(): View {
        candidateView = TextView(this)
        candidateView.textSize = 18f
        candidateView.setPadding(16, 8, 16, 8)
        return candidateView
    }

    override fun onPress(primaryCode: Int) {
        TODO("Not yet implemented")
    }

    override fun onRelease(primaryCode: Int) {
        TODO("Not yet implemented")
    }

    override fun onKey(primaryCode: Int, keyCodes: IntArray?) {
        val ic = currentInputConnection ?: return

        when (primaryCode) {
            Keyboard.KEYCODE_DELETE -> {
                ic.deleteSurroundingText(1, 0)
                updateSuggestions("") // 删除后更新建议
            }
            Keyboard.KEYCODE_DONE -> ic.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER))
            else -> {
                val char = primaryCode.toChar()
                ic.commitText(char.toString(), 1)
                updateSuggestions(char.toString()) // 输入后更新建议
            }
        }
    }

    private fun updateSuggestions(input: String) {
        wordSuggestions.clear()

        // 简单模拟词库建议
        if (input.isNotEmpty()) {
//            wordSuggestions.addAll(
//                listOf("$input测试", "$input演示", "$input示例", "$input输入法")
//            )
        }

        candidateView.text = wordSuggestions.joinToString("  ")
    }




    override fun onText(text: CharSequence?) {
        TODO("Not yet implemented")
    }

    override fun swipeLeft() {
        TODO("Not yet implemented")
    }

    override fun swipeRight() {
        TODO("Not yet implemented")
    }

    override fun swipeDown() {
        TODO("Not yet implemented")
    }

    override fun swipeUp() {
        TODO("Not yet implemented")
    }


}