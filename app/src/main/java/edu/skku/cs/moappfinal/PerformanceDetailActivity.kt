package edu.skku.cs.moappfinal

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import edu.skku.cs.moappfinal.databinding.ActivityPerformanceDetailBinding
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.simpleframework.xml.core.Persister
import java.io.StringReader
import kotlin.coroutines.CoroutineContext

class PerformanceDetailActivity : AppCompatActivity(), CoroutineScope {

    private val API_KEY = "35f87caec8404a1885df223a1ff86692"

    private val job = Job()
    override val coroutineContext: CoroutineContext get() = Dispatchers.Main + job

    private lateinit var binding: ActivityPerformanceDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPerformanceDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val performanceId = intent.getStringExtra("performanceId")
        if (performanceId.isNullOrEmpty()) {
            Toast.makeText(this, "공연 ID가 없습니다.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        loadPerformanceDetail(performanceId)
    }

    private fun loadPerformanceDetail(id: String) {
        launch(Dispatchers.IO) {
            try {
                val url = "https://kopis.or.kr/openApi/restful/pblprfr/$id?service=$API_KEY"

                val client = OkHttpClient()
                val request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute()
                val body = response.body?.string()

                if (body != null) {
                    Log.d("performance detail", body)
                }

                if (response.isSuccessful && !body.isNullOrEmpty()) {
                    val detail = parsePerformanceDetail(body)
                    withContext(Dispatchers.Main) {
                        if (detail != null) {
                            updateUI(detail)
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@PerformanceDetailActivity,
                            "상세 정보 요청 실패",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@PerformanceDetailActivity, "네트워크 에러", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun parsePerformanceDetail(xml: String): PerformanceDetails? {
        val serializer = Persister()
        return try {
            val performanceDetails = serializer.read(PerformanceDetails::class.java, StringReader(xml))
            performanceDetails
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun updateUI(detail: PerformanceDetails) {
        Glide.with(this)
            .load(detail.dbList?.poster)
            .error(R.drawable.placeholder)
            .into(binding.ivDetailPoster)

        binding.tvDetailTitle.text = detail.dbList?.prfnm
        if (detail.dbList?.prfpdfrom == " " && detail.dbList?.prfpdto == " ") {
            binding.tvDetailPeriod.text = "기간: 기간 정보가 없습니다."
        } else {
            binding.tvDetailPeriod.text = "기간: ${detail.dbList?.prfpdfrom} ~ ${detail.dbList?.prfpdto}"
        }
        if (detail.dbList?.fcltynm == " ") {
            binding.tvDetailPlace.text = "장소: 장소 정보가 없습니다."
        } else {
            binding.tvDetailPlace.text = "장소: ${detail.dbList?.fcltynm}"
        }
        if (detail.dbList?.dtguidance == " ") {
            binding.tvDetailTime.text = "시간: 시간 정보가 없습니다."
        } else {
            binding.tvDetailTime.text = "시간: ${detail.dbList?.dtguidance}"
        }
        if (detail.dbList?.prfcast == " ") {
            binding.tvDetailCast.text = "출연진: 출연진 정보가 없습니다."
        } else {
            binding.tvDetailCast.text = "출연진: ${detail.dbList?.prfcast}"
        }
        if (detail.dbList?.prfruntime == " ") {
            binding.tvDetailRuntime.text = "런타임: 런타임 정보가 없습니다."
        } else {
            binding.tvDetailRuntime.text = "런타임: ${detail.dbList?.prfruntime}"
        }
        if (detail.dbList?.prfage == " ") {
            binding.tvDetailAge.text = "관람 연령: 관람 연령 정보가 없습니다."
        } else {
            binding.tvDetailAge.text = "관람 연령: ${detail.dbList?.prfage}"
        }
        val relateUrl = detail.dbList?.relates?.firstOrNull()?.url
        Log.d("relate", detail.dbList?.relates.toString())
        if (relateUrl != null) {
            Log.d("relate url", relateUrl)
        }
        if (relateUrl == " ") {
            binding.tvDetailLink.text = "링크: 관련 링크가 없습니다."
        } else {
            binding.tvDetailLink.text = "링크: ${relateUrl}"
            Linkify.addLinks(binding.tvDetailLink, Linkify.WEB_URLS)
            binding.tvDetailLink.movementMethod = LinkMovementMethod.getInstance()
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

}