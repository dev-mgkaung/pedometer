package mk.learner.pedometer

import android.content.Context
import android.os.Bundle
import androidx.annotation.NonNull
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import mk.learner.stepcounterdemo.StepSensorService
import mk.learner.stepcounterdemo.toast

class MainActivity: FlutterActivity() {

    private val CHANNEL = "mk.learner.pedometer/stepcounter"

    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {

        super.configureFlutterEngine(flutterEngine)

        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler {
            call, result ->
            // Note: this method is invoked on the main thread.
            if (call.method == "startService") {
                StepSensorService.startService(this)
                result.success("Start Service Running...")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        StepSensorService.stopService(this)
    }
}
private const val PREFS_NAME = "pedometer"
private const val PREF_PREFIX_KEY = "appwidget"

// Write the prefix to the SharedPreferences object for this widget
internal fun saveTitlePref(context: Context, appWidgetId: Int, text: String) {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
    prefs.putString(PREF_PREFIX_KEY , text)
    prefs.apply()
}

// Read the prefix from the SharedPreferences object for this widget.
// If there is no preference saved, get the default from a resource
internal fun loadTitlePref(context: Context, appWidgetId: Int): String {

    val prefs = context.getSharedPreferences(PREFS_NAME, 0)
    val titleValue = prefs.getString(PREF_PREFIX_KEY  , null)
    context.toast("load data $titleValue")
    return titleValue ?:"0"
}

internal fun deleteTitlePref(context: Context, appWidgetId: Int) {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
    prefs.remove(PREF_PREFIX_KEY + appWidgetId)
    prefs.apply()
}