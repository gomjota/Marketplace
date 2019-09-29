package com.juangomez.presentation.util

import org.mockito.Mockito
import org.mockito.Mockito.mock


inline fun <reified T> mock(): T = mock(T::class.java)

fun <T> any(): T = Mockito.any<T>()