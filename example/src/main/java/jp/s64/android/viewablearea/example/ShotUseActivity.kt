package jp.s64.android.viewablearea.example

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.TextView
import jp.s64.android.viewablearea.AppAreaCalculator
import jp.s64.android.viewablearea.ViewAreaCalculator

class ShotUseActivity : AppCompatActivity() {

    private val display by lazy { findViewById<TextView>(R.id.display) }
    private val window by lazy { findViewById<TextView>(R.id.window) }
    private val systemGaps by lazy { findViewById<TextView>(R.id.systemGaps) }
    private val contentGaps by lazy { findViewById<TextView>(R.id.contentGaps) }
    private val contentInDisplay by lazy { findViewById<TextView>(R.id.contentInDisplay) }
    private val contentInWindow by lazy { findViewById<TextView>(R.id.contentInWindow) }

    private val targetInDisplay by lazy { findViewById<TextView>(R.id.targetInDisplay) }
    private val targetInWindow by lazy { findViewById<TextView>(R.id.targetInWindow) }
    private val targetInContent by lazy { findViewById<TextView>(R.id.targetInContent) }

    private val target by lazy { findViewById<TextView>(R.id.targetView) }
    private val exec by lazy { findViewById<Button>(R.id.exec) }

    private lateinit var appAreaCalculator: AppAreaCalculator
    private lateinit var viewAreaCalculator: ViewAreaCalculator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shot_use_activity)

        appAreaCalculator = AppAreaCalculator(this@ShotUseActivity)
        viewAreaCalculator =
            ViewAreaCalculator(target, appAreaCalculator)

        exec.setOnClickListener {
            display.text = appAreaCalculator.displaySize.toString()
            window.text = appAreaCalculator.windowRect.toString()
            systemGaps.text = appAreaCalculator.systemGaps.toString()
            contentGaps.text = appAreaCalculator.contentGaps.toString()

            contentInDisplay.text = appAreaCalculator.contentInDisplay.toString()
            contentInWindow.text = appAreaCalculator.contentInWindow.toString()

            targetInDisplay.text = viewAreaCalculator.viewRectInDisplay.toString()
            targetInWindow.text = viewAreaCalculator.viewRectInWindow.toString()
            targetInContent.text = viewAreaCalculator.viewRectInContent.toString()
        }

        getWindow().decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                )
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        exec.performClick()
    }

}
