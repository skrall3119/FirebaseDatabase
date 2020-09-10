package com.ebookfrenzy.firebasedatabase

data class Person(
    var firstName: String? = "",
    var lastName: String? = "",
    var address: String? = "",
    var email: String? = "",
    var id: String = "0"
)