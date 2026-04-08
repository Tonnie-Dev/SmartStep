/*

@file:RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.smartstep.data.notification

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.tonyxlab.smartstep.R
import com.tonyxlab.smartstep.data.notification.StepNotificationHelper.createCollapsedRemoteViews
import com.tonyxlab.smartstep.data.notification.StepNotificationHelper.createExpandedRemoteViews
import com.tonyxlab.smartstep.presentation.MainActivity

object StepNotificationHelper {

    const val CHANNEL_ID = "step_tracking_channel"


    fun createChannel(context: Context) {
        val channel = NotificationChannel(
                CHANNEL_ID,
                "Step Tracking",
                NotificationManager.IMPORTANCE_LOW // 🔥 IMPORTANT: silent
        ).apply {
            description = "Shows step tracking progress"
            setSound(null, null)
            enableVibration(false)
        }

        val manager = context.getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun buildNotification(
        context: Context,
        steps: Int,
        calories: Int,
        progress: Int,
        goal: Int
    ): Notification {

        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_pin)
                .setContentTitle("Smart Step Counter")
                .setContentText("$steps steps • $calories kcal")
                .setContentIntent(pendingIntent)
                .setOnlyAlertOnce(true) // 🔥 prevents re-alerting
                .setOngoing(false) // allows swipe dismiss
                .setSilent(true)
                .setProgress(goal, progress, false)
                .build()
    }


    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun updateNotification(
        context: Context,
        steps: Int,
        calories: Int,
        goal: Int
    ) {
        val manager = NotificationManagerCompat.from(context)

        val notification = buildNotification(
                context,
                steps,
                calories,
                progress = steps,
                goal = goal
        )

        manager.notify(1, notification)
    }
}

*/


@file:RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.smartstep.data.notification

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.tonyxlab.smartstep.R
import com.tonyxlab.smartstep.presentation.MainActivity
import java.text.NumberFormat
import java.util.Locale

object StepNotificationHelper {

    const val CHANNEL_ID = "step_tracking_channel"
    const val NOTIFICATION_ID = 1

    fun createChannel(context: Context) {
        val channel = NotificationChannel(
                CHANNEL_ID,
                "Step Tracking",
                NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Shows step tracking progress"
            setSound(null, null)
            enableVibration(false)
        }

        val manager = context.getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    fun buildNotification(
        context: Context,
        steps: Int,
        calories: Int,
        goal: Int,
        timeLabel: String = "now"
    ): Notification {

        val collapsedView = createCollapsedRemoteViews(
                context = context,
                steps = steps,
                calories = calories,
                goal = goal,
                timeLabel = timeLabel
        )

        val expandedView = createExpandedRemoteViews(
                context = context,
                steps = steps,
                calories = calories,
                goal = goal,
                timeLabel = timeLabel
        )

        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_pin)
                .setContentIntent(pendingIntent)
                .setOnlyAlertOnce(true)
                .setSilent(true)
                .setOngoing(false)
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(collapsedView)
                .setCustomBigContentView(expandedView)
                .build()
    }

    fun updateNotification(
        context: Context,
        steps: Int,
        calories: Int,
        goal: Int,
        timeLabel: String = "now"
    ) {
        val manager = context.getSystemService(NotificationManager::class.java)
        val notification = buildNotification(
                context = context,
                steps = steps,
                calories = calories,
                goal = goal,
                timeLabel = timeLabel
        )
        manager.notify(NOTIFICATION_ID, notification)
    }

    @SuppressLint("RemoteViewLayout")
    private fun createCollapsedRemoteViews(
        context: Context,
        steps: Int,
        calories: Int,
        goal: Int,
        timeLabel: String
    ): RemoteViews {
        return RemoteViews(context.packageName, R.layout.notification_small).apply {
            setTextViewText(R.id.txtTitle, "Smart Step Counter")
            setTextViewText(R.id.txtTime, timeLabel)
            setTextViewText(R.id.txtSteps, formatNumber(steps))
            setTextViewText(R.id.txtCalories, calories.toString())
            setProgressBar(
                    R.id.progressSteps,
                    goal.coerceAtLeast(1),
                    steps.coerceIn(0, goal.coerceAtLeast(1)),
                    false
            )
        }
    }

    private fun createExpandedRemoteViews(
        context: Context,
        steps: Int,
        calories: Int,
        goal: Int,
        timeLabel: String
    ): RemoteViews {
        return RemoteViews(context.packageName, R.layout.notification_large).apply {
            setTextViewText(R.id.txtTitle, "Smart Step Counter")
            setTextViewText(R.id.txtTime, timeLabel)
            setTextViewText(R.id.txtSteps, formatNumber(steps))
            setTextViewText(R.id.txtCalories, calories.toString())
            setProgressBar(
                    R.id.progressSteps,
                    goal.coerceAtLeast(1),
                    steps.coerceIn(0, goal.coerceAtLeast(1)),
                    false
            )
        }
    }

    private fun formatNumber(value: Int): String {
        return NumberFormat.getNumberInstance(Locale.US).format(value)
    }
}