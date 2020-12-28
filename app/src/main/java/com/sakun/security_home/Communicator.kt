package com.sakun.security_home

interface Communicator {
    fun updateUserLocalData(userLocalData: UserLocalData)
    fun getUserLocalData(): UserLocalData?
    fun loginTimeout(): Boolean
    fun resetAppTimeout()
}