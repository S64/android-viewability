package jp.s64.android.viewablearea.example

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import jp.s64.android.viewablearea.*

class MainActivity : AppCompatActivity() {

    private lateinit var appAreaDetector: AppAreaDetector
    private lateinit var viewPositionObserver: ViewPositionObserver

    private val displaySize by lazy { findViewById<TextView>(R.id.displaySize) }
    private val windowSize by lazy { findViewById<TextView>(R.id.windowSize) }
    private val contentSize by lazy { findViewById<TextView>(R.id.contentSize) }
    private val viewPosition by lazy { findViewById<TextView>(R.id.viewPosition) }

    private val targetView by lazy { findViewById<View>(R.id.targetView) }

    private val appAreaDetectorListener = object : AppAreaDetector.IListener {

        override fun onContentSizeChanged(
            lastContentSize: ContentSize?,
            newContentSize: ContentSize
        ) {
            contentSize.text = "${newContentSize?.widthInPixels}x${newContentSize?.heightInPixels}"
        }

        override fun onDisplaySizeChanged(
            oldDisplaySize: DisplaySize?,
            newDisplaySize: DisplaySize
        ) {
            displaySize.text = "${newDisplaySize?.widthInPixels}x${newDisplaySize?.heightInPixels}"
        }

        override fun onWindowSizeChanged(
            oldWindowSize: WindowSize?,
            newWindowSize: WindowSize
        ) {
            windowSize.text = "${newWindowSize.widthInPixels}x${newWindowSize.heightInPixels} (${newWindowSize.left}-${newWindowSize.top}-${newWindowSize.right}-${newWindowSize.bottom})"
        }

    }

    private val viewPositionObserverListener = object : ViewPositionObserver.IListener {

        override fun onViewRectChanged(oldViewRect: ViewRect?, newViewRect: ViewRect) {
            viewPosition.text = "${newViewRect.widthInPixels}x${newViewRect.heightInPixels} (${newViewRect.left}-${newViewRect.top}-${newViewRect.right}-${newViewRect.bottom})"
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        appAreaDetector = AppAreaDetector(
            this@MainActivity,
            appAreaDetectorListener
        )

        viewPositionObserver = ViewPositionObserver(
            targetView,
            viewPositionObserverListener
        )

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                )
    }

}
