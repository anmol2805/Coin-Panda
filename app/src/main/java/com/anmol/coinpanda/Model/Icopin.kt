package com.anmol.coinpanda.Model

class Icopin{
    var cid:String?=null
    var icocoin_name:String?=null
    var pinned_messages:String?=null
    var pinneddate:String?=null

    constructor()
    constructor(cid: String?, icocoin_name: String?, pinned_messages: String?, pinneddate: String?) {
        this.cid = cid
        this.icocoin_name = icocoin_name
        this.pinned_messages = pinned_messages
        this.pinneddate = pinneddate
    }

}