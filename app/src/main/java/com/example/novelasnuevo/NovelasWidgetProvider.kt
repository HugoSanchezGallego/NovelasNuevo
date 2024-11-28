package com.example.novelasnuevo

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.RemoteViews
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class NovelasWidgetProvider : AppWidgetProvider() {
    private val handler = Handler(Looper.getMainLooper())
    private val updateInterval = 3000L // 3 seconds
    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val originalRequest = chain.request()
            val compressedRequest = originalRequest.newBuilder()
                .header("Content-Encoding", "gzip")
                .build()
            chain.proceed(compressedRequest)
        }
        .build()

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        FirebaseApp.initializeApp(context)
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
        scheduleNextUpdate(context)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == "com.example.novelasnuevo.UPDATE_WIDGET") {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(ComponentName(context, NovelasWidgetProvider::class.java))
            onUpdate(context, appWidgetManager, appWidgetIds)
        }
    }

    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val views = RemoteViews(context.packageName, R.layout.novelas_widget)
        val db = FirebaseFirestore.getInstance()
        val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("authenticated_user", null)

        if (username != null) {
            db.collection("novelas")
                .whereEqualTo("username", username)
                .get()
                .addOnSuccessListener { snapshot ->
                    val novelas = snapshot.toObjects(Novela::class.java)
                    val titles = novelas.joinToString("\n") { it.titulo }
                    views.setTextViewText(R.id.widgetTitle, "Novelas")
                    views.setTextViewText(R.id.widgetListView, titles)
                    appWidgetManager.updateAppWidget(appWidgetId, views)
                }
                .addOnFailureListener {
                    views.setTextViewText(R.id.widgetTitle, "Error")
                    views.setTextViewText(R.id.widgetListView, "Can't load widget")
                    appWidgetManager.updateAppWidget(appWidgetId, views)
                }
        } else {
            views.setTextViewText(R.id.widgetTitle, "No User")
            views.setTextViewText(R.id.widgetListView, "Please log in")
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    private fun scheduleNextUpdate(context: Context) {
        handler.postDelayed({
            val intent = Intent(context, NovelasWidgetProvider::class.java).apply {
                action = "com.example.novelasnuevo.UPDATE_WIDGET"
            }
            context.sendBroadcast(intent)
        }, updateInterval)
    }
}