package me.evrooij.groceries.models

import rx.Observable


/**
 * Author: eddy
 * Date: 11-2-17.
 */

class AccountManager() {

    fun getFriends(): Observable<List<AccountItem>> {
//        return Observable.create({ subscriber ->


        return Observable.create {
            subscriber ->

            val accountList = mutableListOf<AccountItem>()
            for (i in 1..10) {
                accountList.add(
                        AccountItem(
                                Account(
                                        i,
                                        username = "User $i",
                                        email = "Mail $i"
                                )
                        ))
            }
            subscriber.onNext(accountList)
        }
    }
}
