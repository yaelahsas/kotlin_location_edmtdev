package com.karina.tracking.Model

class Usernya {
    var uid: String? = null
    var email: String? = null
    var acceptList: HashMap<String, Usernya>? = null

    constructor()
    constructor(uid: String?, email: String?) {
        this.uid = uid
        this.email = email
    }

}
