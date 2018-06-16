package com.anmol.coinpanda.Model

class Icocoin{
    var ico_name:String?=null
    var telegram_url:String?=null
    var website:String?=null
    var medium_url:String?=null
    var crowdsale_date:String?=null
    var ico_status:String?=null
    var industry:String?=null
    var icodescription:String?=null
    var hardcap:String?=null
    var softcap:String?=null
    var twitter_url:String?=null
    var rating:String?=null

    constructor()
    constructor(ico_name: String?, telegram_url: String?, website: String?, medium_url: String?, crowdsale_date: String?, ico_status: String?, industry: String?, icodescription: String?, hardcap: String?, softcap: String?, twitter_url: String?, rating: String?) {
        this.ico_name = ico_name
        this.telegram_url = telegram_url
        this.website = website
        this.medium_url = medium_url
        this.crowdsale_date = crowdsale_date
        this.ico_status = ico_status
        this.industry = industry
        this.icodescription = icodescription
        this.hardcap = hardcap
        this.softcap = softcap
        this.twitter_url = twitter_url
        this.rating = rating
    }

}