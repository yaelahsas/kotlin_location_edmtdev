/*
 * *
 *  * Created by Dhimas Panji Sastra on 4/30/21 2:11 AM
 *  * Copyright (c) $year . All rights reserved.
 *  * Last modified 4/30/21 2:10 AM
 *  * Made With ❤ for U
 *
 */

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
