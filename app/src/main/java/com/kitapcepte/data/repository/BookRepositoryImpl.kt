package com.kitapcepte.data.repository

import com.kitapcepte.data.local.dao.CartDao
import com.kitapcepte.data.local.dao.FavoriteDao
import com.kitapcepte.data.local.entity.CartItemEntity
import com.kitapcepte.data.local.entity.FavoriteBookEntity
import com.kitapcepte.data.mapper.BookMapper
import com.kitapcepte.data.remote.OpenLibraryApi
import com.kitapcepte.domain.model.Book
import com.kitapcepte.domain.model.BookCategory
import com.kitapcepte.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRepositoryImpl @Inject constructor(
    private val api: OpenLibraryApi,
    private val favoriteDao: FavoriteDao,
    private val cartDao: CartDao,
    private val bookMapper: BookMapper
) : BookRepository {

    private val cache = ConcurrentHashMap<BookCategory, List<Book>>()

    override suspend fun getBooksByCategory(category: BookCategory): Result<List<Book>> {
        cache[category]?.let { cachedBooks ->
            return Result.success(cachedBooks)
        }

        return try {
            val response = api.searchBooks(query = category.query, limit = 30)
            val books = bookMapper.mapListToDomain(response.docs, category)
            cache[category] = books
            Result.success(books)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getFavoriteBookIds(): Flow<Set<String>> {
        return favoriteDao.getAllFavorites().map { list ->
            list.map { it.bookId }.toSet()
        }
    }

    override fun getCartBookIds(): Flow<Set<String>> {
        return cartDao.getCartItems().map { list ->
            list.map { it.bookId }.toSet()
        }
    }

    override fun getCartItemCount(): Flow<Int> {
        return cartDao.getCartCount()
    }

    override suspend fun toggleFavorite(book: Book): Boolean {
        val isCurrentlyFavorite = favoriteDao.isFavoriteDirect(book.id)
        return if (isCurrentlyFavorite) {
            favoriteDao.deleteFavorite(book.id)
            false
        } else {
            val entity = FavoriteBookEntity(
                bookId = book.id,
                title = book.title,
                author = book.author,
                coverUrl = book.coverUrl,
                price = book.price,
                originalPrice = book.originalPrice,
                categorySlug = book.category.slug,
                isTopItem = book.isTopItem
            )
            favoriteDao.insertFavorite(entity)
            true
        }
    }

    override suspend fun addToCart(book: Book) {
        val existingItem = cartDao.getCartItemByBookId(book.id)
        if (existingItem != null) {
            cartDao.updateQuantity(existingItem.id, existingItem.quantity + 1)
        } else {
            val newItem = CartItemEntity(
                bookId = book.id,
                title = book.title,
                author = book.author,
                coverUrl = book.coverUrl,
                price = book.price,
                quantity = 1
            )
            cartDao.insertOrUpdate(newItem)
        }
    }
}
