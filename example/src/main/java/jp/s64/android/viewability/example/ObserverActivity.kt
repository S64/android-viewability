package jp.s64.android.viewability.example

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import jp.s64.android.viewability.apparea.AppAreaObserver
import jp.s64.android.viewability.apparea.AppViewabilityObserver
import jp.s64.android.viewability.viewarea.ViewAreaObserver
import jp.s64.android.viewability.viewarea.ViewabilityObserver
import jp.s64.android.viewability.core.dimension.DisplayDimension
import jp.s64.android.viewability.core.gaps.ContentGaps
import jp.s64.android.viewability.core.gaps.SystemGaps
import jp.s64.android.viewability.core.rect.*
import jp.s64.android.viewability.core.viewability.ContentViewability
import jp.s64.android.viewability.core.viewability.Viewability
import jp.s64.android.viewability.core.viewability.WindowViewability

class ObserverActivity : AppCompatActivity() {

    private val displayDimension by lazy { findViewById<TextView>(R.id.displayDimension) }
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
    private val isPaused by lazy { findViewById<TextView>(R.id.isPaused) }

    private val realWidgetRect by lazy { findViewById<TextView>(R.id.realWidgetRect) }
    private val viewability by lazy { findViewById<TextView>(R.id.viewability) }

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
                    this@ObserverActivity.displayDimension.text = newValue.toString()
                }

                override fun onWindowRectChanged(newValue: WindowRect) {
                    this@ObserverActivity.windowSize.text = newValue.toString()
                }

                override fun onSystemGapsChanged(newValue: SystemGaps?) {
                    this@ObserverActivity.systemGaps.text = newValue.toString()
                }

                override fun onContentGapsChanged(newValue: ContentGaps) {
                    this@ObserverActivity.contentGaps.text = newValue.toString()
                }

                override fun onContentInDisplayChanged(newValue: ContentRect?) {
                    this@ObserverActivity.contentInDisplay.text = newValue.toString()
                }

                override fun onContentInWindowChanged(newValue: ContentRect?) {
                    this@ObserverActivity.contentInWindow.text = newValue.toString()
                }

            }
        )

        viewAreaObserver = ViewAreaObserver(
            targetView,
            object : ViewAreaObserver.IListener {

                override fun onViewRectInContentChanged(newValue: WidgetRectInContent?) {
                    this@ObserverActivity.targetInContent.text = newValue.toString()
                }

                override fun onViewRectInWindowChanged(newValue: WidgetRectInWindow?) {
                    this@ObserverActivity.targetInWindow.text = newValue.toString()
                }

                override fun onViewRectInDisplayChanged(newValue: WidgetRectInDisplay) {
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

                override fun onIsPausedChanged(newValue: Boolean) {
                    this@ObserverActivity.isPaused.text = newValue.toString();
                }

            }
        )

        viewabilityObserver = ViewabilityObserver(
            targetView,
            object : ViewabilityObserver.IListener {

                override fun onViewabilityChanged(newValue: Viewability?) {
                    this@ObserverActivity.viewability.text = newValue.toString()
                }

                override fun onRealViewRectChanged(newValue: RealWidgetRect?) {
                    this@ObserverActivity.realWidgetRect.text = newValue.toString()
                }

            }
        )
    }

}
