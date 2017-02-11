package me.evrooij.groceries.models

import me.evrooij.groceries.network.ApiService
import me.evrooij.groceries.network.ClientInterface
import rx.Observable


/**
 * Author: eddy
 * Date: 11-2-17.
 */

class AccountManager() {

    fun getFriends(ownId: Int): Observable<List<AccountItem>> {

        return Observable.create {
            subscriber ->

            val callResponse = ApiService.createService(ClientInterface::class.java).getFriends(ownId)
            val response = callResponse.execute()

            if (response.isSuccessful) {
                val friends = response.body().map(::AccountItem)

                subscriber.onNext(friends)
                subscriber.onCompleted()
            } else {
                subscriber.onError(Throwable(response.message()))
            }
        }
    }
}
