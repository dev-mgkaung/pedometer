package mk.learner.stepcounterdemo

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import mk.learner.pedometer.MainActivity
import mk.learner.pedometer.PedoMeterWidget
import mk.learner.pedometer.R


class StepSensorService : Service(), SensorEventListener {

    private val CHANNEL_ID = "Step Counter Notification"
    private lateinit var sensorManager: SensorManager

    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        // Send a notification that service is started
        toast("Service started.")

        //init sensor Manager
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        reRegisterSensor()

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        toast("Service destroyed.")
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {

        val stepCount = event?.values?.get(0)?.toInt()
        println("stepCount= ${stepCount}")
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Foreground Service")
            .setContentText("Total Step Counts $stepCount steps")
            .setSmallIcon(R.drawable.icon)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1, notification)
        createNotificationChannel() // update notification
        updateStepWidget(stepCount.toString())
    }
    
    private fun updateStepWidget(stepCount: String)
    {
        val views = RemoteViews(this.packageName, R.layout.pedo_meter_widget)

        views.setTextViewText(R.id.appwidget_text, "${stepCount} steps")
        // Instruct the widget manager to update the widget
        val theWidget = ComponentName(this, PedoMeterWidget::class.java)
        val appWidgetManager = AppWidgetManager.getInstance(this)
        appWidgetManager.updateAppWidget(theWidget, views)
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID, "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(serviceChannel)
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun reRegisterSensor() {
        val stepsSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (stepsSensor == null) {
            toast("No Step Counter Sensor !")
        } else {
            sensorManager.registerListener(this, stepsSensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    companion object {

        fun startService(context: Context) {
            val startIntent = Intent(context, StepSensorService::class.java)
            ContextCompat.startForegroundService(context, startIntent)
        }

        fun stopService(context: Context) {
            val stopIntent = Intent(context, StepSensorService::class.java)
            context.stopService(stopIntent)
        }
    }
}