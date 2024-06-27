package com.example.worldnewsinfo.data.room

interface NewsDataBaseRepository<T, N, M> {
    fun getNews(parameters: N):List<T>

    fun saveNews(list: List<M>?, parameters: N)
}