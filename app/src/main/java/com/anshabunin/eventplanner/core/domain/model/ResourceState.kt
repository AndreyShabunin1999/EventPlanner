package com.anshabunin.eventplanner.core.domain.model

sealed class ResourceState {
    object LOADING : ResourceState()
    object MESSAGE : ResourceState()
    object SUCCESS : ResourceState()
    object EMPTY : ResourceState()
    object ERROR : ResourceState()
    object NETWORK_ERROR : ResourceState()
}