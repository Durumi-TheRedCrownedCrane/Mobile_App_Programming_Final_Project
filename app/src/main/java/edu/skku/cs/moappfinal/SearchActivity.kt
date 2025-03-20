package edu.skku.cs.moappfinal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import androidx.recyclerview.widget.RecyclerView
import edu.skku.cs.moappfinal.databinding.ActivitySearchBinding
import org.simpleframework.xml.core.Persister
import java.io.StringReader
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import kotlin.coroutines.CoroutineContext

class SearchActivity : AppCompatActivity(), CoroutineScope {

    private val API_KEY = ""
    private val GENRE = "CCCA"

    private lateinit var binding: ActivitySearchBinding
    private val job = Job()
    override val coroutineContext: CoroutineContext get() = Dispatchers.Main + job

    private val searchAdapter = PerformanceAdapter { showDetail(it) }

    private var pfmName = ""
    private var startDate = ""
    private var endDate = ""
    private var region = ""
    private var cnt = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvSearchResults.layoutManager = GridLayoutManager(this, 3)
        binding.rvSearchResults.adapter = searchAdapter

        binding.btnHomeview.setOnClickListener {
            finish()
        }

        binding.btnSearchview.setOnClickListener {
            binding.etName.setText("")
            binding.etDate.setText("")
            pfmName = ""
            startDate = ""
            endDate = ""
            region = ""
            cnt = 0
            recreate()
        }

        binding.nameConfirm.setOnClickListener{
            pfmName = binding.etName.text.toString()
            Toast.makeText(this@SearchActivity, "제목이 입력되었습니다.", Toast.LENGTH_SHORT).show()
        }

        binding.dateConfirm.setOnClickListener{
            val inputDate = binding.etDate.text.toString()

            if (inputDate.length == 8) {
                if(isValidDate(inputDate)) {
                    Toast.makeText(this@SearchActivity, "날짜가 입력되었습니다.", Toast.LENGTH_SHORT).show()
                    startDate = inputDate
                    endDate = calculate30Days(startDate).toString()

                } else {
                    Toast.makeText(this@SearchActivity, "날짜를 다시 입력해주세요.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.code11.setOnClickListener {
            if (cnt == 0) {
                region = "11"
                cnt = 1
                Toast.makeText(this@SearchActivity, "서울특별시를 선택하셨습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.code26.setOnClickListener {
            if (cnt == 0) {
                region = "26"
                cnt = 1
                Toast.makeText(this@SearchActivity, "부산광역시를 선택하셨습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.code27.setOnClickListener {
            if (cnt == 0) {
                region = "27"
                cnt = 1
                Toast.makeText(this@SearchActivity, "대구광역시를 선택하셨습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.code28.setOnClickListener {
            if (cnt == 0) {
                region = "28"
                cnt = 1
                Toast.makeText(this@SearchActivity, "인천광역시를 선택하셨습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.code29.setOnClickListener {
            if (cnt == 0) {
                region = "29"
                cnt = 1
                Toast.makeText(this@SearchActivity, "굉주광역시를 선택하셨습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.code30.setOnClickListener {
            if (cnt == 0) {
                region = "30"
                cnt = 1
                Toast.makeText(this@SearchActivity, "대전광역시 선택하셨습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.code31.setOnClickListener {
            if (cnt == 0) {
                region = "31"
                cnt = 1
                Toast.makeText(this@SearchActivity, "울산광역시 선택하셨습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.code36.setOnClickListener {
            if (cnt == 0) {
                region = "36"
                cnt = 1
                Toast.makeText(this@SearchActivity, "세종특별자치시를 선택하셨습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.code41.setOnClickListener {
            if (cnt == 0) {
                region = "41"
                cnt = 1
                Toast.makeText(this@SearchActivity, "경기도를 선택하셨습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.code43.setOnClickListener {
            if (cnt == 0) {
                region = "43"
                cnt = 1
                Toast.makeText(this@SearchActivity, "충청북도를 선택하셨습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.code44.setOnClickListener {
            if (cnt == 0) {
                region = "44"
                cnt = 1
                Toast.makeText(this@SearchActivity, "충청남도를 선택하셨습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.code45.setOnClickListener {
            if (cnt == 0) {
                region = "45"
                cnt = 1
                Toast.makeText(this@SearchActivity, "전라북도를 선택하셨습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.code46.setOnClickListener {
            if (cnt == 0) {
                region = "46"
                cnt = 1
                Toast.makeText(this@SearchActivity, "전라남도를 선택하셨습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.code47.setOnClickListener {
            if (cnt == 0) {
                region = "47"
                cnt = 1
                Toast.makeText(this@SearchActivity, "경상북도를 선택하셨습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.code48.setOnClickListener {
            if (cnt == 0) {
                region = "48"
                cnt = 1
                Toast.makeText(this@SearchActivity, "경상남도를 선택하셨습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.code50.setOnClickListener {
            if (cnt == 0) {
                region = "50"
                cnt = 1
                Toast.makeText(this@SearchActivity, "제주도를 선택하셨습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.code51.setOnClickListener {
            if (cnt == 0) {
                region = "51"
                cnt = 1
                Toast.makeText(this@SearchActivity, "강원도를 선택하셨습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.code00.setOnClickListener {
            if (cnt == 0) {
                region = ""
                cnt = 1
                Toast.makeText(this@SearchActivity, "전체 지역을 선택하셨습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnReset.setOnClickListener{
            binding.etName.setText("")
            binding.etDate.setText("")
            pfmName = ""
            startDate = ""
            endDate = ""
            region = ""
            cnt = 0
            Toast.makeText(this@SearchActivity, "입력하신 조건이 초기화되었습니다.", Toast.LENGTH_SHORT).show()
        }

        binding.btnSearch.setOnClickListener {
            if (startDate.length == 8) {
                searchPerformances(pfmName, startDate, endDate, region)
            } else {
                Toast.makeText(this@SearchActivity, "공연일을 선택하지 않아 오늘로 설정되었습니다.", Toast.LENGTH_SHORT).show()
                startDate = getToday()
                endDate = calculate30Days(startDate).toString()
                searchPerformances(pfmName, startDate, endDate, region)
            }
       }
    }

    private fun searchPerformances(pfmName: String, startDate: String, endDate: String, Region: String) {
        launch(Dispatchers.IO) {
            try {
                val client = OkHttpClient()

                val url: String
                if (pfmName == "") {
                    if (Region == "") {
                        url = "https://kopis.or.kr/openApi/restful/pblprfr?service=$API_KEY&stdate=$startDate&eddate=$endDate&cpage=1&rows=50&shcate=$GENRE"
                    } else {
                        url = "https://kopis.or.kr/openApi/restful/pblprfr?service=$API_KEY&stdate=$startDate&eddate=$endDate&cpage=1&rows=50&shcate=$GENRE&signgucode=$Region"
                    }
                } else {
                    if (Region == "") {
                        url = "https://kopis.or.kr/openApi/restful/pblprfr?service=$API_KEY&stdate=$startDate&eddate=$endDate&cpage=1&rows=50&shcate=$GENRE&shprfnm=$pfmName"
                    } else {
                        url = "https://kopis.or.kr/openApi/restful/pblprfr?service=$API_KEY&stdate=$startDate&eddate=$endDate&cpage=1&rows=50&shcate=$GENRE&signgucode=$Region&shprfnm=$pfmName"
                    }
                }

                val request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute()
                val body = response.body?.string()

                val searchResults: List<Performance>? = body?.let {
                    parsePerformances(
                        it
                    )
                }
                val sortedResults = searchResults?.sortedBy { it.endDate }

                withContext(Dispatchers.Main) {
                    if (sortedResults != null) {
                        showResults(sortedResults)
                    } else {
                        Toast.makeText(this@SearchActivity, "해당 조건의 공연이 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@SearchActivity, "검색에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showResults(searchResults: List<Performance>) {

        binding.nameText.visibility = View.GONE
        binding.editName.visibility = View.GONE
        binding.dateText.visibility = View.GONE
        binding.editDate.visibility = View.GONE
        binding.regionText.visibility = View.GONE
        binding.regionButtonLayout.visibility = View.GONE
        binding.btnReset.visibility = View.GONE
        binding.btnSearch.visibility = View.GONE

        binding.rvSearchResults.visibility = View.VISIBLE
        searchAdapter.submitList(searchResults)
    }

    private fun showDetail(performance: Performance) {
        Log.d("performance id", performance.id)
        val intent = Intent(this, PerformanceDetailActivity::class.java).apply {
            putExtra("performanceId", performance.id)
        }
        startActivity(intent)
    }

    private fun isValidDate(date: String): Boolean {
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        return try {
            LocalDate.parse(date, formatter)
            true
        } catch (e: DateTimeParseException) {
            false
        }
    }

    private fun calculate30Days(date: String): String? {
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        return try {
            val localDate = LocalDate.parse(date, formatter)
            val newDate = localDate.plusDays(30)
            newDate.format(formatter)
        } catch (e: DateTimeParseException) {
            null
        }
    }

    private fun getToday(): String {
        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        return today.format(formatter)
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

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}
