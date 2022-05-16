package com.demo.demoapplication.ui.models

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

class SearchListModel {
    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("image")
    @Expose
    var image: Int? = null

    constructor(name: String?, email: String?, image: Int?) {
        this.name = name
        this.email = email
        this.image = image
    }

    constructor(name: String?, email: String?) {
        this.name = name
        this.email = email
    }

    constructor() {}
}