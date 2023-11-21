package com.example.db

import com.example.model.User
import org.ehcache.config.builders.CacheConfigurationBuilder
import org.ehcache.config.builders.CacheManagerBuilder
import org.ehcache.config.builders.ResourcePoolsBuilder
import org.ehcache.config.units.EntryUnit
import org.ehcache.config.units.MemoryUnit
import org.ehcache.impl.config.persistence.CacheManagerPersistenceConfiguration
import java.io.File

class UserServiceCacheImpl(
    private val userService: UserService,
    storagePath:File
) : UserService {
    private val cacheName="usersCache"
    private val cacheConfigurationBuilder=CacheConfigurationBuilder.newCacheConfigurationBuilder(
        Int::class.javaObjectType,
        User::class.java,
        ResourcePoolsBuilder.newResourcePoolsBuilder()
            .heap(1000, EntryUnit.ENTRIES)
            .offheap(10, MemoryUnit.MB)
            .disk(100, MemoryUnit.MB, true)
    )
    private val cacheManager=CacheManagerBuilder.newCacheManagerBuilder()
        .with(CacheManagerPersistenceConfiguration(storagePath))
        .withCache(cacheName,cacheConfigurationBuilder)
        .build(true)

    private val usersCache=cacheManager.getCache(cacheName,Int::class.javaObjectType,User::class.java)

    override suspend fun addUser(user: User): User? {
        return userService.addUser(user)?.also {
            usersCache.put(it.id,it)
        }
    }

    override suspend fun updateUser(user: User): Boolean {
        usersCache.put(user.id,user)
        return userService.updateUser(user)
    }

    override suspend fun deleteUser(user: User): Boolean {
        usersCache.remove(user.id)
        return userService.deleteUser(user)
    }

    override suspend fun getUsers(): List<User> {
        return userService.getUsers()
    }

    override suspend fun searchUser(query: String): List<User> {
        return userService.searchUser(query)
    }

    override suspend fun getUser(id: Int): User? {
        return usersCache[id] ?: userService.getUser(id)?.also {
            usersCache.put(id,it)
        }
    }
}