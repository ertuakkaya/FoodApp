package com.example.foodapp.domain.model

/**
 * Domain model for Food entity.
 * 
 * This represents the core business entity for food items,
 * independent of data layer implementations or API responses.
 * 
 * Benefits:
 * - Clean separation from data layer DTOs
 * - Business logic can be added here
 * - Immutable by design
 * - Type safety with proper validation
 */
data class Food(
    val id: Int,
    val name: String,
    val imageName: String,
    val price: Int
) {
    init {
        require(id > 0) { "Food ID must be positive" }
        require(name.isNotBlank()) { "Food name cannot be blank" }
        require(imageName.isNotBlank()) { "Food image name cannot be blank" }
        require(price > 0) { "Food price must be positive" }
    }
    
    /**
     * Constructs the full image URL for this food item.
     */
    fun getImageUrl(): String = "http://kasimadalan.pe.hu/yemekler/resimler/$imageName"
    
    /**
     * Formats the price with currency symbol.
     */
    fun getFormattedPrice(): String = "$price TL"
    
    /**
     * Checks if this food item is available (basic business rule).
     */
    fun isAvailable(): Boolean = price > 0 && name.isNotBlank()
    
    /**
     * Calculates total price for given quantity.
     */
    fun calculateTotalPrice(quantity: Int): Int {
        require(quantity > 0) { "Quantity must be positive" }
        return price * quantity
    }
}

/**
 * Domain model for Cart Food entity.
 */
data class CartFood(
    val cartId: Int,
    val food: Food,
    val quantity: Int,
    val userName: String
) {
    init {
        require(cartId > 0) { "Cart ID must be positive" }
        require(quantity > 0) { "Quantity must be positive" }
        require(userName.isNotBlank()) { "Username cannot be blank" }
    }
    
    /**
     * Calculates total price for this cart item.
     */
    fun getTotalPrice(): Int = food.calculateTotalPrice(quantity)
    
    /**
     * Creates a new cart item with updated quantity.
     */
    fun updateQuantity(newQuantity: Int): CartFood {
        require(newQuantity > 0) { "Quantity must be positive" }
        return copy(quantity = newQuantity)
    }
}