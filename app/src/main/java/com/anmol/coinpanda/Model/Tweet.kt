package com.anmol.coinpanda.Model

/**
 * Created by anmol on 3/2/2018.
 */
class Tweet{
    var coin:String?=null
    var flag:Boolean = false
    var tid:String?=null
    var keywords: Array<String>? = null
    var main:String?=null
    var ngram2:String?=null
    var polarity:Number? = null
    var subjectivity:Number?=null
    var tweet:String?=null

    constructor()
    constructor(coin: String?, flag: Boolean, tid: String?, keywords: Array<String>?, main: String?, ngram2: String?, polarity: Number?, subjectivity: Number?, tweet: String?) {
        this.coin = coin
        this.flag = flag
        this.tid = tid
        this.keywords = keywords
        this.main = main
        this.ngram2 = ngram2
        this.polarity = polarity
        this.subjectivity = subjectivity
        this.tweet = tweet
    }

}