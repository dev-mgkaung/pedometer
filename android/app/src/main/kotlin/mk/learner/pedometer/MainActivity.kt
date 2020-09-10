package mk.learner.pedometer

import android.os.Bundle
import androidx.annotation.NonNull
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import mk.learner.stepcounterdemo.StepSensorService

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
