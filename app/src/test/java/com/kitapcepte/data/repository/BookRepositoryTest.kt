package com.kitapcepte.data.repository

import com.google.common.truth.Truth.assertThat
import com.kitapcepte.data.local.dao.CartDao
import com.kitapcepte.data.local.dao.FavoriteDao
import com.kitapcepte.data.local.entity.CartItemEntity
import com.kitapcepte.data.mapper.BookMapper
import com.kitapcepte.data.remote.OpenLibraryApi
import com.kitapcepte.data.remote.dto.BookDocDto
import com.kitapcepte.data.remote.dto.SearchResponseDto
import com.kitapcepte.domain.model.Book
import com.kitapcepte.domain.model.BookCategory
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.IOException

class BookRepositoryTest {

    private val api: OpenLibraryApi = mockk()
    private val favoriteDao: FavoriteDao = mockk(relaxed = true)
    private val cartDao: CartDao = mockk(relaxed = true)
    private val bookMapper: BookMapper = mockk()

    private lateinit var repository: BookRepositoryImpl

    private val testBook = Book(
        id = "OL123W",
        title = "Test Book",
        author = "Test Author",
        coverUrl = null,
        price = 99.90,
        category = BookCategory.FANTASY
    )

    @Before
    fun setUp() {
        coEvery { favoriteDao.insertFavorite(any()) } just runs
        coEvery { favoriteDao.deleteFavorite(any()) } just runs
        coEvery { cartDao.insertOrUpdate(any()) } returns 1L
        coEvery { cartDao.updateQuantity(any(), any()) } just runs

        repository = BookRepositoryImpl(
            api = api,
            favoriteDao = favoriteDao,
            cartDao = cartDao,
            bookMapper = bookMapper
        )
    }

    @Test
    fun `getBooksByCategory fetches from API and caches result`() = runTest {
        val dto = BookDocDto(key = "/works/OL123W", title = "Test Book")
        val response = SearchResponseDto(numFound = 1, docs = listOf(dto))
        coEvery { api.searchBooks("subject:fantasy", 30) } returns response
        every { bookMapper.mapListToDomain(listOf(dto), BookCategory.FANTASY) } returns listOf(testBook)

        // First call - invokes API
        val result1 = repository.getBooksByCategory(BookCategory.FANTASY)
        assertThat(result1.isSuccess).isTrue()
        assertThat(result1.getOrNull()).containsExactly(testBook)

        // Second call - returns cached, does not call API again
        val result2 = repository.getBooksByCategory(BookCategory.FANTASY)
        assertThat(result2.isSuccess).isTrue()
        assertThat(result2.getOrNull()).containsExactly(testBook)

        coVerify(exactly = 1) { api.searchBooks("subject:fantasy", 30) }
    }

    @Test
    fun `getBooksByCategory returns failure when API fails`() = runTest {
        coEvery { api.searchBooks("subject:history", 30) } throws IOException("Network timeout")

        val result = repository.getBooksByCategory(BookCategory.HISTORY)
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()?.message).contains("Network timeout")
    }

    @Test
    fun `toggleFavorite adds favorite when not currently favorite`() = runTest {
        coEvery { favoriteDao.isFavoriteDirect("OL123W") } returns false

        val isAdded = repository.toggleFavorite(testBook)

        assertThat(isAdded).isTrue()
        coVerify(exactly = 1) { favoriteDao.insertFavorite(any()) }
        coVerify(exactly = 0) { favoriteDao.deleteFavorite(any()) }
    }

    @Test
    fun `toggleFavorite removes favorite when already favorite`() = runTest {
        coEvery { favoriteDao.isFavoriteDirect("OL123W") } returns true

        val isAdded = repository.toggleFavorite(testBook)

        assertThat(isAdded).isFalse()
        coVerify(exactly = 1) { favoriteDao.deleteFavorite("OL123W") }
        coVerify(exactly = 0) { favoriteDao.insertFavorite(any()) }
    }

    @Test
    fun `addToCart inserts new item if not in cart`() = runTest {
        coEvery { cartDao.getCartItemByBookId("OL123W") } returns null

        repository.addToCart(testBook)

        coVerify(exactly = 1) { cartDao.insertOrUpdate(match { it.bookId == "OL123W" && it.quantity == 1 }) }
    }

    @Test
    fun `addToCart increments quantity if already in cart`() = runTest {
        val existing = CartItemEntity(id = 5L, bookId = "OL123W", title = "Test", author = "Author", coverUrl = null, price = 99.90, quantity = 2)
        coEvery { cartDao.getCartItemByBookId("OL123W") } returns existing

        repository.addToCart(testBook)

        coVerify(exactly = 1) { cartDao.updateQuantity(5L, 3) }
    }
}
