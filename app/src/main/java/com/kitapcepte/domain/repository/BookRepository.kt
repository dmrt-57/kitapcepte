package com.kitapcepte.domain.repository

import com.kitapcepte.domain.model.Book
import com.kitapcepte.domain.model.BookCategory
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    suspend fun getBooksByCategory(category: BookCategory): Result<List<Book>>
    fun getFavoriteBookIds(): Flow<Set<String>>
    fun getCartBookIds(): Flow<Set<String>>
    fun getCartItemCount(): Flow<Int>
    suspend fun toggleFavorite(book: Book): Boolean
    suspend fun addToCart(book: Book)
}
