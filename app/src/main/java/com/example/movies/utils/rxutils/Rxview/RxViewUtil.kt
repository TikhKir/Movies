package com.example.movies.utils.rxutils.Rxview

import android.text.Editable
import android.text.TextWatcher
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import io.reactivex.Observable

class RxViewUtil {

    companion object {
        fun getTextWatcherObservable(editText: EditText): Observable<String> {
            var isFirst = true
            return Observable.create<String> { obsEmitter ->
                if (isFirst) {
                    obsEmitter.onNext(editText.text.toString())
                    isFirst = false
                }
                editText.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?, start: Int, count: Int, after: Int) {

                    }

                    override fun onTextChanged(
                        s: CharSequence?, start: Int, before: Int, count: Int) {
                        s?.let { obsEmitter.onNext(it.toString()) }

                    }

                    override fun afterTextChanged(s: Editable?) {

                    }

                })
            }

        }

        fun getSearchViewTextObservable(searchView: SearchView): Observable<String> {
            var isFirst = true
            return Observable.create { obsEmitter ->
                if (isFirst) {
                    obsEmitter.onNext(searchView.query.toString())
                    isFirst = false
                }
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        newText?.let { obsEmitter.onNext(it) }
                        return true
                    }

                })
            }
        }

        fun getCheckBoxStateObservable(checkBox: CheckBox): Observable<Boolean> {
            var isFirst = true
            return Observable.create { obsEmitter ->
                if (isFirst) {
                    obsEmitter.onNext(checkBox.isChecked)
                    isFirst = false
                }
                checkBox.setOnCheckedChangeListener(object :
                    CompoundButton.OnCheckedChangeListener {
                    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                        obsEmitter.onNext(isChecked)
                    }

                })
            }
        }
    }

}