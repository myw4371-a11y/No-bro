package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar

@OptIn(ExperimentalCoroutinesApi::class)
class MarketViewModel(private val repository: MarketRepository) : ViewModel() {

    // --- State Flows from Database ---
    val sessions = repository.allSessions.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _currentSessionId = MutableStateFlow<Int?>(null)
    val currentSessionId: StateFlow<Int?> = _currentSessionId.asStateFlow()

    val currentSessionItems: StateFlow<List<BazarItem>> = _currentSessionId
        .flatMapLatest { id ->
            if (id != null) repository.getItemsForSession(id) else flowOf(emptyList())
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val currentSession: StateFlow<BazarSession?> = _currentSessionId
        .flatMapLatest { id ->
            if (id != null) repository.getSessionByIdFlow(id) else flowOf(null)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val people = repository.allPeople.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val allMoneyEntries = repository.allMoneyEntries.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val allMealEntries = repository.allMealEntries.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val allEggEntries = repository.allEggEntries.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val allEggAddEntries = repository.allEggAddEntries.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Cache to store the previous state of a BazarItem before saving, for Undo purposes
    private val lastSavedItemStates = mutableMapOf<Int, BazarItem>()

    // --- Navigation Helper ---
    fun setCurrentSessionId(sessionId: Int?) {
        _currentSessionId.value = sessionId
    }

    // --- Date Formatting Helpers in Bengali ---
    fun getBengaliDate(): String {
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val monthIndex = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)

        val banglaMonths = listOf(
            "জানুয়ারি", "ফেব্রুয়ারি", "মার্চ", "এপ্রিল", "মে", "জুন",
            "জুলাই", "আগস্ট", "সেপ্টেম্বর", "অক্টোবর", "নভেম্বর", "ডিসেম্বর"
        )
        val banglaMonth = banglaMonths.getOrElse(monthIndex) { "" }
        return "${day.toBangla()} $banglaMonth ${year.toBangla()}"
    }

    fun getBengaliDayMonth(): String {
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val monthIndex = calendar.get(Calendar.MONTH)

        val banglaMonths = listOf(
            "জানুয়ারি", "ফেব্রুয়ারি", "মার্চ", "এপ্রিল", "মে", "জুন",
            "জুলাই", "আগস্ট", "সেপ্টেম্বর", "অক্টোবর", "নভেম্বর", "ডিসেম্বর"
        )
        val banglaMonth = banglaMonths.getOrElse(monthIndex) { "" }
        return "${day.toBangla()} $banglaMonth"
    }

    // --- Bazar Sessions & Items Actions ---
    fun createBazarSession(type: String, onComplete: (Int) -> Unit) {
        viewModelScope.launch {
            val date = getBengaliDate()
            val session = BazarSession(type = type, date = date)
            val sessionId = repository.insertSession(session).toInt()

            if (type == "বড় বাজার") {
                // Populate default items
                val defaultItems = listOf(
                    "মুরগি", "মাছ", "চাল", "তেল", "ডিম", "পেঁয়াজ", "রসুন",
                    "কাঁচা মরিচ", "হলুদের গুঁড়া", "মরিচের গুঁড়া", "জিরা",
                    "গরম মসলা", "আদা", "খাওয়া ও ভাড়া", "সাবান", "ভিম"
                )
                val items = defaultItems.map { name ->
                    BazarItem(sessionId = sessionId, name = name)
                }
                repository.insertBazarItems(items)
            }
            onComplete(sessionId)
        }
    }

    fun addNewItemToSession(sessionId: Int, name: String) {
        viewModelScope.launch {
            val item = BazarItem(sessionId = sessionId, name = name)
            repository.insertBazarItem(item)
        }
    }

    fun saveBazarItem(item: BazarItem, quantity: Double?, unitPrice: Double?, totalPrice: Double) {
        viewModelScope.launch {
            // Keep track of previous state in memory for Undo action
            lastSavedItemStates[item.id] = item.copy()

            val updatedItem = item.copy(
                quantity = quantity,
                unitPrice = unitPrice,
                totalPrice = totalPrice,
                isSaved = true
            )
            repository.insertBazarItem(updatedItem)
            recalculateSessionTotal(item.sessionId)
        }
    }

    fun undoBazarItemSave(item: BazarItem) {
        viewModelScope.launch {
            val previousState = lastSavedItemStates[item.id]
            if (previousState != null) {
                repository.insertBazarItem(previousState)
                lastSavedItemStates.remove(item.id)
            } else {
                // Clear state if no previous history
                val clearedItem = item.copy(
                    quantity = null,
                    unitPrice = null,
                    totalPrice = 0.0,
                    isSaved = false
                )
                repository.insertBazarItem(clearedItem)
            }
            recalculateSessionTotal(item.sessionId)
        }
    }

    private suspend fun recalculateSessionTotal(sessionId: Int) {
        val items = repository.getItemsForSession(sessionId).first()
        val total = items.filter { it.isSaved }.sumOf { it.totalPrice }
        val session = repository.getSessionById(sessionId)
        if (session != null) {
            repository.updateSession(session.copy(totalAmount = total))
        }
    }

    fun lockSessionPermanently(sessionId: Int) {
        viewModelScope.launch {
            val session = repository.getSessionById(sessionId)
            if (session != null) {
                repository.updateSession(session.copy(isPermanent = true))
            }
        }
    }

    fun deleteSession(session: BazarSession) {
        viewModelScope.launch {
            repository.deleteSession(session)
        }
    }

    // --- Person Actions ---
    fun addPerson(name: String) {
        viewModelScope.launch {
            val person = Person(name = name)
            repository.insertPerson(person)
        }
    }

    fun deletePerson(person: Person) {
        viewModelScope.launch {
            repository.deletePerson(person)
        }
    }

    // --- Money Actions ---
    fun addMoneyEntry(personId: Int, amount: Double) {
        viewModelScope.launch {
            val entry = MoneyEntry(
                personId = personId,
                date = getBengaliDayMonth(),
                amount = amount
            )
            repository.insertMoneyEntry(entry)
        }
    }

    fun undoMoneyEntry(personId: Int, onResult: (Boolean) -> Unit = {}) {
        viewModelScope.launch {
            val success = repository.undoLatestMoneyEntryForPerson(personId)
            onResult(success)
        }
    }

    // --- Meal Actions ---
    fun addMealEntry(personId: Int, count: Double) {
        viewModelScope.launch {
            val entry = MealEntry(
                personId = personId,
                date = getBengaliDayMonth(),
                count = count
            )
            repository.insertMealEntry(entry)
        }
    }

    fun undoMealEntry(personId: Int, onResult: (Boolean) -> Unit = {}) {
        viewModelScope.launch {
            val success = repository.undoLatestMealEntryForPerson(personId)
            onResult(success)
        }
    }

    // --- Egg Actions ---
    fun addEggEntry(count: Int) {
        viewModelScope.launch {
            val entry = EggEntry(
                date = getBengaliDayMonth(),
                count = count
            )
            repository.insertEggEntry(entry)
        }
    }

    fun undoEggEntry(onResult: (Boolean) -> Unit = {}) {
        viewModelScope.launch {
            val success = repository.undoLatestEggEntry()
            onResult(success)
        }
    }

    fun addEggAddEntry(count: Int) {
        viewModelScope.launch {
            val entry = EggAddEntry(
                date = getBengaliDayMonth(),
                count = count
            )
            repository.insertEggAddEntry(entry)
        }
    }

    fun undoEggAddEntry(onResult: (Boolean) -> Unit = {}) {
        viewModelScope.launch {
            val success = repository.undoLatestEggAddEntry()
            onResult(success)
        }
    }

    // --- Manager Calculation States ---
    data class ManagerSummary(
        val totalMoneyRaised: Double = 0.0,
        val totalBazarExpense: Double = 0.0,
        val totalMeals: Double = 0.0,
        val currentCash: Double = 0.0,
        val mealRate: Double = 0.0,
        val totalEggsBought: Double = 0.0,
        val totalEggsUsed: Int = 0,
        val remainingEggs: Double = 0.0
    )

    data class PersonSummary(
        val person: Person,
        val moneyGiven: Double,
        val mealsCount: Double,
        val mealExpense: Double,
        val balance: Double
    )

    val managerSummaryState: StateFlow<ManagerSummary> = combine(
        allMoneyEntries,
        sessions,
        allMealEntries,
        allEggAddEntries,
        allEggEntries
    ) { money, sess, meals, eggAdds, eggUses ->
        val totalMoney = money.sumOf { it.amount }
        // Total expense of ALL sessions
        val totalExpense = sess.sumOf { it.totalAmount }
        val sumMeals = meals.sumOf { it.count }
        val cash = totalMoney - totalExpense
        val rate = if (sumMeals > 0.0) totalExpense / sumMeals else 0.0

        // Find items manually added
        val eggsBought = eggAdds.sumOf { it.count }.toDouble()

        val eggsUsed = eggUses.sumOf { it.count }
        val eggsLeft = eggsBought - eggsUsed

        ManagerSummary(
            totalMoneyRaised = totalMoney,
            totalBazarExpense = totalExpense,
            totalMeals = sumMeals,
            currentCash = cash,
            mealRate = rate,
            totalEggsBought = eggsBought,
            totalEggsUsed = eggsUsed,
            remainingEggs = eggsLeft
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ManagerSummary()
    )

    val personSummariesState: StateFlow<List<PersonSummary>> = combine(
        people,
        allMoneyEntries,
        allMealEntries,
        managerSummaryState
    ) { pList, money, meals, summary ->
        pList.map { person ->
            val moneyGiven = money.filter { it.personId == person.id }.sumOf { it.amount }
            val mealsCount = meals.filter { it.personId == person.id }.sumOf { it.count }
            val mealExpense = mealsCount * summary.mealRate
            val balance = moneyGiven - mealExpense
            PersonSummary(
                person = person,
                moneyGiven = moneyGiven,
                mealsCount = mealsCount,
                mealExpense = mealExpense,
                balance = balance
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
}

class MarketViewModelFactory(private val repository: MarketRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MarketViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MarketViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

// --- Number translation helpers to Bengali ---
fun String.toBangla(): String {
    return this.map { char ->
        when (char) {
            '0' -> '০'
            '1' -> '১'
            '2' -> '২'
            '3' -> '৩'
            '4' -> '৪'
            '5' -> '৫'
            '6' -> '৬'
            '7' -> '৭'
            '8' -> '৮'
            '9' -> '৯'
            '.' -> '.'
            else -> char
        }
    }.joinToString("")
}

fun Double.toBangla(decimalPlaces: Int = 2): String {
    val formatted = String.format("%.${decimalPlaces}f", this)
    val clean = if (formatted.endsWith(".00")) formatted.substringBefore(".00") else formatted
    return clean.toBangla()
}

fun Int.toBangla(): String {
    return this.toString().toBangla()
}
