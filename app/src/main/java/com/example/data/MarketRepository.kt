package com.example.data

import kotlinx.coroutines.flow.Flow

class MarketRepository(private val marketDao: MarketDao) {

    // --- Sessions ---
    val allSessions: Flow<List<BazarSession>> = marketDao.getAllSessions()

    fun getSessionByIdFlow(sessionId: Int): Flow<BazarSession?> =
        marketDao.getSessionByIdFlow(sessionId)

    suspend fun getSessionById(sessionId: Int): BazarSession? =
        marketDao.getSessionById(sessionId)

    suspend fun insertSession(session: BazarSession): Long =
        marketDao.insertSession(session)

    suspend fun updateSession(session: BazarSession) =
        marketDao.updateSession(session)

    suspend fun deleteSession(session: BazarSession) =
        marketDao.deleteSession(session)

    // --- Items ---
    fun getItemsForSession(sessionId: Int): Flow<List<BazarItem>> =
        marketDao.getItemsForSession(sessionId)

    suspend fun insertBazarItem(item: BazarItem): Long =
        marketDao.insertBazarItem(item)

    suspend fun insertBazarItems(items: List<BazarItem>) =
        marketDao.insertBazarItems(items)

    suspend fun updateBazarItem(item: BazarItem) =
        marketDao.updateBazarItem(item)

    suspend fun deleteBazarItem(item: BazarItem) =
        marketDao.deleteBazarItem(item)

    val allEggPurchaseItems: Flow<List<BazarItem>> = marketDao.getAllEggPurchaseItems()

    // --- People ---
    val allPeople: Flow<List<Person>> = marketDao.getAllPeople()

    suspend fun insertPerson(person: Person): Long =
        marketDao.insertPerson(person)

    suspend fun deletePerson(person: Person) =
        marketDao.deletePerson(person)

    // --- Money Entries ---
    val allMoneyEntries: Flow<List<MoneyEntry>> = marketDao.getAllMoneyEntries()

    fun getMoneyEntriesForPerson(personId: Int): Flow<List<MoneyEntry>> =
        marketDao.getMoneyEntriesForPerson(personId)

    suspend fun insertMoneyEntry(entry: MoneyEntry) =
        marketDao.insertMoneyEntry(entry)

    suspend fun deleteMoneyEntry(entry: MoneyEntry) =
        marketDao.deleteMoneyEntry(entry)

    suspend fun undoLatestMoneyEntryForPerson(personId: Int): Boolean {
        val latest = marketDao.getLatestMoneyEntryForPerson(personId)
        return if (latest != null) {
            marketDao.deleteMoneyEntry(latest)
            true
        } else {
            false
        }
    }

    // --- Meal Entries ---
    val allMealEntries: Flow<List<MealEntry>> = marketDao.getAllMealEntries()

    fun getMealEntriesForPerson(personId: Int): Flow<List<MealEntry>> =
        marketDao.getMealEntriesForPerson(personId)

    suspend fun insertMealEntry(entry: MealEntry) =
        marketDao.insertMealEntry(entry)

    suspend fun deleteMealEntry(entry: MealEntry) =
        marketDao.deleteMealEntry(entry)

    suspend fun undoLatestMealEntryForPerson(personId: Int): Boolean {
        val latest = marketDao.getLatestMealEntryForPerson(personId)
        return if (latest != null) {
            marketDao.deleteMealEntry(latest)
            true
        } else {
            false
        }
    }

    // --- Egg Entries ---
    val allEggEntries: Flow<List<EggEntry>> = marketDao.getAllEggEntries()

    suspend fun insertEggEntry(entry: EggEntry) =
        marketDao.insertEggEntry(entry)

    suspend fun deleteEggEntry(entry: EggEntry) =
        marketDao.deleteEggEntry(entry)

    suspend fun undoLatestEggEntry(): Boolean {
        val latest = marketDao.getLatestEggEntry()
        return if (latest != null) {
            marketDao.deleteEggEntry(latest)
            true
        } else {
            false
        }
    }

    // --- Egg Add Entries ---
    val allEggAddEntries: Flow<List<EggAddEntry>> = marketDao.getAllEggAddEntries()

    suspend fun insertEggAddEntry(entry: EggAddEntry) =
        marketDao.insertEggAddEntry(entry)

    suspend fun deleteEggAddEntry(entry: EggAddEntry) =
        marketDao.deleteEggAddEntry(entry)

    suspend fun undoLatestEggAddEntry(): Boolean {
        val latest = marketDao.getLatestEggAddEntry()
        return if (latest != null) {
            marketDao.deleteEggAddEntry(latest)
            true
        } else {
            false
        }
    }
}
