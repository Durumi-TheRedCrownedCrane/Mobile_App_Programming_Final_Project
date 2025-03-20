package edu.skku.cs.moappfinal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Toast
import edu.skku.cs.moappfinal.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.simpleframework.xml.core.Persister
import java.io.StringReader
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {
    private val API_KEY = ""
    private val GENRE = "CCCA"
    private lateinit var binding: ActivityMainBinding
    private val job = Job()
    override val coroutineContext: CoroutineContext get() = Dispatchers.Main + job // Job()으로 코루틴 관리

    private val currentAdapter = PerformanceAdapter { showDetail(it) }
    private val expectedAdapter = PerformanceAdapter { showDetail(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater) // 데이터 바인딩으로 activity_main에 접근
        setContentView(binding.root)

        binding.rvTopPerformances.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.rvTopPerformances.adapter = currentAdapter

        binding.rvRecentPerformances.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.rvRecentPerformances.adapter = expectedAdapter

        binding.btnSearchview.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }

        loadPerformances()

    }

    private fun loadPerformances() {

        val now = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val startOfMonth = now.withDayOfMonth(1).format(formatter)
        val endOfMonth = now.withDayOfMonth(now.lengthOfMonth()).format(formatter)
        Log.d("today", now.toString())
        Log.d("startOfMonth", startOfMonth)
        Log.d("endOfMonth", endOfMonth)

        launch(Dispatchers.IO) {
            try {
                val client = OkHttpClient()

                val boxStatsCateUrl = "https://kopis.or.kr/openApi/restful/pblprfr?service=$API_KEY&stdate=$startOfMonth&eddate=$endOfMonth&cpage=1&rows=10&shcate=$GENRE&prfstate=02"
                val boxStatsRequest = Request.Builder().url(boxStatsCateUrl).build()
                val boxStatsResponse = client.newCall(boxStatsRequest).execute()
                val boxStatsBody = boxStatsResponse.body?.string()

                val boxOfficeUrl = "https://kopis.or.kr/openApi/restful/pblprfr?service=$API_KEY&stdate=$startOfMonth&eddate=$endOfMonth&cpage=1&rows=10&shcate=$GENRE&prfstate=01"
                val boxOfficeRequest = Request.Builder().url(boxOfficeUrl).build()
                val boxOfficeResponse = client.newCall(boxOfficeRequest).execute()
                val boxOfficeBody = boxOfficeResponse.body?.string()

                if (boxStatsBody != null) {
                    Log.d("current performance", boxStatsBody)
                }
                val currentClassicPerformances: List<Performance>? = boxStatsBody?.let {
                    parsePerformances(
                        it
                    )
                }
                val sortedCrntPerformances = currentClassicPerformances?.sortedBy { it.endDate }

                val expectedClassicPerformances: List<Performance>? = boxOfficeBody?.let {
                    parsePerformances(
                        it
                    )
                }
                val sortedExptPerformances = expectedClassicPerformances?.sortedBy { it.endDate }

                withContext(Dispatchers.Main) {
                    currentAdapter.submitList(sortedCrntPerformances)
                    expectedAdapter.submitList(sortedExptPerformances)
                }

            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "데이터 로드 실패", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun parsePerformances(xml: String): List<Performance>? {
        val serializer = Persister()
        return try {
            val performances = serializer.read(Performances::class.java, StringReader(xml))
            performances.performanceList
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun showDetail(performance: Performance) {
        Log.d("performance id", performance.id)
        val intent = Intent(this, PerformanceDetailActivity::class.java).apply {
            putExtra("performanceId", performance.id)
        }
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}
