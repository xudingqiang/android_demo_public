package com.bella.android_demo_public

import android.inputmethodservice.InputMethodService
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputConnection
import android.widget.Button
import com.bella.android_demo_public.utils.LogTool


class MyInputMethodService : InputMethodService() {
    private lateinit var keyboard: Keyboard
    private lateinit var keyboardView: KeyboardView


    override fun onCreateInputView(): View {
        keyboardView = LayoutInflater.from(this).inflate(R.layout.keyboard_view, null) as KeyboardView

        val inputConnection: InputConnection? = currentInputConnection

        keyboard = Keyboard(this, R.xml.keyboard_layout)
        keyboardView.keyboard = keyboard
        keyboardView.setOnKeyboardActionListener(keyboardActionListener)


        val buttonA: Button? = keyboardView.findViewById(R.id.button_a)
        val buttonB: Button? = keyboardView.findViewById(R.id.button_b)
        val buttonSpace: Button? = keyboardView.findViewById(R.id.button_space)
        val buttonDel: Button? = keyboardView.findViewById(R.id.button_del)

        buttonA?.setOnClickListener {
            inputConnection?.commitText("A", 1)
        }

        buttonB?.setOnClickListener {
            inputConnection?.commitText("B", 1)
        }

        buttonSpace?.setOnClickListener {
            inputConnection?.commitText(" ", 1)
        }

        buttonDel?.setOnClickListener {
            inputConnection?.deleteSurroundingText(1, 0)
        }

        return keyboardView
    }


    private val keyboardActionListener = object : KeyboardView.OnKeyboardActionListener {
        override fun onPress(primaryCode: Int) {
            TODO("Not yet implemented")
        }

        override fun onRelease(primaryCode: Int) {
            TODO("Not yet implemented")
        }

        override fun onKey(primaryCode: Int, keyCodes: IntArray?) {
            LogTool.w("onKey primaryCode "+primaryCode + ",keyCodes " )
            currentInputConnection?.let { ic ->
                LogTool.w("onKey primaryCode "+primaryCode + ",keyCodes " )
                ic.commitText("bella", 1)
//                when (primaryCode) {
//
//                }
            }
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

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        LogTool.w("onKeyDown keyCode "+keyCode + ",keyCodes " )
        currentInputConnection?.let { ic ->
            ic.commitText("bella", 1)
//                when (primaryCode) {
//
//                }
        }
        return super.onKeyDown(keyCode, event)
    }

}