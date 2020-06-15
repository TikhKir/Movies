package com.example.movies.utils.rxutils

import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object RxComposers {
    fun <T> applySingleSchedulers(): SingleTransformer<T, T> {
        return SingleTransformer { single: Single<T> ->
            single.subscribeOn(
                Schedulers.io()
            ).observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun <T> applyObservableSchedulers(): ObservableTransformer<T, T> {
        return ObservableTransformer { observable: Observable<T> ->
            observable.subscribeOn(
                Schedulers.io()
            ).observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun <T> applyFlowableSchedulers(): FlowableTransformer<T, T> {
        return FlowableTransformer { flowable: Flowable<T> ->
            flowable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun <T> applyMaybeSchedulers(): MaybeTransformer<T, T> {
        return MaybeTransformer { maybe: Maybe<T> ->
            maybe.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        }
    }
}