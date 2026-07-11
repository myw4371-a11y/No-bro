package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MarketDao {

    // --- Bazar Sessions ---
    @Query("SELECT * FROM bazar_sessions ORDER BY id DESC")
    fun getAllSessions(): Flow<List<BazarSession>>

    @Query("SELECT * FROM bazar_sessions WHERE id = :sessionId LIMIT 1")
    fun getSessionByIdFlow(sessionId: Int): Flow<BazarSession?>

    @Query("SELECT * FROM bazar_sessions WHERE id = :sessionId LIMIT 1")
    suspend fun getSessionById(sessionId: Int): BazarSession?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: BazarSession): Long

    @Update
    suspend fun updateSession(session: BazarSession)

    @Delete
    suspend fun deleteSession(session: BazarSession)

    // --- Bazar Items ---
    @Query("SELECT * FROM bazar_items WHERE sessionId = :sessionId ORDER BY id ASC")
    fun getItemsForSession(sessionId: Int): Flow<List<BazarItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBazarItem(item: BazarItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBazarItems(items: List<BazarItem>)

    @Update
    suspend fun updateBazarItem(item: BazarItem)

    @Delete
    suspend fun deleteBazarItem(item: BazarItem)

    // Query to help calculate egg count from all saved/permanent or active sessions:
    // We look for items containing "ডিম" (Bengali for Egg)
    @Query("SELECT * FROM bazar_items WHERE name LIKE '%ডিম%'")
    fun getAllEggPurchaseItems(): Flow<List<BazarItem>>

    // --- People ---
    @Query("SELECT * FROM people ORDER BY id ASC")
    fun getAllPeople(): Flow<List<Person>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPerson(person: Person): Long

    @Delete
    suspend fun deletePerson(person: Person)

    // --- Money Entries ---
    @Query("SELECT * FROM money_entries WHERE personId = :personId ORDER BY timestamp ASC")
    fun getMoneyEntriesForPerson(personId: Int): Flow<List<MoneyEntry>>

    @Query("SELECT * FROM money_entries ORDER BY timestamp DESC")
    fun getAllMoneyEntries(): Flow<List<MoneyEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMoneyEntry(entry: MoneyEntry)

    @Delete
    suspend fun deleteMoneyEntry(entry: MoneyEntry)

    @Query("SELECT * FROM money_entries WHERE personId = :personId ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestMoneyEntryForPerson(personId: Int): MoneyEntry?

    // --- Meal Entries ---
    @Query("SELECT * FROM meal_entries WHERE personId = :personId ORDER BY timestamp ASC")
    fun getMealEntriesForPerson(personId: Int): Flow<List<MealEntry>>

    @Query("SELECT * FROM meal_entries ORDER BY timestamp DESC")
    fun getAllMealEntries(): Flow<List<MealEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMealEntry(entry: MealEntry)

    @Delete
    suspend fun deleteMealEntry(entry: MealEntry)

    @Query("SELECT * FROM meal_entries WHERE personId = :personId ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestMealEntryForPerson(personId: Int): MealEntry?

    // --- Egg Entries ---
    @Query("SELECT * FROM egg_entries ORDER BY timestamp ASC")
    fun getAllEggEntries(): Flow<List<EggEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEggEntry(entry: EggEntry)

    @Delete
    suspend fun deleteEggEntry(entry: EggEntry)

    @Query("SELECT * FROM egg_entries ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestEggEntry(): EggEntry?

    // --- Egg Add Entries ---
    @Query("SELECT * FROM egg_add_entries ORDER BY timestamp ASC")
    fun getAllEggAddEntries(): Flow<List<EggAddEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEggAddEntry(entry: EggAddEntry)

    @Delete
    suspend fun deleteEggAddEntry(entry: EggAddEntry)

    @Query("SELECT * FROM egg_add_entries ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestEggAddEntry(): EggAddEntry?
}
