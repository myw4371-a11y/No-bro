package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bazar_sessions")
data class BazarSession(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String, // "বড় বাজার" or "ছোট বাজার"
    val date: String, // e.g., "১১ জুলাই, ২০২৬"
    val isPermanent: Boolean = false, // If true, the session is locked
    val totalAmount: Double = 0.0
)

@Entity(tableName = "bazar_items")
data class BazarItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sessionId: Int, // Links to BazarSession
    val name: String,
    val quantity: Double? = null, // কেজি
    val unitPrice: Double? = null, // টাকা
    val totalPrice: Double = 0.0, // মোট
    val isSaved: Boolean = false // Tick checked
)

@Entity(tableName = "people")
data class Person(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String
)

@Entity(tableName = "money_entries")
data class MoneyEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val personId: Int, // Links to Person
    val date: String, // e.g., "৫ জুলাই"
    val amount: Double,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "meal_entries")
data class MealEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val personId: Int, // Links to Person
    val date: String, // e.g., "৫ জুলাই"
    val count: Double, // e.g., 3.0, 1.5
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "egg_entries")
data class EggEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String, // e.g., "১১ জুলাই"
    val count: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "egg_add_entries")
data class EggAddEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String, // e.g., "১১ জুলাই"
    val count: Int,
    val timestamp: Long = System.currentTimeMillis()
)
