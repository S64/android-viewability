package jp.s64.android.viewablearea.example

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import jp.s64.android.viewability.core.dimension.DisplayDimension
import jp.s64.android.viewability.core.gaps.ContentGaps
import jp.s64.android.viewability.core.gaps.SystemGaps
import jp.s64.android.viewability.core.rect.ContentRect
import jp.s64.android.viewability.core.rect.RealViewRect
import jp.s64.android.viewability.core.rect.ViewRect
import jp.s64.android.viewability.core.rect.WindowRect
import jp.s64.android.viewability.core.viewability.ContentViewability
import jp.s64.android.viewability.core.viewability.WindowViewability
import jp.s64.android.viewablearea.*

class ObserverActivity : AppCompatActivity() {

    private val displaySize by lazy { findViewById<TextView>(R.id.displaySize) }
    private val windowSize by lazy { findViewById<TextView>(R.id.windowSize) }
    private val systemGaps by lazy { findViewById<TextView>(R.id.systemGaps) }
    private val contentGaps by lazy { findViewById<TextView>(R.id.contentGaps) }
    private val contentInDisplay by lazy { findViewById<TextView>(R.id.contentInDisplay) }
    private val contentInWindow by lazy { findViewById<TextView>(R.id.contentInWindow) }

    private val targetView by lazy { findViewById<View>(R.id.targetView) }

    private val targetInContent by lazy { findViewById<TextView>(R.id.targetInContent) }
    private val targetInWindow by lazy { findViewById<TextView>(R.id.targetInWindow) }
    private val targetInDisplay by lazy { findViewById<TextView>(R.id.targetInDisplay) }

    private val windowViewability by lazy { findViewById<TextView>(R.id.windowViewability) }
    private val contentViewability by lazy { findViewById<TextView>(R.id.contentViewability) }

    private val realViewRect by lazy { findViewById<TextView>(R.id.realViewRect) }

    private lateinit var appAreaObserver: AppAreaObserver
    private lateinit var viewAreaObserver: ViewAreaObserver

    private lateinit var appViewabilityObserver: AppViewabilityObserver

    private lateinit var viewabilityObserver: ViewabilityObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.observer_activity)

        appAreaObserver = AppAreaObserver(
            this,
            object : AppAreaObserver.IListener {

                override fun onDisplaySizeChanged(newValue: DisplayDimension?) {
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

                override fun onContentInDisplayChanged(contentInDisplay: ContentRect?) {
                    this@ObserverActivity.contentInDisplay.text = contentInDisplay.toString()
                }

                override fun onContentInWindowChanged(contentInWindow: ContentRect?) {
                    this@ObserverActivity.contentInWindow.text = contentInWindow.toString()
                }

            }
        )

        viewAreaObserver = ViewAreaObserver(
            targetView,
            object : ViewAreaObserver.IListener {

                override fun onViewRectInContentChanged(newValue: ViewRect?) {
                    this@ObserverActivity.targetInContent.text = newValue.toString()
                }

                override fun onViewRectInWindowChanged(newValue: ViewRect?) {
                    this@ObserverActivity.targetInWindow.text = newValue.toString()
                }

                override fun onViewRectInDisplayChanged(newValue: ViewRect) {
                    this@ObserverActivity.targetInDisplay.text = newValue.toString()
                }

            }
        )

        appViewabilityObserver = AppViewabilityObserver(
            this,
            object : AppViewabilityObserver.IListener {

                override fun onWindowViewabilityChanged(newValue: WindowViewability?) {
                    this@ObserverActivity.windowViewability.text = newValue.toString()
                }

                override fun onContentViewabilityChanged(newValue: ContentViewability?) {
                    this@ObserverActivity.contentViewability.text = newValue.toString()
                }

            }
        )

        viewabilityObserver = ViewabilityObserver(
            targetView,
            object : ViewabilityObserver.IListener {

                override fun onRealViewRectChanged(newValue: RealViewRect?) {
                    this@ObserverActivity.realViewRect.text = newValue.toString()
                }

            }
        )
    }

}
