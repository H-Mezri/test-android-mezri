package com.mezri.bigburger.data.errors

import androidx.annotation.StringRes
import com.mezri.bigburger.R

/**
 * Enum for app messages
 */
enum class AppMessages(@StringRes private val resourceId: Int) {
    ERROR_UNKNOWN(R.string.unknown_error),
    PRODUCTS_NOT_FOUND(R.string.products_not_found),
    NETWORK_ERROR(R.string.network_error),
    NO_NETWORK_AVAILABLE(R.string.no_network_available),
    PRODUCT_REMOVED_TO_BASKET(R.string.product_removed_to_basket),
    PRODUCT_ADDED_TO_BASKET(R.string.product_added_to_basket);

    fun getAppError(throwable: Throwable? = null): AppInformation {
        if (throwable != null) {
            // TODO send app errors to remote server ( Example: Fabric Crashlytics )
        }
        return AppInformation(this.resourceId, throwable)
    }
}