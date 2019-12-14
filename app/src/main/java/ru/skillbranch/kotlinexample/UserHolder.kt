package ru.skillbranch.kotlinexample

object UserHolder {
    private val map = mutableMapOf<String, User>()

    fun registerUser(
        fullName:String,
        email:String,
        password:String
    ):User{
        return User.makeUser(fullName, email=email, password=password)
            .also { user-> if (map.containsKey(user.login)) {
                throw IllegalArgumentException("A user with this email already exists")
            } else {
                map[user.login] = user
            }
        }
    }

    fun loginUser (login:String, password: String) : String? {
        val loginIsPhone = login.replace("[^+\\d]".toRegex(), "")
        when {
            loginIsPhone.length == 12 && loginIsPhone.first() == '+' -> {
                return map[loginIsPhone.trim()]?.run {
                    if (checkPassword(password)) this.userInfo
                    else null
                }
            }
            else -> {
                return map[login.trim()]?.run {
                    if (checkPassword(password)) this.userInfo
                    else null
                }
            }
        }
    }

    fun registerUserByPhone(
        fullName: String,
        rawPhone: String
    ):User{
        return User.makeUser(fullName, phone = rawPhone)
            .also { user-> if (map.containsKey(user.login)) {
                throw IllegalArgumentException("A user with this phone already exists")
            } else {
                val validPhone = user.login.replace("[^+\\d]".toRegex(), "")
                if ( validPhone.length == 12 && validPhone.first() == '+') {
                    map[user.login] = user
                } else {
                    throw IllegalArgumentException("Enter a valid phone number starting with a + and containing 11 digits")
                }
            }
        }
    }

    fun requestAccessCode(rawPhone: String): Unit {
        val user = map.get(rawPhone.replace("[^+\\d]".toRegex(), ""))
        user!!.changeAccessCode(rawPhone)
        return
    }
}