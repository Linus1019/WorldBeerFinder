package com.example.worldbeerfinder.viewModels

import androidx.lifecycle.*
import com.example.worldbeerfinder.apiService.BeerApi
import com.example.worldbeerfinder.apiService.BeerApiResponse
import com.example.worldbeerfinder.models.BeerItem
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel: ViewModel() {
    enum class ErrorCode {
        NONE,
        EMPTY_KEYWORD,
        EMPTY_RESULT,
        API_ERROR
    }

    private var isLoading = false

    val page = MediatorLiveData<Int>().apply { value = DEFAULT_PAGE }
    private val perPage = MutableLiveData<Int>().apply { value = DEFAULT_PER_PAGE}
    val keyword = MutableLiveData<String>()

    val beerList = MediatorLiveData<List<BeerItem>>()
    val selectedBeerItem = MutableLiveData<BeerItem>()

    val errorCode = MutableLiveData<ErrorCode>()
    var errorMessage = ""

    init {
        page.addSource(keyword) {
            page.value = DEFAULT_PAGE
        }

        beerList.addSource(page) {
            if (isLoading) return@addSource
            loadBeerList(keyword.value ?: "", page.value!!, perPage.value!!)
        }
    }

    fun loadBeerList(keyword: String, page: Int, perPage: Int) {
        viewModelScope.launch {
            if (isLoading || keyword.isEmpty()) {
                errorMessage = EMPTY_KEYWORD_ERROR_MESSAGE
                errorCode.value = ErrorCode.EMPTY_KEYWORD
                return@launch
            }

            isLoading = true
            BeerApi.client.create(BeerApi::class.java)
                .findBeerList(keyword, page, perPage).enqueue(object: Callback<List<BeerApiResponse>> {
                    override fun onResponse(
                        call: Call<List<BeerApiResponse>>,
                        response: Response<List<BeerApiResponse>>
                    ) {
                        val body = response.body()
                        val hasBody = body.isNullOrEmpty().not()

                        errorMessage = if (hasBody) "" else EMPTY_RESULT_ERROR_MESSAGE
                        errorCode.value = if (hasBody) ErrorCode.NONE else ErrorCode.EMPTY_RESULT

                        if (errorCode.value != ErrorCode.NONE) {
                            isLoading = false
                            return
                        }

                        beerList.value = if (beerList.value == null) {
                            body?.map {
                                BeerItem(
                                    it.imageUrl,
                                    it.beerName!!,
                                    it.firstBrewed!!,
                                    it.tagLine!!,
                                    it.description!!
                                )
                            }
                        } else {
                            beerList.value!!
                                .plus(body?.map {
                                    BeerItem(
                                        it.imageUrl,
                                        it.beerName!!,
                                        it.firstBrewed!!,
                                        it.tagLine!!,
                                        it.description!!
                                    )
                                }!!)
                        }

                        isLoading = false
                    }

                    override fun onFailure(call: Call<List<BeerApiResponse>>, t: Throwable) {
                        errorCode.value = ErrorCode.API_ERROR
                        errorMessage = t.message ?: ""
                        isLoading = false
                    }
                })
        }
    }

    companion object {
        const val DEFAULT_PAGE = 1
        const val DEFAULT_PER_PAGE = 10
        const val EMPTY_KEYWORD_ERROR_MESSAGE = "검색어를 입력해주세요."
        const val EMPTY_RESULT_ERROR_MESSAGE = "검색 결과가 없습니다."
    }
}