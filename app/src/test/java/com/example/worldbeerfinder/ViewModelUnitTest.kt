package com.example.worldbeerfinder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.worldbeerfinder.models.BeerItem
import com.example.worldbeerfinder.viewModels.MainViewModel
import junit.framework.Assert.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class ViewModelUnitTest {
    lateinit var mainViewModel: MainViewModel

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Before
    fun setUp() {
        mainViewModel = MainViewModel()
    }

    @Test
    fun viewModelTest() {
        val keywordObserver = Mockito.mock(Observer::class.java) as Observer<String>
        mainViewModel.keyword.observeForever(keywordObserver)

        val beerListObserver = Mockito.mock(Observer::class.java) as Observer<List<BeerItem>>
        mainViewModel.beerList.observeForever(beerListObserver)

        val pageListObserver = Mockito.mock(Observer::class.java) as Observer<Int>
        mainViewModel.page.observeForever(pageListObserver)

        mainViewModel.keyword.value = "c"

        Thread.sleep(1000) //api 호출 완료까지 대기하기 위한 sleep
        assertNotNull(mainViewModel.beerList.value)

        mainViewModel.page.value = 2

        Thread.sleep(1000)
        assertEquals(mainViewModel.beerList.value!!.size, 20)

        mainViewModel.keyword.value = ""
        mainViewModel.page.value = 1

        Thread.sleep(1000)
        assertEquals(mainViewModel.errorCode.value, MainViewModel.ErrorCode.EMPTY_KEYWORD)

        mainViewModel.keyword.value = "##"
        mainViewModel.page.value = 1
        Thread.sleep(1000)
        mainViewModel.beerList.value = null
        assertEquals(mainViewModel.errorCode.value, MainViewModel.ErrorCode.EMPTY_RESULT)
    }
}