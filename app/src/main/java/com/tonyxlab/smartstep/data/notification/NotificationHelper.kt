@file:RequiresApi(Build.VERSION_CODES.S)

package com.tonyxlab.smartstep.data.notification

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.graphics.toColorInt
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
    ): Notification {

        val collapsedView = createCollapsedRemoteViews(
                context = context,
                steps = steps,
                calories = calories,
                goal = goal
        )

        val expandedView = createExpandedRemoteViews(
                context = context,
                steps = steps,
                calories = calories,
                goal = goal
        )

        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_steps)
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
        goal: Int
    ) {
        val manager = context.getSystemService(NotificationManager::class.java)
        val notification = buildNotification(
                context = context,
                steps = steps,
                calories = calories,
                goal = goal
        )
        manager.notify(NOTIFICATION_ID, notification)
    }

    @SuppressLint("RemoteViewLayout")
    private fun createCollapsedRemoteViews(
        context: Context,
        steps: Int,
        calories: Int,
        goal: Int
    ): RemoteViews {

        val (textColor, iconTint, dividerColor) = resolveColors(context)


        return RemoteViews(context.packageName, R.layout.notification_small).apply {

            setTextViewText(R.id.txtSteps, formatNumber(steps))
            setTextViewText(R.id.txtCalories, calories.toString())
            setProgressBar(
                    R.id.progressSteps,
                    goal.coerceAtLeast(1),
                    steps.coerceIn(0, goal.coerceAtLeast(1)),
                    false
            )

            // Text Color
            setTextColor(R.id.txtSteps, textColor)
            setTextColor(R.id.txtCalories, textColor)

            // Icons Tint
            setColorInt(R.id.imgSneakers, "setColorFilter", iconTint, iconTint)
            setColorInt(R.id.imgWeight, "setColorFilter", iconTint, iconTint)

            // Divider Color
            setColorInt(R.id.imgDivider, "setColorFilter", dividerColor, dividerColor)

        }

    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun createExpandedRemoteViews(
        context: Context,
        steps: Int,
        calories: Int,
        goal: Int,
    ): RemoteViews {

        val (textColor, iconTint, dividerColor) = resolveColors(context)

        return RemoteViews(context.packageName, R.layout.notification_large).apply {

            setTextViewText(R.id.txtSteps, formatNumber(steps))
            setTextViewText(R.id.txtCalories, calories.toString())
            setProgressBar(
                    R.id.progressSteps,
                    goal.coerceAtLeast(1),
                    steps.coerceIn(0, goal.coerceAtLeast(1)),
                    false
            )

            // Text Color
            setTextColor(R.id.txtSteps, textColor)
            setTextColor(R.id.txtCalories, textColor)

            // Icons Tint
            setColorInt(R.id.imgSneakers, "setColorFilter", iconTint, iconTint)
            setColorInt(R.id.imgWeight, "setColorFilter", iconTint, iconTint)

            // Divider Color
            setColorInt(R.id.imgDivider, "setColorFilter", dividerColor, dividerColor)
        }
    }

    private fun resolveColors(context: Context): Triple<Int, Int, Int> {
        val isNightMode = (context.resources.configuration.uiMode
                and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

        val textColor = if (isNightMode) "#FFFFFF".toColorInt() else "#1F2024".toColorInt()
        val iconTint = if (isNightMode) "#8C9EFF".toColorInt() else "#3A43B6".toColorInt()
        val dividerColor = if (isNightMode) "#FFFFFF".toColorInt() else "#BFBFBF".toColorInt()

        return Triple(textColor, iconTint, dividerColor)
    }

    private fun formatNumber(value: Int): String {
        return NumberFormat.getNumberInstance(Locale.US)
                .format(value)
    }
}