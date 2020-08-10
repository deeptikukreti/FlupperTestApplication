package com.live.fluppertestapplication.viewModel

data class StoreBean(
    var id: String,
    var store_name: String,
    var isSelected: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StoreBean

        if (id != other.id) return false
        if (store_name != other.store_name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + store_name.hashCode()
        return result
    }
}