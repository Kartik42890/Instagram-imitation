package com.example.instagramclone.Models

class Post {
    var postUrl:String=""
    var caption:String=""
    var uid:String=""
    constructor()
    constructor(postUrl: String, caption: String, uid: String) {
        this.postUrl = postUrl
        this.caption = caption
        this.uid = uid
    }

}