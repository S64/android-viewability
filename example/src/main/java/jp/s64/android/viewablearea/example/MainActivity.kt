package jp.s64.android.viewablearea.example

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import jp.s64.android.viewablearea.*

class MainActivity : AppCompatActivity() {

    private lateinit var appAreaObserver: AppAreaObserver
    private lateinit var viewPositionObserver: ViewPositionObserver
    private lateinit var appViewabilityObserver: AppViewabilityObserver
    private lateinit var viewabilityObserver: ViewabilityObserver

    private val displaySize by lazy { findViewById<TextView>(R.id.displaySize) }
    private val windowSize by lazy { findViewById<TextView>(R.id.windowSize) }
    private val contentSize by lazy { findViewById<TextView>(R.id.contentSize) }
    private val viewPosition by lazy { findViewById<TextView>(R.id.viewPosition) }
    private val realContentPosition by lazy { findViewById<TextView>(R.id.realContentPosition) }
    private val realViewPosition by lazy { findViewById<TextView>(R.id.realViewPosition) }

    private val targetView by lazy { findViewById<View>(R.id.targetView) }

    private val appAreaDetectorListener = object : AppAreaObserver.IListener {

        override fun onContentSizeChanged(
            oldContentSize: ContentSize?,
            newContentSize: ContentSize
        ) {
            contentSize.text = newContentSize.toString()
        }

        override fun onDisplaySizeChanged(
            oldDisplaySize: DisplaySize?,
            newDisplaySize: DisplaySize
        ) {
            displaySize.text = "${newDisplaySize?.widthInPixels}x${newDisplaySize?.heightInPixels}"
        }

        override fun onWindowRectChanged(
            oldWindowRect: WindowRect?,
            newWindowRect: WindowRect
        ) {
            windowSize.text = newWindowRect.toString()
        }

    }

    private val viewPositionObserverListener = object : ViewPositionObserver.IListener {

        override fun onViewRectChanged(oldViewRect: ViewRect?, newViewRect: ViewRect) {
            viewPosition.text = newViewRect.toString()
        }

    }

    private val appViewabilityListener = object : AppViewabilityObserver.IListener {

        override fun onAppViewabilityChanged(
            oldViewability: AppViewability?,
            newViewability: AppViewability
        ) {
            realContentPosition.text = newViewability.toString()
        }

    }

    private val viewabilityListener = object : ViewabilityObserver.IListener {

        override fun onViewabilityChanged(
            oldViewability: Viewability?,
            newViewability: Viewability
        ) {
            realViewPosition.text = newViewability.toString()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        appAreaObserver = AppAreaObserver(
            this@MainActivity,
            appAreaDetectorListener
        )

        viewPositionObserver = ViewPositionObserver(
            targetView,
            viewPositionObserverListener
        )

        appViewabilityObserver = AppViewabilityObserver(
            this@MainActivity,
            appViewabilityListener
        )

        viewabilityObserver = ViewabilityObserver(
            targetView,
            viewabilityListener
        )

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                )
    }

}
