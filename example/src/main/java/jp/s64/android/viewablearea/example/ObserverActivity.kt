package jp.s64.android.viewablearea.example

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import jp.s64.android.viewablearea.*

class ObserverActivity : AppCompatActivity() {

    private val displaySize by lazy { findViewById<TextView>(R.id.displaySize) }
    private val windowSize by lazy { findViewById<TextView>(R.id.windowSize) }
    private val systemGaps by lazy { findViewById<TextView>(R.id.systemGaps) }
    private val contentGaps by lazy { findViewById<TextView>(R.id.contentGaps) }
    private val contentInDisplay by lazy { findViewById<TextView>(R.id.contentInDisplay) }
    private val contentInWindow by lazy { findViewById<TextView>(R.id.contentInWindow) }

    private lateinit var appAreaObserver: AppAreaObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.observer_activity)

        appAreaObserver = AppAreaObserver(
            this,
            object : AppAreaObserver.IListener {

                override fun onDisplaySizeChanged(newValue: DisplaySize?) {
                    this@ObserverActivity.displaySize.text = newValue.toString()
                }

                override fun onWindowRectChanged(windowRect: WindowRect) {
                    this@ObserverActivity.windowSize.text = windowRect.toString()
                }

                override fun onSystemGapsChanged(systemGaps: SystemGaps?) {
                    this@ObserverActivity.systemGaps.text = systemGaps.toString()
                }

                override fun onContentGapsChanged(contentGaps: ContentGaps) {
                    this@ObserverActivity.contentGaps.text = contentGaps.toString()
                }

                override fun onContentInDisplayChanged(contentInDisplay: ContentSize?) {
                    this@ObserverActivity.contentInDisplay.text = contentInDisplay.toString()
                }

                override fun onContentInWindowChanged(contentInWindow: ContentSize?) {
                    this@ObserverActivity.contentInWindow.text = contentInWindow.toString()
                }

            }
        )
    }

}
